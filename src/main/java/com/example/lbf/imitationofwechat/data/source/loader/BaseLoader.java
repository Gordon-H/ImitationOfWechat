package com.example.lbf.imitationofwechat.data.source.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.lbf.imitationofwechat.data.source.ChatsRepository;

/**
 * Created by lbf on 2016/8/15.
 */
public abstract class BaseLoader<T> extends AsyncTaskLoader<T>{

    protected Context mContext;
    protected ChatsRepository mRepository;

    public BaseLoader(Context context) {
        super(context);
        mContext = context;
    }

    public BaseLoader(Context context, ChatsRepository repository ) {
        super(context);
        mContext = context;
        mRepository = repository;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(isStarted()){
            forceLoad();
        }
    }

    @Override
    public void deliverResult(T data) {
        if (isReset()) {
            return;
        }
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
