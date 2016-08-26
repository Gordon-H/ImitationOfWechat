package com.example.lbf.imitationofwechat.module.contacts;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.loader.ContactsLoader;
import com.example.lbf.imitationofwechat.util.CommonUtil;

import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ContactsPresenter implements ContactsContract.Presenter,LoaderManager.LoaderCallbacks<List<ContactBean>> {

    private static final int LOAD_CONTACTS = 2;

    private ContactsContract.View mView;
    private ChatsRepository mRepository;
    private final LoaderManager mLoaderManager;
    private final ContactsLoader mLoader;

    public ContactsPresenter(LoaderManager loaderManager, ContactsLoader loader, ContactsContract.View view, ChatsRepository repository) {
        mLoaderManager = CommonUtil.checkNotNull(loaderManager, "loader manager cannot be null!");
        mLoader = CommonUtil.checkNotNull(loader, "loader cannot be null!");
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mRepository =  CommonUtil.checkNotNull(repository, "repository cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(LOAD_CONTACTS,null,this);
    }

    @Override
    public Loader<List<ContactBean>> onCreateLoader(int id, Bundle args) {
        mView.showLoading();
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<ContactBean>> loader, List<ContactBean> contactList) {
        mView.setContactsList(contactList);
        mView.hideLoading();
    }

    @Override
    public void onLoaderReset(Loader<List<ContactBean>> loader) {

    }

    @Override
    public void touchIndicator(int group) {
        mView.moveToGroup(group);
        mView.showSectionTv(group);
    }

    @Override
    public void unTouchIndicator() {
        mView.hideSectionTv();
    }
}
