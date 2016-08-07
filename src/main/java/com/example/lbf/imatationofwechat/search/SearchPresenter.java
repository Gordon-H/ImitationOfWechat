package com.example.lbf.imatationofwechat.search;

import android.content.Context;

import com.example.lbf.imatationofwechat.util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class SearchPresenter implements SearchContract.Presenter {
    private Context mContext;
    private SearchContract.View mView;

    public SearchPresenter(Context context, SearchContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }
}
