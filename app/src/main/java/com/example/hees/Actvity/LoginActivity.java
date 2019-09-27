package com.example.hees.Actvity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hees.Model.Country;
import com.example.hees.PhoneNumber.PhoneUtils;
import com.example.hees.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import static android.content.DialogInterface.OnClickListener;

public class LoginActivity extends AppCompatActivity {
    private Button btn_countryList;
    private EditText CountryCode_text;
    private EditText PhoneNumber_text;


    String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private Button verifiy;
    private HashMap<Integer, Country> map = new HashMap<>();
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_actvity);
        btn_countryList = (Button) findViewById(R.id.country_list);
        CountryCode_text = (EditText) findViewById(R.id.Country_code);
        PhoneNumber_text = (EditText) findViewById(R.id.Phone_Number);
        verifiy = (Button) findViewById(R.id.btn_verification);
        btn_countryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CountryActivity.class);
                startActivity(intent);
            }
        });

        verifiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean vaild = ValidationNumber();
                if (vaild) {
                    DialogInterface();
                    Log.e("ok", "ok");
                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        Intent intent = getIntent();
        boolean a = intent.getBooleanExtra("action", false);
        Log.e("select", String.valueOf(a));
        if (a) {
            ActvityChangeData(intent);
        } else {
            StartActivityOne();
        }
        super.onStart();
    }

    private void StartActivityOne() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("countries.dat"), "UTF-8"));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                Country c = new Country(LoginActivity.this, line, i);
                map.put(c.getCode(), c);
                i++;
            }
            String CountryRegion = PhoneUtils.getCountryRegionFromPhone(LoginActivity.this);
            phoneNumberUtil = PhoneNumberUtil.getInstance();
            int code = phoneNumberUtil.getCountryCodeForRegion(CountryRegion);
            Country U = map.get(code);
            String Countryname = U.getCountryName();
            btn_countryList.setText(Countryname);
            int CountryRes = U.getResId();
            Drawable d = getResources().getDrawable(CountryRes);
            d.setBounds(0, 0, 40, 40);
            Drawable dropdown = getResources().getDrawable(R.drawable.edit_box_drop_sel_white);
            dropdown.setBounds(0, 0, 120, 100);
            btn_countryList.setCompoundDrawables(d, null, dropdown, null);
            String CountryCode = U.getCountryCode();
            CountryCode_text.getText().clear();
            CountryCode_text.setText(CountryCode);

        } catch (IOException e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    private void ActvityChangeData(Intent intent) {
        String CountryCode = intent.getStringExtra("CountryCode");
        CountryCode_text.getText().clear();
        CountryCode_text.setText(CountryCode);
        String CountryName = intent.getStringExtra("Countryname");
        btn_countryList.setText(CountryName);
        int Res = intent.getIntExtra("CountryRes", R.drawable.f230);
        Drawable res = getResources().getDrawable(Res);
        res.setBounds(0, 0, 40, 40);
        Drawable dropdown = getResources().getDrawable(R.drawable.edit_box_drop_sel_white);
        dropdown.setBounds(0, 0, 120, 100);
        btn_countryList.setCompoundDrawables(res, null, dropdown, null);
    }

    private boolean ValidationNumber() {
        boolean validtion = false;
        String Number = PhoneNumber_text.getText().toString();
        String Code = CountryCode_text.getText().toString().replaceAll("\\s", "");
        if (TextUtils.isEmpty(Number) && TextUtils.isEmpty(Code)) {
            Toast.makeText(LoginActivity.this, "Please enter aphone number", Toast.LENGTH_LONG).show();
            validtion = false;
        } else {
            String mPhoneNumber = Code + Number;
            Log.e("Number", mPhoneNumber);
            String Phone = valid(mPhoneNumber);
            if (Phone == null) {
                Toast.makeText(LoginActivity.this, "Please enter a valid number", Toast.LENGTH_LONG).show();
                validtion = false;
            } else {
                Log.e("PhoneNumber", Phone);
                validtion = true;
            }
        }
        return validtion;
    }

    private void DialogInterface() {
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ad.create();
        View dialog = inflater.inflate(R.layout.dialog, null);
        ad.setView(dialog);
        TextView textView = (TextView) dialog.findViewById(R.id.dialog_text);

        ad.setNegativeButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String Number = PhoneNumber_text.getText().toString();
                String Code = CountryCode_text.getText().toString().replaceAll("\\s", "");
                String mPhoneNumber = Code + Number;
                String Phone = valid(mPhoneNumber);
                if (Phone != null) {
                    Intent intent = new Intent(LoginActivity.this, VerificactionActivity.class);
                    intent.putExtra("Phone", Phone);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Phone a null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final AlertDialog alertDialog = ad.show();
        String FullText = textView.getText().toString();
        if (FullText.contains("Worng")) {
            setClickableText(textView, "Worng", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();

                }
            });
        } else {
            Log.e("Error", "Not Found");
        }

    }

    private String valid(String NumberPhone) {
        String Phone = null;
        String Region = null;
        try {
            if (NumberPhone != null) {

                Phonenumber.PhoneNumber p = phoneNumberUtil.parse(NumberPhone, null);
                StringBuilder sb = new StringBuilder(16);
                sb.append("+").append(p.getCountryCode()).append(p.getNationalNumber());
                Phone = sb.toString();
                Region = phoneNumberUtil.getRegionCodeForNumber(p);
                Log.e("Region", Region);
                Log.e("Phone", Phone);
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        if (Region != null) {
            return Phone;
        } else {
            return null;
        }
    }

    private void setClickableText(final TextView tv, String textToHighlight, final View.OnClickListener onClickListener) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
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
}



