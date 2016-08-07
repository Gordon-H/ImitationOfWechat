package com.example.lbf.imatationofwechat.main;

import android.support.v4.app.Fragment;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface MainContract {
    interface Presenter extends BasePresenter {
        Fragment getFragmentForPage(int page);
    }

    interface View extends BaseView<Presenter> {
        void changePage(int page);
    }
}
