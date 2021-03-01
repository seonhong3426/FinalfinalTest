package com.example.finalfinaltest;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordItem implements Parcelable {
    String work;
    String date;
    String title;
    String contents;

    public RecordItem(String work, String date, String title, String contents) {
        this.work = work;
        this.date = date;
        this.title = title;
        this.contents = contents;
    }

    public RecordItem(Parcel parcel) {
        this.work = parcel.readString();
        this.date = parcel.readString();
        this.title = parcel.readString();
        this.contents = parcel.readString();
    }


    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        
        parcel.writeString(this.work);
        parcel.writeString(this.date);
        parcel.writeString(this.title);
        parcel.writeString(this.contents);
    }

    public static final Creator CREATOR = new Creator(){

        @Override
        public RecordItem createFromParcel(Parcel parcel) {
            return new RecordItem(parcel);
        }

        @Override
        public RecordItem[] newArray(int i) {
            return new RecordItem[i];
        }
    };
   
}
