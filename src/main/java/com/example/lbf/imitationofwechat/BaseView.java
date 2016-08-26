package com.example.lbf.imitationofwechat;

/**
 * Created by lbf on 2016/7/28.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
    void showLoading();
    void showError(String errorMsg);
    void hideLoading();
}
