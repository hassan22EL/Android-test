package com.example.hees.Actvity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.hees.Database.DatabaseNames;
import com.example.hees.Model.User;
import com.example.hees.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {
    private static final int GALLERY_RESQUEST_CODE = 1;
    private static final int CAMERA_RESQUEST_CODE = 2;
    private CircleImageView ImageProfile;
    private EditText username_text;
    private Button welocme;
    private Spinner gendertype;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    UploadTask Task;
    File Image;
    private String item[] = {"Male", "Female"};
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ImageProfile = (CircleImageView) findViewById(R.id.imageProfile);
        username_text = (EditText) findViewById(R.id.username);
        gendertype = (Spinner) findViewById(R.id.gender);
        SpinnerGender();
        welocme = (Button) findViewById(R.id.welocme);
        View bottomsheet = findViewById(R.id.design_bottom_sheet);
        welocme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validusername()) {
                    UpdateUsernameatDatabase();
                }
            }
        });

        ImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetLayout();
            }
        });
    }

    private File createImage() throws IOException {
        String timestam = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String ImageName = "_JPEG_" + timestam + "_";
        File FileStoreDir = new File(Environment.getExternalStorageDirectory() + "/HEESApp/Media", "ProfilePhotos");
        if (!FileStoreDir.exists()) {
            FileStoreDir.mkdirs();
        }

        String pathImage = FileStoreDir.getPath();
        Image = File.createTempFile(ImageName, ".png", new File(pathImage));
        uri = Uri.fromFile(Image);
        return Image;
    }

    private void SpinnerGender() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (UserInfoActivity.this, android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gendertype.setAdapter(adapter);
        gendertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                DatabaseNames names = new DatabaseNames();
                reference = FirebaseDatabase.getInstance().getReference(names.getUsers()).child(firebaseUser.getUid());
                HashMap<String, Object> map = new HashMap<>();
                switch (i) {
                    case 0:
                        map.put("userGender", item[0]);
                        reference.updateChildren(map);
                        ImageProfile.setImageResource(R.drawable.boy_deflaut_image);
                        break;
                    case 1:
                        map.put("userGender", item[1]);
                        reference.updateChildren(map);
                        ImageProfile.setImageResource(R.drawable.giral_defalut_image);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void ImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(this, "com.example.hees", createImage()));
            startActivityForResult(intent, CAMERA_RESQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error Camera", e.getMessage());
        }

    }

    private void ImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String mimType[] = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimType);
        startActivityForResult(intent, GALLERY_RESQUEST_CODE);

    }

    private boolean validusername() {
        String username = username_text.getText().toString();
        boolean isUsername;
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(UserInfoActivity.this, "Pelase Enter your Name", Toast.LENGTH_SHORT).show();
            isUsername = false;
        } else if (username.length() <= 30) {
            isUsername = true;
        } else {
            isUsername = false;
        }
        return isUsername;
    }

    private void UpdateUsernameatDatabase() {
        final String username = username_text.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseNames databaseNames = new DatabaseNames();
        reference = FirebaseDatabase.getInstance().getReference(databaseNames.getUsers()).child(firebaseUser.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(UserInfoActivity.this, HEESAppActivity.class);
                    startActivity(intent);
                    Log.e("Name", "scesss " + task.getResult());
                } else {
                    Log.e("Name", "task falil");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Name", "Filauer");
            }
        });
    }

    private boolean PermissionCamrea() {
        String TAG = "Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Perimmission is granted");
                ImageFromCamera();
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 1);
                Log.v(TAG, "Perimmision is inVolked");
                return false;
            }
        } else {
            ImageFromCamera();
            Log.e(TAG, "Perimission is granted");
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            ImageFromCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_RESQUEST_CODE:
                    Uri image = data.getData();
                    Log.e("urigallery", image.toString());
                    upload(image);
                    break;
                case CAMERA_RESQUEST_CODE:
                    Uri imageC = uri;
                    Log.e("uricamera", imageC.toString());
                    upload(imageC);
                    break;
            }
        }


    }


    private void upload(Uri Image) {
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Upload").child("Images");
        Task = storageReference.putFile(Image);
        Task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri uri = task.getResult();
                    Glide.with(UserInfoActivity.this).load(uri).into(ImageProfile);
                    Log.e("upload Image", "Complete upload ");
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    DatabaseNames names = new DatabaseNames();
                    String imageUrl = uri.toString();
                    reference = FirebaseDatabase.getInstance().getReference(names.getUsers()).child(firebaseUser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userImageProfile", imageUrl);
                    reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e("ImageDatabase", "Upload image a database sucess");
                                Toast.makeText(UserInfoActivity.this, "Upload Photo Sucess", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("ImageDatabase", "Upload image a database Faile");
                                Toast.makeText(UserInfoActivity.this, "Upload Photo Faile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Log.e("ImageUpload", "Upload image a  Faile");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("upload Image", "Failure\t " + e.getMessage());
            }
        });
    }

    private void bottomSheetLayout() {
        final BottomSheetDialog bottomSheetDialog;

        final View bottomsheetlayout = getLayoutInflater().inflate(R.layout.dialog_photo, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        CircleImageView image_camera = (CircleImageView) bottomsheetlayout.findViewById(R.id.camera_Image);
        CircleImageView image_garlley = (CircleImageView) bottomsheetlayout.findViewById(R.id.gallrey_Image);
        Button cencel = (Button) bottomsheetlayout.findViewById(R.id.cencel_btn);
        bottomSheetDialog.setContentView(bottomsheetlayout);
        bottomSheetDialog.show();
        image_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionCamrea();
                bottomSheetDialog.dismiss();
            }

        });

        image_garlley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageFromGallery();
                bottomSheetDialog.dismiss();
            }
        });
        cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }


}