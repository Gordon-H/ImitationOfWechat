package com.example.lbf.imitationofwechat.module.chats;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.lbf.imitationofwechat.beans.Conversation;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.loader.ChatsLoader;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import java.util.List;

import cn.bmob.newim.BmobIM;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChatsPresenter implements ChatsContract.Presenter,LoaderManager.LoaderCallbacks<List<Conversation>> {

    private static final int LOAD_CHATS = 1;

    private Context mContext;
    private ChatsContract.View mView;
    private ChatsRepository mRepository;
    private final LoaderManager mLoaderManager;

    private List<Conversation> conversationList;

    public ChatsPresenter(Context context,LoaderManager loaderManager, ChatsContract.View view, ChatsRepository repository) {
        mContext = context;
        mLoaderManager = CommonUtil.checkNotNull(loaderManager, "loader manager cannot be null!");
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mRepository =  CommonUtil.checkNotNull(repository, "repository cannot be null!");
        mView.setPresenter(this);
    }


    @Override
    public void start() {
        mLoaderManager.initLoader(LOAD_CHATS,null,this);
    }

    @Override
    public void restartLoader() {
        mLoaderManager.restartLoader(LOAD_CHATS,null,this);
    }

    @Override
    public Loader<List<Conversation>> onCreateLoader(int id, Bundle args) {
        mView.showLoading();
        LogUtil.i("onCreateLoader");
        return new ChatsLoader(mContext,mRepository);
    }

    @Override
    public void onLoadFinished(Loader<List<Conversation>> loader, List<Conversation> conversationList) {
        this.conversationList = conversationList;
        Log.i("info", "onLoadFinished() called with: " +
                "loader = [" + loader + "], conversationList = [" + conversationList + "]");
        mView.setChatsList(conversationList);
        mView.hideLoading();
    }

    @Override
    public void onLoaderReset(Loader<List<Conversation>> loader) {
        LogUtil.i("onLoaderReset");
    }

    @Override
    public void deleteChats(int position) {
        BmobIM.getInstance().deleteConversation(conversationList.get(position).getcId());
        conversationList.remove(position);
        mView.updateList();
    }

    @Override
    public void setOnTop(int position) {
        Conversation c = conversationList.get(position);
        conversationList.remove(position);
        conversationList.add(0,c);
        c.setOnTop(true);
        c.setOrder(0);
        for(int i = 1;i<conversationList.size();i++){
            Conversation c1 = conversationList.get(i);
            if(c1.isOnTop()){
                c1.increaseOrder();
            }else{
                break;
            }
        }
        mView.updateList();
    }

    @Override
    public void unsetOnTop(int position) {
        Conversation c = conversationList.get(position);
        conversationList.remove(position);
        int i;
        for( i = position; i<conversationList.size();i++){
            Conversation c1 = conversationList.get(position);
            if(c1.isOnTop()){
                c1.decreaseOrder();
            }else if(!c1.isOnTop()&&c1.getLastMessageTime() < c.getLastMessageTime()){
                conversationList.add(i,c);
                c.setOnTop(false);
                break;
            }
        }
        if(i == conversationList.size()){
            conversationList.add(c);
        }
        mView.updateList();
    }


}
