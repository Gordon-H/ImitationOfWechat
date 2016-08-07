package com.example.lbf.imatationofwechat.discover;

import com.example.lbf.imatationofwechat.util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class DiscoverPresenter implements DiscoverContract.Presenter {

    private DiscoverContract.View mView;

    public DiscoverPresenter( DiscoverContract.View view) {
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
