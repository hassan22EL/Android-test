package com.example.hees.Model;

public class Contact {
    private String userid;
    private String id;
    private String contactname;
    private String contactphone;

    public Contact(String userid, String id, String contactname, String contactphone) {
        this.userid = userid;
        this.id = id;
        this.contactname = contactname;
        this.contactphone = contactphone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public Contact() {

    }
}
