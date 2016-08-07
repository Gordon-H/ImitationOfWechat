package com.example.lbf.imatationofwechat.me;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface MeContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
//        点击 item后展示内容
        void showItem(android.view.View v);

    }
}
