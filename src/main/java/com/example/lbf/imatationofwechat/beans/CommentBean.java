package com.example.lbf.imatationofwechat.beans;

/**
 * Created by lbf on 2016/7/21.
 */
public class CommentBean {
    ContactBean from;
    ContactBean to;
    String content;

    public CommentBean(String content, ContactBean from, ContactBean to) {
        this.content = content;
        this.from = from;
        this.to = to;
    }

    public ContactBean getFrom() {
        return from;
    }

    public void setFrom(ContactBean from) {
        this.from = from;
    }

    public ContactBean getTo() {
        return to;
    }

    public void setTo(ContactBean to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
