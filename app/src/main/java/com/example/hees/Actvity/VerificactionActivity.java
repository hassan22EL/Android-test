package com.example.hees.Actvity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hees.Database.DatabaseNames;
import com.example.hees.Model.Country;
import com.example.hees.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerificactionActivity extends AppCompatActivity {
    boolean mVerificationInProgress = false;
    String Phone;
    private String TAG = "Phoneauth";
    private EditText E_VerifiyCode;
    private SparseArray<Country> mArray = new SparseArray<>();
    private TextView T_ChangePhoneNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseUser mUser;
    private String verificationId;
    private TextView CountTime_text;
    private TextView ResendMessage_text;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificaction);
        E_VerifiyCode = findViewById(R.id.verifiy_code);
        T_ChangePhoneNumber = findViewById(R.id.change_phoneNumber);
        CountTime_text = findViewById(R.id.countdown);
        ResendMessage_text = findViewById(R.id.resendmessage);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(final PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "sucess");
                String code = phoneAuthCredential.getSmsCode();
                E_VerifiyCode.setText(code);
                Registation(phoneAuthCredential);
                mVerificationInProgress = false;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);
            }

            @Override
            public void onCodeSent(String mVerificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(mVerificationId, forceResendingToken);
                mVerificationId = mVerificationId;
                mResendToken = forceResendingToken;
                Log.d(TAG, "onCodeSent:" + mVerificationId);


            }
        };


        String FullText = T_ChangePhoneNumber.getText().toString();
        if (FullText.contains("Worng")) {
            setClickableText(T_ChangePhoneNumber, "Worng", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(VerificactionActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            });
        } else {
            Log.e("Error", "Not Found");
        }


    }

    private void setClickableText(final TextView tv, String textToHighlight, final View.OnClickListener onClickListener) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                if (onClickListener != null) onClickListener.onClick(tv);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.GREEN);
                ds.setUnderlineText(true);
            }
        };
        SpannableString wordToSapn = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1) {
                break;
            } else {
                wordToSapn.setSpan(span, ofe, ofe + textToHighlight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSapn, TextView.BufferType.SPANNABLE);
                tv.setMovementMethod(LinkMovementMethod.getInstance());

            }
        }
    }

    @Override
    protected void onStart() {

        Intent intent = getIntent();
        Phone = intent.getStringExtra("Phone");
        PhoneVerifing(Phone);

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                CountTime_text.setText("Resend sms message after:\t" + l / 1000);
            }

            @Override
            public void onFinish() {
                CountTime_text.setText("Send SMS Message");
                ResendMessage_text.setTextColor(Color.CYAN);
                ResendMessage_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ResendMessage_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ResendMessage(Phone, mResendToken);
                            }
                        });
                    }
                });
            }
        }.start();

        super.onStart();
    }

    private void PhoneVerifing(String Phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Phone,
                60L,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
        mVerificationInProgress = true;

    }

    private void ResendMessage(String Phone, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Phone,
                60L,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token
        );

    }

    private void Registation(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    createDatabaseUser();
                    Intent intent = new Intent(VerificactionActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                    Log.e("errorSign", "true");

                } else {
                    Log.e("errorSign", "flase");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
    }

    private void createDatabaseUser() {
        DatabaseNames names = new DatabaseNames();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(names.getUsers()).child(firebaseUser.getUid());
        HashMap<String, String> map = new HashMap<>();
//        if (reference == null) {
        map.put("userid", firebaseUser.getUid());
        map.put("username", "null");
        map.put("userImageProfile", "defalut");
        map.put("userState", "flase");
        map.put("userPhone", firebaseUser.getPhoneNumber());
        map.put("userGender", "flase");
        map.put("about", "Iam Using HEESApp");
        map.put("LastSeen", "LastSeen");
        reference.setValue(map);

    }


}
