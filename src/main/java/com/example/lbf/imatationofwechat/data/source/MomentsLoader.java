package com.example.lbf.imatationofwechat.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.lbf.imatationofwechat.beans.MomentBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/30.
 */
public class MomentsLoader extends AsyncTaskLoader<List<MomentBean>> {
    private ChatsRepository mRepository;
    private int page = 0;
    public MomentsLoader(Context context, ChatsRepository repository) {
        super(context);
        mRepository = repository;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage(){
        return page;
    }
    @Override
    public List<MomentBean> loadInBackground() {
        return mRepository.getMomentList(page);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(isStarted()){
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<MomentBean> data) {
        if (isReset()) {
            return;
        }
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
