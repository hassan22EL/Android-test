package com.example.hees.Actvity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hees.Adapter.ContactAdapter;
import com.example.hees.Database.DatabaseNames;
import com.example.hees.Fragment.ChatsFragment;
import com.example.hees.Fragment.ContactsFragment;
import com.example.hees.Fragment.GroupsFragment;
import com.example.hees.Fragment.StatusFragment;
import com.example.hees.Model.Contact;
import com.example.hees.Model.User;
import com.example.hees.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class HEESAppActivity extends AppCompatActivity {

    //views
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private NavigationView nav;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private CircleImageView ImageProfile;
    private TextView usernmae_text;
    private TextView nave_username;
    private CircleImageView naveimage;
    private LinearLayout linearLayout;


    private ContactAdapter mAdapter;

    //user operations
    private List<User> mUsers;
    private List<Contact> mContacts;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    //nav bottom exute
    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.chats:
                    ChatsFragment chatsFragment = ChatsFragment.newInstance();
                    openFragment(chatsFragment);
                    Toast.makeText(HEESAppActivity.this, "Chats", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.groups:
                    GroupsFragment groupsFragment = GroupsFragment.newInstance();
                    openFragment(groupsFragment);
                    Toast.makeText(HEESAppActivity.this, "groups", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.status:
                    StatusFragment statusFragment = StatusFragment.newInstance();
                    openFragment(statusFragment);
                    Toast.makeText(HEESAppActivity.this, "status", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.onlineUser:
                    ContactsFragment contactsFragment = ContactsFragment.newInstance();
                    openFragment(contactsFragment);
                    Toast.makeText(HEESAppActivity.this, "online", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heesapp);
        //views
        views();
        //setuptoolbar
        setUpToolbar();
        //exuetue mthodes nav bottom
        bottomNavigationView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);
        permission();
        SetUpRecycleView();
        slideShowcontact();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HEESAppActivity.this, profileActivity.class);
                startActivity(intent);
            }
        });

    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.fcontainer, fragment);
        fragmentManager.addToBackStack(null);
        fragmentManager.commit();

    }

    @Override
    protected void onStart() {
        setData();
        connectdatabase();
        super.onStart();
    }

    private void setData() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseNames names = new DatabaseNames();
        reference = FirebaseDatabase.getInstance().getReference(names.getUsers()).child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usernmae_text.setText(user.getUsername());
                nave_username.setText(user.getUsername());
                String gender = user.getUserGender();
                if (gender.equals("Male") && user.getUserImageProfile().equals("defalut")) {
                    ImageProfile.setImageResource(R.drawable.boy_deflaut_image);
                    naveimage.setImageResource(R.drawable.boy_deflaut_image);
                } else if (gender.equals("Female") && user.getUserImageProfile().equals("defalut")) {
                    ImageProfile.setImageResource(R.drawable.giral_defalut_image);
                    naveimage.setImageResource(R.drawable.giral_defalut_image);
                } else {
                    Glide.with(HEESAppActivity.this).load(user.getUserImageProfile()).into(ImageProfile);
                    Glide.with(HEESAppActivity.this).load(user.getUserImageProfile()).into(naveimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void slideShowcontact() {
        //slideshow caontact
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawble_open, R.string.draable_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void SetUpRecycleView() {
        //recycle view bottom
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HEESAppActivity.this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration item = new DividerItemDecoration(HEESAppActivity.this, DividerItemDecoration.VERTICAL);
        final Drawable m = getDrawable(R.drawable.line);
        item.setDrawable(m);
        recyclerView.addItemDecoration(item);
        recyclerView.setLayoutManager(linearLayoutManager);
        mUsers = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final HashMap<String, Object> map = new HashMap<>();
        DatabaseNames n = new DatabaseNames();
        reference = FirebaseDatabase.getInstance().getReference(n.getUsers());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (!(user.getUserid().equals(firebaseUser.getUid()))) {
                        mUsers.add(user);
                    }


                }
                mContacts = new ArrayList<>();
                DatabaseNames names = new DatabaseNames();

                DatabaseReference mReference = FirebaseDatabase.getInstance().
                        getReference(names.getContact()).child(firebaseUser.getUid());

                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mContacts.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Contact c = snapshot.getValue(Contact.class);
                            if (!(c.getUserid().equals(firebaseUser.getUid()))) {
                                mContacts.add(c);
                            }

                        }
                        Log.e("Contacct", "onDataChange: " + String.valueOf(mContacts.get(0).getContactname()));
                        mAdapter = new ContactAdapter(HEESAppActivity.this, mUsers, mContacts);
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void views() {
        //Views
        toolbar = (Toolbar) findViewById(R.id.toolar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        usernmae_text = (TextView) findViewById(R.id.username);
        ImageProfile = (CircleImageView) findViewById(R.id.imageprofile);
        nav = (NavigationView) findViewById(R.id.shitstuss);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.contact);
        recyclerView = (RecyclerView) findViewById(R.id.recycelList);
        nave_username = (TextView) findViewById(R.id.users);
        naveimage = (CircleImageView) findViewById(R.id.imagere);
        linearLayout = (LinearLayout) findViewById(R.id.k);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }


    private HashMap<Integer, List> ReadContacts() {
        List ContactName = new ArrayList();
        List ContactPhone = new ArrayList();
        List ContactID = new ArrayList();
        HashMap<Integer, List> mListContact = new HashMap<>();
        ContentResolver mResolver = getContentResolver();
        Cursor cr = mResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null);
        int i = 1;
        if (cr.getCount() > 0) {
            while (cr.moveToNext()) {
                if (i == 1) {
                    cr.moveToFirst();
                    i++;
                }

                //readid
                String id = cr.getString(cr.getColumnIndex(ContactsContract.Contacts._ID));
                String Contactname = cr.getString(cr.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                ContactName.add(Contactname);
                ContactID.add(id);


                //Read Phone
                Cursor phoneCur = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        new String[]{id}, null);

                if (phoneCur.moveToNext()) {
                    String Phone = phoneCur.
                            getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ContactPhone.add(Phone);

                }
                phoneCur.close();

            }
            cr.close();
            Log.e("ContactPhone", String.valueOf(ContactPhone.size()));
            Log.e("ContactName", String.valueOf(ContactName.size()));
            mListContact.put(0, ContactID);
            mListContact.put(1, ContactName);
            mListContact.put(2, ContactPhone);
        }
        return mListContact;
    }

    private boolean permission() {
        String TAG = "Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Perimmission is granted");
                readPhone();
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, 1);
                Log.v(TAG, "Perimmision is inVolked");
                return false;
            }
        } else {
            readPhone();
            Log.e(TAG, "Perimission is granted");
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        readPhone();

    }

    private void ContactDatabase(List a, List b) {
        List phoneList = new ArrayList();
        List namelist = new ArrayList();
        List idlist = new ArrayList();
        HashMap<Integer, List> list = new HashMap<>();
        HashMap<String, String> map = new HashMap<>();


        list = ReadContacts();
        idlist = list.get(0);
        namelist = list.get(1);
        phoneList = list.get(2);

        mAuth = FirebaseAuth.getInstance();
        DatabaseNames n = new DatabaseNames();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        Log.e("phoneList", String.valueOf(a.size()));
        for (int j = 0; j < a.size(); j++) {
            String phonematch = (String) a.get(j);
            String id = (String) b.get(j);
            Log.e("phoneM", phonematch);
            for (int i = 0; i < phoneList.size(); i++) {
                String ID = (String) idlist.get(i);
                String phone = (String) phoneList.get(i);
                String PhoneNumber = phone.replaceAll("\\s", "");
                String PhoneCode = addCode(PhoneNumber);
                String name = (String) namelist.get(i);
                if (phonematch.equals(PhoneCode)) {
                    reference = FirebaseDatabase.getInstance().getReference(n.getContact())
                            .child(firebaseUser.getUid()).child(ID);

                    map.put("userid", id);
                    map.put("id", ID);
                    map.put("contactname", name);
                    map.put("contactphone", PhoneNumber);
                    reference.setValue(map);
                    break;
                }
            }
        }
    }

    private void readPhone() {
        final List<String> a = new ArrayList<String>();
        final List<String> id = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        DatabaseNames n = new DatabaseNames();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(n.getUsers());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                    a.add(user.getUserPhone());
                    id.add(user.getUserid());

                }
                ContactDatabase(a, id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String addCode(String Phone) {
        if (!Phone.startsWith("+")) {

            String PhoneNumber = "+2" + Phone;
            return PhoneNumber;
        } else {
            return Phone;
        }
    }

    private void connectdatabase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseNames names = new DatabaseNames();
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(names.getUsers()).child(firebaseUser.getUid());
        map.put("userState", "true");
        map.put("LastSeen", "Active");
        reference.updateChildren(map);
        map1.put("LastSeen", isSeen());
        map1.put("userState", "false");
        reference.onDisconnect().updateChildren(map1);
    }


    private String isSeen() {
        String time = new SimpleDateFormat("yyyy/MM/dd-HH:mm").format(new Date());
        return time;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        Log.e("exit App", "Applection exit");
        System.exit(1);
    }
}




