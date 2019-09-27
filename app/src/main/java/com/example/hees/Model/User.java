package com.example.hees.Model;

public class User {
    private String userid;
    private String username;
    private String userImageProfile;
    private String userState;
    private String about;
    private String userGender;
    private String userPhone;
    private String LastSeen;


    public User() {
    }

    public User(String userid, String username, String userImageProfile, String userState, String about, String userGender, String userPhone, String LastSeen) {
        this.userid = userid;
        this.username = username;
        this.userImageProfile = userImageProfile;
        this.userState = userState;
        this.about = about;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.LastSeen = LastSeen;

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImageProfile() {
        return userImageProfile;
    }

    public void setUserImageProfile(String userImageProfile) {
        this.userImageProfile = userImageProfile;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getLastSeen() {
        return LastSeen;
    }

    public void setLastSeen(String lastSeen) {
        LastSeen = lastSeen;
    }
}
