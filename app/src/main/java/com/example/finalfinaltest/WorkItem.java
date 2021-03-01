package com.example.finalfinaltest;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkItem implements Parcelable {
    int resId;
    String workName;
    String bodyPart;

    public WorkItem(int resdId, String workName, String bodyPart) {
        this.resId = resdId;
        this.workName = workName;
        this.bodyPart = bodyPart;
    }
    public WorkItem(Parcel in){
        this.resId = in.readInt();
        this.workName = in.readString();
        this.bodyPart = in.readString();
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.resId);
        parcel.writeString(this.workName);
        parcel.writeString(this.bodyPart);
    }

    public static final Creator CREATOR = new Creator(){

        @Override
        public WorkItem createFromParcel(Parcel parcel) {
            return new WorkItem(parcel);
        }

        @Override
        public WorkItem[] newArray(int i) {
            return new WorkItem[i];
        }
    };


}
