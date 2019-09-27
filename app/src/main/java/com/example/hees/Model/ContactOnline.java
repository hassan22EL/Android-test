package com.example.hees.Model;

public class ContactOnline {
    private String ID;
    private String userid;
    private String LastSeen;

    public ContactOnline(String ID, String userid, String lastSeen) {
        this.ID = ID;
        this.userid = userid;
        LastSeen = lastSeen;
    }

    public ContactOnline() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastSeen() {
        return LastSeen;
    }

    public void setLastSeen(String lastSeen) {
        LastSeen = lastSeen;
    }
}
