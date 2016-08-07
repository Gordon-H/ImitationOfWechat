package com.example.lbf.imatationofwechat.beans;

/**
 * Created by lbf on 2016/7/21.
 */
public class MomentBean {
    private ContactBean contact;
    private String content;
    private String link;
    private String[] images;
    private ContactBean[] favors;
    private CommentBean[] comments;
    private boolean isFavored = false;
    private long time;

    public boolean isFavored() {
        return isFavored;
    }

    public void setFavored(boolean favored) {
        isFavored = favored;
    }

    public MomentBean() {
    }

    public MomentBean(ContactBean contact, String content, String link, String[] images, ContactBean[] favors, CommentBean[] comments, long time) {
        this.contact = contact;
        this.content = content;
        this.link = link;
        this.images = images;
        this.favors = favors;
        this.comments = comments;
        this.time = time;
    }

    public MomentBean(ContactBean contact, long time) {
        this.contact = contact;
        this.time = time;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public ContactBean[] getFavors() {
        return favors;
    }

    public void setFavors(ContactBean[] favors) {
        this.favors = favors;
    }

    public CommentBean[] getComments() {
        return comments;
    }

    public void setComments(CommentBean[] comments) {
        this.comments = comments;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
