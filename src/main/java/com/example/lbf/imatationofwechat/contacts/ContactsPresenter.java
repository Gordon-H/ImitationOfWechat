package com.example.lbf.imatationofwechat.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;

import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.beans.ContactBean;
import com.example.lbf.imatationofwechat.data.source.ChatsRepository;
import com.example.lbf.imatationofwechat.data.source.ContactsLoader;
import com.example.lbf.imatationofwechat.data.source.local.WeChatPersistenceContract;

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
    private SectionIndexer indexer;

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
    public void onLoadFinished(Loader<List<ContactBean>> loader, List<ContactBean> chatBeanList) {
        final String alphebat = "*ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
        Cursor cursor = mRepository.getContactsCursor();
        indexer = new AlphabetIndexer(cursor,cursor.getColumnIndex(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY),alphebat);
        mView.setIndexer(indexer);
        mView.setContactsList(chatBeanList);
        mView.hideLoading();
    }

    @Override
    public void onLoaderReset(Loader<List<ContactBean>> loader) {

    }

    @Override
    public void touchIndicator(int section) {
        if(section == 0) {
            mView.moveToPosition(0);
        }else{
            int position = indexer.getPositionForSection(section-1);
            mView.moveToPosition(ContactsFragment.DEFAULT_ITEM_IMAGE.length+position);
        }
        mView.showSectionTv(section);
    }

    @Override
    public void unTouchIndicator() {
        mView.hideSectionTv();
    }
}
