package com.example.lbf.imitationofwechat.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lbf on 2016/6/29.
 */
public class ContactBean implements Parcelable{
    private int id;
//    用户名
    private String account;
//    头像
    private String image;
//    昵称
    private String name;
//    备注，默认与昵称相同
    private String remarks;
    private String sortKey;
    private String userId;
    private String objectId;
    private boolean isMale;
//    个性签名
    private String signature;
//    地区
    private String location;

    private String tag;
//    Friend表中对应的ID
    private String friendId;
    public boolean isMale() {
        return isMale;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ContactBean() {
    }
    public ContactBean(User user) {
        setImage(user.getAvatar());
        setName(user.getName());
        setAccount(user.getUsername());
        setMale(user.getMale());
        setUserId(user.getObjectId());
        setRemarks(user.getName());
        setLocation(user.getRegion());
        setSignature(user.getSignature());
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public ContactBean(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public ContactBean(String username,String image, String name, String sortKey,
                       String userId, boolean isMale,String signature, String location, String tag) {
        this.account = username;
        this.image = image;
        this.name = name;
        this.sortKey = sortKey;
        this.userId = userId;
        this.isMale = isMale;
        this.signature = signature;
        this.location = location;
        this.tag = tag;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    protected ContactBean(Parcel in) {
        id = in.readInt();
        image = in.readString();
        name = in.readString();
        sortKey = in.readString();
        account = in.readString();
        remarks = in.readString();
        userId = in.readString();
        isMale = in.readByte() != 0;
        location = in.readString();
        signature = in.readString();
        friendId = in.readString();
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(sortKey);
        dest.writeString(account);
        dest.writeString(remarks);
        dest.writeString(userId);
        dest.writeByte((byte) (isMale?1:0));
        dest.writeString(location);
        dest.writeString(signature);
        dest.writeString(friendId);


    }
    public void createFromParcel(Parcel parcel){
        id = parcel.readInt();
        image = parcel.readString();
        name = parcel.readString();
        sortKey = parcel.readString();
        account = parcel.readString();
        remarks = parcel.readString();
        userId = parcel.readString();
        isMale = parcel.readByte() != 0;
        location = parcel.readString();
        signature = parcel.readString();
        friendId = parcel.readString();
    }
}
