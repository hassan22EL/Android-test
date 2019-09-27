package com.example.hees.Model;

import android.content.Context;

public class Country {
    private String CountryName;
    private String CountryISO;
    private String CountryCode;
    private int num;
    private int code;
    private int ResId;
    private int prioirty;

    public Country() {
    }

    public Country(Context context, String Format, int num) {
        String data[] = Format.split(",");
        this.CountryName = data[0];
        this.CountryISO = data[1];
        this.code = Integer.parseInt(data[2]);
        this.CountryCode = "+\t\t" + data[2];
        if (data.length > 3) {
            this.prioirty = Integer.parseInt(data[3]);
        }
        String fileName = String.format("f%03d", num);
        this.ResId = context.getApplicationContext().getResources().getIdentifier(fileName, "drawable",
                context.getApplicationContext().getPackageName());

    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getCountryISO() {
        return CountryISO;
    }

    public void setCountryISO(String countryISO) {
        CountryISO = countryISO;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getResId() {
        return ResId;
    }

    public void setResId(int resId) {
        ResId = resId;
    }

    public int getPrioirty() {
        return prioirty;
    }

    public void setPrioirty(int prioirty) {
        this.prioirty = prioirty;
    }
}
