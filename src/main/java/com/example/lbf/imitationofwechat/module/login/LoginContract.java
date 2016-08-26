package com.example.lbf.imitationofwechat.module.login;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface LoginContract {
    interface Presenter extends BasePresenter {
        void login(String account, String password);

    }

    interface View extends BaseView<Presenter> {
    }
}
