package com.example.lbf.imatationofwechat.beans;

/**
 * Created by lbf on 2016/6/28.
 */
public class MessageBean {
    private boolean isSend;
    private String content;
    private int type;
    private boolean disPlaytime;
    private long time;
    private int id;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MessageBean(int id, String content, boolean isSend, long time, boolean disPlaytime) {
        this.id = id;
        this.content = content;
        this.isSend = isSend;
        this.time = time;
        this.disPlaytime = disPlaytime;
    }

    public MessageBean(String content, boolean isSend, long time, boolean disPlaytime, int type) {
        this.content = content;
        this.isSend = isSend;
        this.time = time;
        this.disPlaytime = disPlaytime;
        this.type = type;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public boolean isDisPlaytime() {
        return disPlaytime;
    }

    public void setDisPlaytime(boolean disPlaytime) {
        this.disPlaytime = disPlaytime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
