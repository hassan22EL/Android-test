package com.example.hees.Actvity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hees.Adapter.chatAdapter;
import com.example.hees.Database.DatabaseNames;
import com.example.hees.Model.Message;
import com.example.hees.Model.User;
import com.example.hees.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    //Loacl Viarable
    private static final int CAMERA_IMAGE = 0;
    private static final int GALLERY_IMAGR = 1;
    private String userid;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private DatabaseNames mNams;
    private HashMap<String, Object> map;
    private List<Message> mMessages;
    private chatAdapter mAdapter;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private UploadTask mTask;
    private Uri uri;
    private int count = 0;


    private DrawerLayout mDrawerLayout;
    private RelativeLayout navigationViewRight;
    private RelativeLayout navigationViewLeft;
    private Toolbar toolbar;
    private CircleImageView imageprofile;
    private CircleImageView viewcintacticon;
    private TextView username;
    private TextView Lastseen;
    private RecyclerView recycelcaht;
    private EditText meesage;
    private Button emoje;
    private Button btn_camera;
    private Button btn_voice;
    private Button btn_send;
    private Button btn_link;
    //nav right
    private LinearLayout mediaIcon;
    private LinearLayout sherach;
    private LinearLayout notification;
    //nav left
    private LinearLayout ViewContact;
    private LinearLayout Block;
    private LinearLayout deletechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        View();
        setUpToolbar();
        setUpRecycleView();
        showbtnSend();
        viewContact();
        btnsendmessage();
        setvimageviewcontact();
        btnCamera();


    }

    private void View() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.chat);
        navigationViewRight = (RelativeLayout) findViewById(R.id.navchat);
        navigationViewLeft = (RelativeLayout) findViewById(R.id.navchatleft);
        toolbar = (Toolbar) findViewById(R.id.toolar);
        imageprofile = (CircleImageView) findViewById(R.id.imageprofile);
        username = (TextView) findViewById(R.id.username);
        Lastseen = (TextView) findViewById(R.id.Lastseen);
        recycelcaht = (RecyclerView) findViewById(R.id.recycelcaht);
        //chatedit
        emoje = (Button) findViewById(R.id.emoje);
        meesage = (EditText) findViewById(R.id.meesage);
        btn_voice = (Button) findViewById(R.id.btn_voice);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_link = (Button) findViewById(R.id.btn_link);
        //nave
        viewcintacticon = (CircleImageView) findViewById(R.id.viewcintacticon);
        mediaIcon = (LinearLayout) findViewById(R.id.MediaIcon);
        sherach = (LinearLayout) findViewById(R.id.Sherach);
        notification = (LinearLayout) findViewById(R.id.Notification);
        ViewContact = (LinearLayout) findViewById(R.id.ViewContact);
        Block = (LinearLayout) findViewById(R.id.Block);
        deletechat = (LinearLayout) findViewById(R.id.deletechat);


    }

    private void showbtnSend() {
        meesage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_voice.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private String ReadIntent() {
        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");
        return userid;

    }

    private void viewContact() {
        ViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ViewContactActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setvimageviewcontact() {
        mAuth = FirebaseAuth.getInstance();
        userid = ReadIntent();
        mNams = new DatabaseNames();
        final String myuser = mAuth.getCurrentUser().getUid();
        mReference = FirebaseDatabase.getInstance().getReference(mNams.getUsers()).child(userid);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                String lastseen = time(user.getLastSeen());
                Lastseen.setText("Last seen: " + lastseen);
                if (user.getUserImageProfile().equals("defalut") && user.getUserGender().equals("Male")) {
                    imageprofile.setImageResource(R.drawable.boy_deflaut_image);
                    viewcintacticon.setImageResource(R.drawable.boy_deflaut_image);
                } else if (user.getUserImageProfile().equals("defalut") && user.getUserGender().equals("Female")) {
                    imageprofile.setImageResource(R.drawable.giral_defalut_image);
                    viewcintacticon.setImageResource(R.drawable.giral_defalut_image);
                } else {
                    Glide.with(ChatActivity.this).load(user.getUserImageProfile()).into(imageprofile);
                    Glide.with(ChatActivity.this).load(user.getUserImageProfile()).into(viewcintacticon);
                }
                readMessag(myuser, userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setUpRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recycelcaht.setLayoutManager(linearLayoutManager);
    }

    private void SendMeesage() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mNams = new DatabaseNames();
        mReference = FirebaseDatabase.getInstance().getReference();
        map = new HashMap<>();
        String senderid = mUser.getUid();
        String userid = ReadIntent();
        String message = getmessage();
        String time = getTime();
        map.put("Senderid", senderid);
        map.put("Receiverid", userid);
        map.put("Text", message);
        map.put("TimeSender", time);
        map.put("unReceive", "unreceive");
        map.put("Image", "image");
        mReference.child(mNams.getChats()).push().setValue(map);
    }

    private String getmessage() {
        if (isValidmessage()) {
            String messageget = meesage.getText().toString();
            return messageget;
        } else {
            return null;
        }
    }

    private boolean isValidmessage() {
        String messageValid = meesage.getText().toString();
        if (TextUtils.isEmpty(messageValid)) {
            return false;

        } else {
            return true;
        }

    }

    private String getTime() {
        String time = new SimpleDateFormat("yyyy/MM/dd-HH:mm").format(new Date());
        return time;
    }

    private void btnsendmessage() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidmessage()) {
                    SendMeesage();
                    meesage.setText("");
                    btn_send.setVisibility(View.GONE);
                    btn_voice.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(ChatActivity.this, "can’t send empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void readMessag(final String senderid, final String receiveid) {
        mMessages = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mNams = new DatabaseNames();
        mReference = FirebaseDatabase.getInstance().getReference(mNams.getChats());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message1 = snapshot.getValue(Message.class);
                    if (message1.getReceiverid().equals(senderid) && message1.getSenderid().equals(receiveid) ||
                            message1.getReceiverid().equals(receiveid) && message1.getSenderid().equals(senderid)) {
                        mMessages.add(message1);
                    } else {
                        Toast.makeText(ChatActivity.this, "error read message", Toast.LENGTH_SHORT).show();
                    }

                }
                Log.e("error read message", "onDataChange:" + String.valueOf(mMessages.size()));
                mAdapter = new chatAdapter(ChatActivity.this, mMessages);
                recycelcaht.setAdapter(mAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadImage(Uri image) {
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference("Upload").child("Images");
        mTask = mStorageReference.putFile(image);
        mTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return mStorageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri uri = task.getResult();
                    mAuth = FirebaseAuth.getInstance();
                    mNams = new DatabaseNames();
                    map = new HashMap<>();
                    mReference = FirebaseDatabase.getInstance().getReference(mNams.getChats());
                    Log.e("upload Image", "Complete upload ");
                    String message;
                    String userid = ReadIntent();
                    if (isValidmessage()) {
                        message = getmessage();
                    } else {
                        message = "null";
                    }
                    String senderid = mAuth.getCurrentUser().getUid();
                    map.put("Senderid", senderid);
                    map.put("Receiverid", userid);
                    map.put("Text", message);
                    String time = getTime();
                    map.put("TimeSender", time);
                    map.put("unReceive", "unreceive");
                    map.put("Image", uri.toString());
                    mReference.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e("ImageDatabase", "Upload image a database sucess");
                            } else {
                                Log.e("ImageDatabase", "Upload image a database Faile");
                            }
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Fali Upload", e.getMessage());
            }
        });


    }

    private String time(String time) {
        String TimeStoreDatabase = null;
        String date = null;
        String Hour = null;
        String currentTime = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        for (int i = 0; i < time.length(); i++) {
            if (time.charAt(i) == '-') {
                date = time.substring(0, i);
                Hour = time.substring(i + 1, time.length());
                String intcurrnentdata = currentTime.replaceAll("/", "");
                Log.e("data", intcurrnentdata);
                int Currentdate = Integer.parseInt(intcurrnentdata);
                String intdata = date.replaceAll("/", "");
                Log.e("data", intdata);
                int dateInt = Integer.parseInt(intdata);
                int diff = Currentdate - dateInt;
                if (diff == 0) {
                    TimeStoreDatabase = "Today\t" + Hour;

                } else if (diff == 1) {
                    TimeStoreDatabase = "Yesterday\t" + Hour;
                } else {
                    TimeStoreDatabase = "\t" + date;
                }
                Log.e("data", TimeStoreDatabase);
                break;
            }
        }

        return TimeStoreDatabase;
    }

    private void btnCamera() {
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }


}


