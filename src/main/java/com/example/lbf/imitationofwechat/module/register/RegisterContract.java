package com.example.lbf.imitationofwechat.module.register;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface RegisterContract {
    interface Presenter extends BasePresenter {
        void register(String username,String name, String password, String passwordAgain);

    }

    interface View extends BaseView<Presenter> {
        void clearPassword();
    }
}
