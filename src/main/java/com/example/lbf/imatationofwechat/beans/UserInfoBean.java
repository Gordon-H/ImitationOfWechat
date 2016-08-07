package com.example.lbf.imatationofwechat.beans;

/**
 * Created by lbf on 2016/8/3.
 */
public class UserInfoBean {
    private String account;
    private String name;
    private int image;
    private int qrCodeImage;
    private int gender;
    private String region;
    private String signature;

    public UserInfoBean(String account, String name, int image, int qrCodeImage, int gender, String region, String signature) {
        this.account = account;
        this.name = name;
        this.image = image;
        this.qrCodeImage = qrCodeImage;
        this.gender = gender;
        this.region = region;
        this.signature = signature;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(int qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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
