package com.example.hees.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hees.Actvity.ChatActivity;
import com.example.hees.Model.Contact;
import com.example.hees.Model.User;
import com.example.hees.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder0> {
    private Context mContext;
    private List<User> mContactList;
    private List<Contact> mContact;

    //Adapter

    public ContactAdapter(Context mContext, List<User> mContactList, List<Contact> mContact) {
        this.mContext = mContext;
        this.mContactList = mContactList;
        this.mContact = mContact;
    }

    @NonNull
    @Override
    public ViewHolder0 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_recycleview_item, parent, false);
        return new ViewHolder0(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder0 holder, int position) {

        final User user = mContactList.get(position);
        Contact contact = mContact.get(position);
        String gender = user.getUserGender();
        String Image = user.getUserImageProfile();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String contactname = contact.getContactname();
        holder.username.setText(contactname);
        if (Image.equals("defalut") && gender.equals("Male")) {
            holder.ImageProfile.setImageResource(R.drawable.boy_deflaut_image);
        } else if (Image.equals("defalut") && gender.equals("Female")) {
            holder.ImageProfile.setImageResource(R.drawable.giral_defalut_image);
        } else {
            Glide.with(mContext).load(user.getUserImageProfile()).into(holder.ImageProfile);
        }
        holder.Abuot.setText(user.getAbout());
        if (!user.getLastSeen().equals("Active")) {
            String last = time(user.getLastSeen());
            holder.Lastseen.setText(last);
        } else {
            holder.Lastseen.setText(user.getLastSeen());
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                contactViewopetion();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userid", user.getUserid());
                mContext.startActivity(intent);


            }
        });
    }


    @Override
    public int getItemCount() {
        boolean a = true;
        if (a) {
            return mContact.size();
        }
        return mContactList.size();
    }


    public class ViewHolder0 extends RecyclerView.ViewHolder {
        private CircleImageView ImageProfile;
        private TextView Abuot;
        private TextView Lastseen;
        private TextView username;

        public ViewHolder0(@NonNull View itemView) {
            super(itemView);
            ImageProfile = (CircleImageView) itemView.findViewById(R.id.phtoprofilerec);
            Abuot = (TextView) itemView.findViewById(R.id.aboutrec);
            Lastseen = (TextView) itemView.findViewById(R.id.lastseenrec);
            username = (TextView) itemView.findViewById(R.id.usernamerec);
        }
    }

    private void contactViewopetion() {
        final BottomSheetDialog bottomSheetDialog;
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_option, null);
        bottomSheetDialog = new BottomSheetDialog(mContext);
        CircleImageView createnewgroup = (CircleImageView) view.findViewById(R.id.cretenewGroup);
        CircleImageView Blockuser = (CircleImageView) view.findViewById(R.id.blocking);
        CircleImageView dontcare = (CircleImageView) view.findViewById(R.id.DeleteUser);
        CircleImageView basetoff = (CircleImageView) view.findViewById(R.id.show);
        Button cencel = (Button) view.findViewById(R.id.cencel_btn);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        createnewgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "create new Group", Toast.LENGTH_SHORT).show();
            }
        });
        Blockuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Block user", Toast.LENGTH_SHORT).show();
            }
        });

        dontcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "dont care user", Toast.LENGTH_SHORT).show();
            }
        });

        basetoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "best of user", Toast.LENGTH_SHORT).show();
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
                    TimeStoreDatabase = date;
                }
                Log.e("error", "TimeStoreDatabase:" + TimeStoreDatabase);
                break;
            }
        }

        return TimeStoreDatabase;
    }


}



