package com.example.lbf.imatationofwechat.beans;

/**
 * Created by lbf on 2016/7/7.
 */
public class ImageFolderBean {
    String path;
    String firstImageName;
    int imageCount;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFirstImageName() {
        return firstImageName;
    }

    public void setFirstImageName(String firstImageName) {
        this.firstImageName = firstImageName;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public ImageFolderBean(String path, String firstImageName, int imageCount) {
        this.path = path;
        this.firstImageName = firstImageName;
        this.imageCount = imageCount;
    }
}
