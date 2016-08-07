package com.example.lbf.imatationofwechat.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.lbf.imatationofwechat.beans.ContactBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/30.
 */
public class AccountsLoader extends AsyncTaskLoader<List<ContactBean>> {
    private ChatsRepository mRepository;

    public AccountsLoader(Context context, ChatsRepository repository) {
        super(context);
        mRepository = repository;
    }

    @Override
    public List<ContactBean> loadInBackground() {
        return mRepository.getAccountsList();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(isStarted()){
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<ContactBean> data) {
        if (isReset()) {
            return;
        }
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
