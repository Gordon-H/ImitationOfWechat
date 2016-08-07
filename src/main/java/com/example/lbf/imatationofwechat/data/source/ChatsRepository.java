package com.example.lbf.imatationofwechat.data.source;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.lbf.imatationofwechat.beans.CommentBean;
import com.example.lbf.imatationofwechat.beans.MomentBean;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.beans.MessageBean;
import com.example.lbf.imatationofwechat.beans.ChatBean;
import com.example.lbf.imatationofwechat.beans.ContactBean;
import com.example.lbf.imatationofwechat.beans.UserInfoBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/30.
 */
public class ChatsRepository implements ChatsDataSource {

    private static ChatsRepository INSTANCE = null;

    private final ChatsDataSource mRemoteDataSource;

    private final ChatsDataSource mLocalDataSource;

    private ChatsRepository(@NonNull ChatsDataSource tasksRemoteDataSource,
                            @NonNull ChatsDataSource tasksLocalDataSource) {
//        mRemoteDataSource = CommonUtil.checkNotNull(tasksRemoteDataSource);
        mRemoteDataSource = tasksRemoteDataSource;
        mLocalDataSource = CommonUtil.checkNotNull(tasksLocalDataSource);
    }
    public static ChatsRepository getInstance(ChatsDataSource tasksRemoteDataSource,
                                              ChatsDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ChatsRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void saveChatsList(List<ChatBean> chatBeanList, int onTopNumber) {
        mLocalDataSource.saveChatsList(chatBeanList, onTopNumber);
    }

    @Override
    public void deleteAccount(int accountId) {
        mLocalDataSource.deleteAccount(accountId);
    }

    @Override
    public void addAccount(ContactBean bean) {
        mLocalDataSource.addAccount(bean);
    }

    @Override
    public void deleteContact(int contactId) {
        mLocalDataSource.deleteContact(contactId);
    }

    @Override
    public void addContact(ContactBean bean) {
        mLocalDataSource.addContact(bean);
    }

    @Override
    public List<ChatBean> getChatsList() {
        return mLocalDataSource.getChatsList();
    }

    @Override
    public List<ContactBean> getContactsList() {
        return mLocalDataSource.getContactsList();
    }

    @Override
    public List<ContactBean> getAccountsList() {
        return mLocalDataSource.getAccountsList();
    }

    @Override
    public Cursor getContactsCursor() {
        return mLocalDataSource.getContactsCursor();
    }

    @Override
    public Cursor getAccountsCursor() {
        return mLocalDataSource.getAccountsCursor();
    }

    @Override
    public ContactBean getContactInfo(int contactId) {
        return mLocalDataSource.getContactInfo(contactId);
    }

    @Override
    public List<MessageBean> getMessageList(int contactId, int page) {
        return mLocalDataSource.getMessageList(contactId,page);
    }

    @Override
    public void saveMessageList(List<MessageBean> beanList, int contactId) {
        mLocalDataSource.saveMessageList(beanList, contactId);
    }

    @Override
    public List<MomentBean> getMomentList(int page) {
        return mLocalDataSource.getMomentList(page);
    }

    @Override
    public ContactBean[] getFavors(int id) {
        return mLocalDataSource.getFavors(id);
    }

    @Override
    public CommentBean[] getComments(int id) {
        return mLocalDataSource.getComments(id);
    }

    @Override
    public UserInfoBean getUserInfo() {
        return mLocalDataSource.getUserInfo();
    }

}
