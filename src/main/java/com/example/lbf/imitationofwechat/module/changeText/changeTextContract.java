package com.example.lbf.imitationofwechat.module.changeText;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface changeTextContract {
    interface Presenter extends BasePresenter {
        void saveText(String content1,String content2);
    }

    interface View extends BaseView<Presenter> {
        void setHint(String hint1,String hint2);
        void setContent(String content1,String content2);
        void setTitle(String title);
    }
}
