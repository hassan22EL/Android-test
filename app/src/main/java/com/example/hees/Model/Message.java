package com.example.hees.Model;

public class Message {
    private String Text;
    private String PdF;
    private String File;
    private String Voice;
    private String Video;
    private String Image;
    private String Location;
    private String ContactID;
    //realtion atrubte


    private String Receiverid;
    private String Senderid;
    private String TimeReceive;
    private String TimeRead;
    private String TimeSender;

    private String Read;
    private String Receive;
    private String unReceive;

    public Message(String text, String pdF, String file, String voice,
                   String video, String image, String location, String ContactID,
                   String receiverid, String senderid, String timeReceive, String timeRead,
                   String read, String receive, String unReceive, String TimeSender) {
        this.Text = text;
        this.PdF = pdF;
        this.File = file;
        this.Voice = voice;
        this.Video = video;
        this.Image = image;
        this.Location = location;
        this.ContactID = ContactID;
        this.Receiverid = receiverid;
        this.Senderid = senderid;
        this.TimeReceive = timeReceive;
        this.TimeRead = timeRead;
        this.Read = read;
        this.Receive = receive;
        this.unReceive = unReceive;
        this.TimeSender = TimeSender;

    }

    public Message() {
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getPdF() {
        return PdF;
    }

    public void setPdF(String pdF) {
        PdF = pdF;
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
    }

    public String getVoice() {
        return Voice;
    }

    public void setVoice(String voice) {
        Voice = voice;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return ContactID;
    }

    public void setTime(String ContactID) {
        ContactID = ContactID;
    }

    public String getReceiverid() {
        return Receiverid;
    }

    public void setReceiverid(String receiverid) {
        Receiverid = receiverid;
    }

    public String getSenderid() {
        return Senderid;
    }

    public void setSenderid(String senderid) {
        Senderid = senderid;
    }

    public String getTimeReceive() {
        return TimeReceive;
    }

    public void setTimeReceive(String timeReceive) {
        TimeReceive = timeReceive;
    }

    public String getTimeRead() {
        return TimeRead;
    }

    public void setTimeRead(String timeRead) {
        TimeRead = timeRead;
    }

    public String getTimeSender() {
        return TimeSender;
    }

    public void setTimeSender(String timeSender) {
        TimeSender = timeSender;
    }

    public String getRead() {
        return Read;
    }

    public void setRead(String read) {
        Read = read;
    }

    public String getReceive() {
        return Receive;
    }

    public void setReceive(String receive) {
        Receive = receive;
    }

    public String getUnReceive() {
        return unReceive;
    }

    public void setUnReceive(String unReceive) {
        this.unReceive = unReceive;
    }
}
