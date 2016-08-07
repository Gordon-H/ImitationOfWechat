package com.example.lbf.imatationofwechat.beans;

/**
 * Created by lbf on 2016/6/29.
 */
public class DiscoverBean {
    public static final int TYPE_NEWS_IMAGE = 1;
    public static final int TYPE_NORMAL = 2;
    public static final int TYPE_NEWS_TEXT_IMAGE = 3;
    public DiscoverBean(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public DiscoverBean(String name, int image, int type) {
        this.name = name;
        this.image = image;
        this.type = type;
    }

    public DiscoverBean(int image, String name, boolean hasHeader, int type) {
        this.image = image;
        this.name = name;
        this.hasHeader = hasHeader;
        this.type = type;
    }

    private int image;
    private String name;
    private boolean hasHeader = false;
    private int type = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DiscoverBean(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
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

}
