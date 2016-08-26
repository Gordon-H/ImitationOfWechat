package com.example.lbf.imitationofwechat.beans;

import com.example.lbf.imitationofwechat.data.source.local.db.NewFriend;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
//    头像
    private String avatar;
//    昵称
    private String name;
//    性别
    private Boolean isMale = true;
//    地区
    private String region;
//    签名
    private String signature;

    public User(){}

    public User(ContactBean contactBean){
        setName(contactBean.getName());
        setAvatar(contactBean.getImage());
        setMale(contactBean.isMale());
        setUsername(contactBean.getAccount());
        setObjectId(contactBean.getUserId());
        setRegion(contactBean.getLocation());
        setSignature(contactBean.getSignature());
    }
    public User(NewFriend friend){
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
