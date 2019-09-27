package com.example.hees.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hees.Model.Message;
import com.example.hees.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //constant vairable
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    //Gaboable virable
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private List<Message> mMessages;
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public chatAdapter(Context mContext, List<Message> mMessages) {
        this.mMessages = mMessages;
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = mLayoutInflater.inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolderLeft(view);
        } else if (viewType == MSG_TYPE_RIGHT) {
            View view = mLayoutInflater.inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolderRight(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        switch (holder.getItemViewType()) {

            case MSG_TYPE_LEFT:
                ViewHolderLeft holderLeft = (ViewHolderLeft) holder;
                if (message.getText().equals("null") && !message.getImage().equals("image")) {
                    Glide.with(mContext).load(message.getImage()).into(holderLeft.image);
                    holderLeft.image.setVisibility(View.VISIBLE);
                    holderLeft.messageRight.setVisibility(View.GONE);
                } else {
                    holderLeft.messageRight.setText(message.getText());
                    holderLeft.image.setVisibility(View.GONE);
                    holderLeft.messageRight.setVisibility(View.VISIBLE);
                }
                if (message.getUnReceive().equals("unreceive")) {
                    holderLeft.messageUnReceive.setVisibility(View.VISIBLE);
                    holderLeft.messageRead.setVisibility(View.GONE);
                    holderLeft.messageReceive.setVisibility(View.GONE);
                } else if (message.getUnReceive().equals("receive")) {
                    holderLeft.messageUnReceive.setVisibility(View.GONE);
                    holderLeft.messageRead.setVisibility(View.VISIBLE);
                    holderLeft.messageReceive.setVisibility(View.GONE);
                } else if (message.getUnReceive().equals("read")) {
                    holderLeft.messageUnReceive.setVisibility(View.GONE);
                    holderLeft.messageRead.setVisibility(View.VISIBLE);
                    holderLeft.messageReceive.setVisibility(View.GONE);
                }
                break;
            case MSG_TYPE_RIGHT:
                ViewHolderRight holderright = (ViewHolderRight) holder;
                if (message.getText().equals("null") && !message.getImage().equals("image")) {
                    Glide.with(mContext).load(message.getImage()).into(holderright.image);
                    holderright.image.setVisibility(View.VISIBLE);
                    holderright.messageleft.setVisibility(View.GONE);
                } else {
                    holderright.messageleft.setText(message.getText());
                    holderright.image.setVisibility(View.GONE);
                    holderright.messageleft.setVisibility(View.VISIBLE);
                }
                String messageTime = time(message.getTimeSender());
                holderright.TimeSender.setText(messageTime);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mMessages.get(position).getSenderid().equals(mUser.getUid())) {
            return MSG_TYPE_LEFT;
        } else {
            return MSG_TYPE_RIGHT;

        }
    }

    private class ViewHolderRight extends RecyclerView.ViewHolder {
        private TextView messageleft;
        private TextView TimeSender;
        private ImageView image;

        public ViewHolderRight(@NonNull View itemView) {
            super(itemView);
            messageleft = (TextView) itemView.findViewById(R.id.meesageshow);
            TimeSender = (TextView) itemView.findViewById(R.id.receive);
            image = (ImageView) itemView.findViewById(R.id.image);

        }


    }

    private class ViewHolderLeft extends RecyclerView.ViewHolder {

        private ImageView messageRead;
        private ImageView messageUnReceive;
        private ImageView messageReceive;
        private TextView messageRight;
        private ImageView image;

        public ViewHolderLeft(@NonNull View itemView) {
            super(itemView);
            messageRight = (TextView) itemView.findViewById(R.id.meesageshow);
            messageUnReceive = (ImageView) itemView.findViewById(R.id.unread);
            messageReceive = (ImageView) itemView.findViewById(R.id.receive);
            messageRead = (ImageView) itemView.findViewById(R.id.read);
            image = (ImageView) itemView.findViewById(R.id.image);

        }

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
                break;
            }
        }

        String intcurrnentdata = currentTime.replaceAll("/", "");
        Log.e("data", intcurrnentdata);
        int Currentdate = Integer.parseInt(intcurrnentdata);
        String intdata = date.replaceAll("/", "");
        Log.e("data", intdata);
        int dateInt = Integer.parseInt(intdata);
        int diff = Currentdate - dateInt;
        if (diff == 0) {
            TimeStoreDatabase = "Today:" + Hour;

        } else if (diff == 1) {
            TimeStoreDatabase = "Yesterday" + Hour;
        } else {
            TimeStoreDatabase = "at" + date;
        }
        Log.e("data", TimeStoreDatabase);
        return TimeStoreDatabase;
    }

}
