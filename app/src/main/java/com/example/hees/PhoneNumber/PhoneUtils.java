package com.example.hees.PhoneNumber;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneUtils {


    public static String getCountryRegionFromPhone(Context context) {
        TelephonyManager services = null;
        services = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String code = null;
        if (services != null) {
            code = services.getNetworkCountryIso();
        }
        if (code == null) {
            code = context.getResources().getConfiguration().locale.getCountry();
        }
        if (code == null) {
            code = services.getSimCountryIso();
            Log.e("code", String.valueOf(code));
        }

        if (code != null) {
            return code.toUpperCase();
        }

        return null;
    }


}
