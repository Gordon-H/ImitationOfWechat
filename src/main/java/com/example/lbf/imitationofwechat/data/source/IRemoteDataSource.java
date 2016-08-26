package com.example.lbf.imitationofwechat.data.source;

import com.example.lbf.imitationofwechat.Interface.QueryUserListener;
import com.example.lbf.imitationofwechat.beans.CommentBean;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.beans.Friend;
import com.example.lbf.imitationofwechat.beans.MomentBean;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.beans.UserInfoBean;

import java.util.List;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lbf on 2016/7/30.
 */
public interface IRemoteDataSource {
//    删除公众号
    void deleteAccount(int accountId);
//    添加公众号
    void addAccount(ContactBean bean);
//    删除联系人
    void deleteContact(String userId);
//    添加联系人
    void addContact(ContactBean bean);
    //    保存联系人信息
    void updateContact(ContactBean bean);
//    获取联系人列表
    List<ContactBean> getContactsList();
//    获取公众号列表
    List<ContactBean> getAccountsList();
//    获取联系人信息
    ContactBean getContactInfo(int contactId);

    List<MomentBean> getMomentList(int page);

    ContactBean[] getFavors(int id);

    CommentBean[] getComments(int id);

//    获取用户信息
    UserInfoBean getUserInfo();

    void login(String username, String password, final LogInListener listener);

    void logout();

    void register(String username,String name, String password, String pwdagain, final LogInListener listener);

    void queryUsers(String username,int limit,final FindListener<User> listener);

    void queryUserInfo(String objectId, final QueryUserListener listener);

    void addContact(Friend friend, ContactBean contact, SaveListener listener);

    void deleteContact(ContactBean f, DeleteListener listener);
}
