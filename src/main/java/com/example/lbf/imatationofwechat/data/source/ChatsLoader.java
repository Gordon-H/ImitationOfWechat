package com.example.lbf.imatationofwechat.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.lbf.imatationofwechat.beans.ChatBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/30.
 */
public class ChatsLoader extends AsyncTaskLoader<List<ChatBean>> {

    private ChatsRepository mRepository;


    public ChatsLoader(Context context, ChatsRepository repository) {
        super(context);
        mRepository = repository;
    }

    @Override
    public List<ChatBean> loadInBackground() {
        return mRepository.getChatsList();
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(isStarted()){
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<ChatBean> data) {
        if (isReset()) {
            return;
        }
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

}
