package com.example.lbf.imitationofwechat.module.me;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface MeContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
//        点击 item后展示内容
        void showItem(android.view.View v);
        void setUserInfo(String avatar, String name, String username);
    }
}
