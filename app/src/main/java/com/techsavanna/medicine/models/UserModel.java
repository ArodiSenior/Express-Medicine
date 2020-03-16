package com.techsavanna.medicine.models;

public class UserModel {
    public String upphone;
    public String upuserid;
    public String upemail;
    public String upimage;

    public UserModel() {
    }

    public UserModel(String upphone, String upuserid, String upemail, String upimage) {
        this.upphone = upphone;
        this.upuserid = upuserid;
        this.upemail = upemail;
        this.upimage = upimage;
    }

    public String getUpphone() {
        return upphone;
    }

    public void setUpphone(String upphone) {
        this.upphone = upphone;
    }

    public String getUpuserid() {
        return upuserid;
    }

    public void setUpuserid(String upuserid) {
        this.upuserid = upuserid;
    }

    public String getUpemail() {
        return upemail;
    }

    public void setUpemail(String upemail) {
        this.upemail = upemail;
    }

    public String getUpimage() {
        return upimage;
    }

    public void setUpimage(String upimage) {
        this.upimage = upimage;
    }
}
