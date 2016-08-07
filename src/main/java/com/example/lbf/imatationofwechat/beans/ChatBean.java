package com.example.lbf.imatationofwechat.beans;

/**
 * Created by lbf on 2016/6/28.
 */
public class ChatBean {
    private String name;
    private String content;
    private long time;
    private int image;
    private int type;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //    是否置顶,默认为false
    private boolean isOnTop = false;

    public boolean isOnTop() {
        return isOnTop;
    }

    public void setOnTop(boolean onTop) {
        isOnTop = onTop;
    }

    public ChatBean(String name, String content, long time, int image) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.image = image;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatBean(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
