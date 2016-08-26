package com.example.lbf.imitationofwechat.module.personalInfo;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface PersonalInfoContract {
    interface Presenter extends BasePresenter {
        void saveAvatar(String imageUrl);
        void saveName(String name);
        void saveSignature(String signature);
        void saveGender(boolean isMale);
        void saveLocation(String location);
        void stop();
    }

    interface View extends BaseView<Presenter> {
        void showItem(android.view.View v);
        void setUserInfo(String avatar, String name, String username, boolean isMale, String location, String signature);
    }
}
