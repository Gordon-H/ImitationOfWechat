package com.example.lbf.imatationofwechat.shakeSetting;

import android.content.Intent;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ShakeSettingContract {
    interface Presenter extends BasePresenter {
        void saveBackgroundImage(String imagePath);
        void pause();
        void result(int requestCode, int resultCode, Intent result);
    }

    interface View extends BaseView<Presenter> {
        void setUseSoundSelected(boolean isSelected);
        boolean isUseSoundSelected();

    }
}
