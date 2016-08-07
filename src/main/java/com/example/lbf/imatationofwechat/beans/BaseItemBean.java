package com.example.lbf.imatationofwechat.beans;

/**
 * Created by lbf on 2016/7/7.
 */
public class BaseItemBean {
    String text;
    int image;

    public BaseItemBean(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
