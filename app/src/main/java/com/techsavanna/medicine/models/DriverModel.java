package com.techsavanna.medicine.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverModel implements Parcelable {

    private String email;
    private String lat;
    private String lng;
    private String uid;
   
    public final static Parcelable.Creator<DriverModel> CREATOR = new Creator<DriverModel>() {
        @SuppressWarnings({
                "unchecked"
        })
        public DriverModel createFromParcel(Parcel in) {
            return new DriverModel(in);
        }
        
        public DriverModel[] newArray(int size) {
            return (new DriverModel[size]);
        }
        
    };
    
    public DriverModel(String email, String lat, String lng, String uid) {
        this.email = email;
        this.lat = lat;
        this.lng = lng;
        this.uid = uid;
    }
    
    private DriverModel(Parcel in) {
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.lat = ((String) in.readValue((String.class.getClassLoader())));
        this.lng = ((String) in.readValue((String.class.getClassLoader())));
        this.uid = ((String) in.readValue((String.class.getClassLoader())));
   
    }
    
    public DriverModel() {
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getLat() {
        return lat;
    }
    
    public void setLat(String lat) {
        this.lat = lat;
    }
    
    public String getLng() {
        return lng;
    }
    
    public void setLng(String lng) {
        this.lng = lng;
    }
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(email);
        dest.writeValue(lat);
        dest.writeValue(lng);
        dest.writeValue(uid);
    }
    
    public int describeContents() {
        return 0;
    }
    
}
