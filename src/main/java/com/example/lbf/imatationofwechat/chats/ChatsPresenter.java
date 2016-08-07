package com.example.lbf.imatationofwechat.chats;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.beans.ChatBean;
import com.example.lbf.imatationofwechat.data.source.ChatsLoader;
import com.example.lbf.imatationofwechat.data.source.ChatsRepository;

import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChatsPresenter implements ChatsContract.Presenter,LoaderManager.LoaderCallbacks<List<ChatBean>> {

    private static final int LOAD_CHATS = 1;

    private ChatsContract.View mView;
    private ChatsRepository mRepository;
    private final LoaderManager mLoaderManager;
    private final ChatsLoader mLoader;

    public ChatsPresenter(LoaderManager loaderManager,ChatsLoader loader, ChatsContract.View view, ChatsRepository repository) {
        mLoaderManager = CommonUtil.checkNotNull(loaderManager, "loader manager cannot be null!");
        mLoader = CommonUtil.checkNotNull(loader, "loader cannot be null!");
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mRepository =  CommonUtil.checkNotNull(repository, "repository cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void saveChatsList(List<ChatBean> chatBeanList, int onTopNumber) {
        mRepository.saveChatsList(chatBeanList, onTopNumber);
    }

    @Override
    public void deleteAccount(int accountId) {
        mRepository.deleteAccount(accountId);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(LOAD_CHATS,null,this);
    }

    @Override
    public Loader<List<ChatBean>> onCreateLoader(int id, Bundle args) {
        mView.showLoading();
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<ChatBean>> loader, List<ChatBean> chatBeanList) {
        LogUtil.i("onLoadFinished");
        mView.setChatsList(chatBeanList);
        mView.hideLoading();
    }

    @Override
    public void onLoaderReset(Loader<List<ChatBean>> loader) {
        LogUtil.i("onLoaderReset");
    }

}
