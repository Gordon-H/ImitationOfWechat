package com.example.lbf.imatationofwechat.shake;

import android.graphics.Bitmap;
import android.view.animation.Animation;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ShakeContract {
    interface Presenter extends BasePresenter {
        void setupAnimation();
        void create();
        void resume();
        void pause();
    }

    interface View extends BaseView<Presenter> {
        void changeSelection(int position);
        void setHiddenBackground(Bitmap background);
        void setHiddenBackground(int background);
        void startAnimation(Animation animationUp, Animation animationDown);
    }
}
