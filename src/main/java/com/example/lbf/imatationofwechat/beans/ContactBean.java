package com.example.lbf.imatationofwechat.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lbf on 2016/6/29.
 */
public class ContactBean implements Parcelable{
    private int id;
    private int image;
    private String name;
    private String sortKey = "$";

    public ContactBean(int id, int image, String name, String sortKey) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.sortKey = sortKey;
    }

    public ContactBean() {
    }

    public ContactBean(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public ContactBean(int image, String name, String sortKey) {
        this.image = image;
        this.name = name;
        this.sortKey = sortKey;
    }

    protected ContactBean(Parcel in) {
        id = in.readInt();
        image = in.readInt();
        name = in.readString();
        sortKey = in.readString();
    }

    public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel in) {
            return new ContactBean(in);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(image);
        dest.writeString(name);
        dest.writeString(sortKey);
    }
    public void createFromParcel(Parcel parcel){
        id = parcel.readInt();
        image = parcel.readInt();
        name = parcel.readString();
        sortKey = parcel.readString();
    }
}
