package com.example.lbf.imatationofwechat.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.lbf.imatationofwechat.beans.MessageBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/30.
 */
public class MessagesLoader extends AsyncTaskLoader<List<MessageBean>> {
    private ChatsRepository mRepository;
    private int page = 0;
    private int contactId;
    public MessagesLoader(Context context, ChatsRepository repository,int contactId) {
        super(context);
        mRepository = repository;
        this.contactId = contactId;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    @Override
    public List<MessageBean> loadInBackground() {
        return mRepository.getMessageList(contactId,page);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(isStarted()){
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<MessageBean> data) {
        if (isReset()) {
            return;
        }
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
