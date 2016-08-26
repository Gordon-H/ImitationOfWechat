package com.example.lbf.imitationofwechat.module.search;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface SearchContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
    }
}
