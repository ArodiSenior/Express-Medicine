package com.techsavanna.medicine.models;

public class ItemModel {
    public String upgitemname;
    public String upitemprice;
    public String upitemdescription;
    public String upcategory;
    public String upimage;
    public String upitemid;

    public ItemModel() {
    }

    public ItemModel(String upgitemname, String upitemprice, String upitemdescription, String upcategory, String upimage, String upitemid) {
        this.upgitemname = upgitemname;
        this.upitemprice = upitemprice;
        this.upitemdescription = upitemdescription;
        this.upcategory = upcategory;
        this.upimage = upimage;
        this.upitemid = upitemid;
    }

    public String getUpgitemname() {
        return upgitemname;
    }

    public void setUpgitemname(String upgitemname) {
        this.upgitemname = upgitemname;
    }

    public String getUpitemprice() {
        return upitemprice;
    }

    public void setUpitemprice(String upitemprice) {
        this.upitemprice = upitemprice;
    }

    public String getUpitemdescription() {
        return upitemdescription;
    }

    public void setUpitemdescription(String upitemdescription) {
        this.upitemdescription = upitemdescription;
    }

    public String getUpcategory() {
        return upcategory;
    }

    public void setUpcategory(String upcategory) {
        this.upcategory = upcategory;
    }

    public String getUpimage() {
        return upimage;
    }

    public void setUpimage(String upimage) {
        this.upimage = upimage;
    }

    public String getUpitemid() {
        return upitemid;
    }

    public void setUpitemid(String upitemid) {
        this.upitemid = upitemid;
    }
}
