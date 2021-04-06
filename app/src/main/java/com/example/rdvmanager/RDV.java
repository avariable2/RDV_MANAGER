package com.example.rdvmanager;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class RDV implements Parcelable {
    long id;
    String title;
    String date;
    String time;
    String contact;
    int state;

    public RDV() {
    }
    public RDV(String title, String date, String time,
                  String contact, int state) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.contact = contact;
        this.state = 0;
    }
    public RDV(long id, String title, String date, String time,
               String contact, int state) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.contact = contact;
        this.state = 0;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(contact);
        dest.writeInt(state);
    }

    public static final Parcelable.Creator<RDV> CREATOR = new Parcelable.Creator<RDV>(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public RDV createFromParcel(Parcel parcel) {
            return new RDV(parcel);
        }
        @Override
        public RDV[] newArray(int size) {
            return new RDV[size];
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public RDV(Parcel parcel){
        id=parcel.readLong();
        title=parcel.readString();
        date=parcel.readString();
        time=parcel.readString();
        contact=parcel.readString();
        state=parcel.readInt();
    }
}
