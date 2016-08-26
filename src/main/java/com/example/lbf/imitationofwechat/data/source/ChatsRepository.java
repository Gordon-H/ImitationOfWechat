package com.example.lbf.imitationofwechat.data.source;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.lbf.imitationofwechat.Interface.QueryUserListener;
import com.example.lbf.imitationofwechat.Interface.UpdateCacheListener;
import com.example.lbf.imitationofwechat.beans.CommentBean;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.beans.Friend;
import com.example.lbf.imitationofwechat.beans.MessageBean;
import com.example.lbf.imitationofwechat.beans.MomentBean;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.beans.UserInfoBean;
import com.example.lbf.imitationofwechat.data.source.local.ChatsLocalDataSource;
import com.example.lbf.imitationofwechat.data.source.remote.ChatsRemoteDataSource;
import com.example.lbf.imitationofwechat.module.contacts.ContactsFragment;
import com.example.lbf.imitationofwechat.BmobIMApplication;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lbf on 2016/7/30.
 */
public class ChatsRepository implements IDataSource {

    private static ChatsRepository INSTANCE = null;

    private final IRemoteDataSource mRemoteDataSource;

    private final ILocalDataSource mLocalDataSource;

    private ChatsRepository(@NonNull IRemoteDataSource tasksRemoteDataSource,
                            @NonNull ILocalDataSource tasksLocalDataSource) {
        mRemoteDataSource = CommonUtil.checkNotNull(tasksRemoteDataSource);
        mLocalDataSource = CommonUtil.checkNotNull(tasksLocalDataSource);
    }
    public static ChatsRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (ChatsRepository.class) {
                Context context = BmobIMApplication.INSTANCE();
                if (INSTANCE == null) {
                    INSTANCE = new ChatsRepository(ChatsRemoteDataSource.getInstance(context),
                        ChatsLocalDataSource.getInstance(context));
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void saveConversationList(List<BmobIMConversation> conversationList) {

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
    public void updateContact(ContactBean bean) {
        mLocalDataSource.updateContact(bean);
        mRemoteDataSource.updateContact(bean);
    }

    @Override
    public List<BmobIMConversation> getConversationList() {
        return mLocalDataSource.getConversationList();
    }

    @Override
    public List<ContactBean> getContactsList() {
        List<ContactBean> contactList = new ArrayList<>();
        List<ContactBean> localList = mLocalDataSource.getContactsList();
        if(localList==null||localList.size() == 0){
//            首先在本地DB中查找，如果为null，说明首次启动，需要向服务器获取过数据
//            并且把获取的数据存储在本地DB
            List<ContactBean> remoteList = mRemoteDataSource.getContactsList();
            if(remoteList!=null){
                contactList.addAll(remoteList);
            }
            saveContactList(contactList);
        } else{
//            如果不为空直接添加即可
            contactList.addAll(localList);
        }
        for(int i = 0; i< ContactsFragment.DEFAULT_ITEM_NAME.length; i++){
//            添加默认的几项
            ContactBean bean = new ContactBean(ContactsFragment.DEFAULT_ITEM_IMAGE[i],
                    ContactsFragment.DEFAULT_ITEM_NAME[i]);
            contactList.add(i,bean);
        }
//        添加最后一项用于显示联系人数目
        contactList.add(new ContactBean());
        return contactList;
    }

    @Override
    public void saveContactList(List<ContactBean> contactList) {
        mLocalDataSource.saveContactList(contactList);
    }

    @Override
    public List<ContactBean> getAccountsList() {
        return mLocalDataSource.getAccountsList();
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

    @Override
    public void login(String username, String password, LogInListener listener) {
        mRemoteDataSource.login(username,password,listener);
    }

    @Override
    public void logout() {
        mRemoteDataSource.logout();
    }

    @Override
    public User getCurrentUser() {
        return mLocalDataSource.getCurrentUser();
    }

    @Override
    public void register(String username,String name, String password, String pwdagain, LogInListener listener) {
        mRemoteDataSource.register(username,name, password,pwdagain,listener);
    }

    @Override
    public void queryUsers(String username, int limit, FindListener<User> listener) {
        mRemoteDataSource.queryUsers(username,limit,listener);
    }

    @Override
    public void queryUserInfo(String objectId, QueryUserListener listener) {
        mRemoteDataSource.queryUserInfo(objectId,listener);
    }

    @Override
    public void updateUserInfo(MessageEvent event, UpdateCacheListener listener) {
        mLocalDataSource.updateUserInfo(event,listener);
    }

    @Override
    public void addContact(final ContactBean contact, final SaveListener listener ) {
        final Friend friend = new Friend();
        mRemoteDataSource.addContact(friend, contact, new SaveListener() {
            @Override
            public void onSuccess() {
                contact.setFriendId(friend.getObjectId());
                mLocalDataSource.addContact(contact);
                if(listener!=null){
                    listener.onSuccess();
                }
                LogUtil.i("add successfully");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("add onFailure!");
                if(listener!=null){
                    listener.onFailure(i,s);

                }
            }
        });
    }

    @Override
    public void deleteContact(final ContactBean contact, final DeleteListener listener) {
        mRemoteDataSource.deleteContact(contact, new DeleteListener() {
            @Override
            public void onSuccess() {
                mLocalDataSource.deleteContact(contact);
                LogUtil.i("delete successfully");
                listener.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("delete failed!"+s);
                listener.onFailure(i,s);
            }
        });

    }

}
