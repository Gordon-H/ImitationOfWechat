package com.example.lbf.imitationofwechat.data.source;

import com.example.lbf.imitationofwechat.Interface.UpdateCacheListener;
import com.example.lbf.imitationofwechat.beans.CommentBean;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.beans.MessageBean;
import com.example.lbf.imitationofwechat.beans.MomentBean;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.beans.UserInfoBean;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;

/**
 * Created by lbf on 2016/7/30.
 */
public interface ILocalDataSource {
//    保存聊天列表
    void saveConversationList(List<BmobIMConversation> conversationList);
//    删除公众号
    void deleteAccount(int accountId);
//    添加公众号
    void addAccount(ContactBean bean);
//    删除联系人
    void deleteContact(ContactBean contact);
//    添加联系人
    void addContact(ContactBean contact);
    //    保存联系人信息
    void updateContact(ContactBean bean);
//    获取聊天列表
    List<BmobIMConversation> getConversationList();
//    获取联系人列表
    List<ContactBean> getContactsList();

    void saveContactList(List<ContactBean> contactList);
//    获取公众号列表
    List<ContactBean> getAccountsList();

//    获取联系人信息
    ContactBean getContactInfo(int contactId);
//    获取消息列表
    List<MessageBean> getMessageList(int contactId, int page);

    void saveMessageList(List<MessageBean> beanList, int contactId);

    List<MomentBean> getMomentList(int page);

    ContactBean[] getFavors(int id);

    CommentBean[] getComments(int id);

//    获取用户信息
    UserInfoBean getUserInfo();

    void updateUserInfo(MessageEvent event, final UpdateCacheListener listener);

    User getCurrentUser();

}
