package com.example.lbf.imitationofwechat.module.userInfo;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;
import com.example.lbf.imitationofwechat.beans.ContactBean;

/**
 * Created by lbf on 2016/7/28.
 */
public interface UserInfoContract {
    interface Presenter extends BasePresenter {

        ContactBean getContact();
//        添加好友
        void addFriend();
//        聊天
        void chat();
//        设置备注及标签
        void saveRemarksAndTag(String remarks,String tag);
//        删除好友
        void deleteFriend();
//        设为/取消星标
        void setIsStarred(boolean isStarred);
    }

    interface View extends BaseView<Presenter> {

        void showBtAddFriend();

        void hideBtAddFriend();

        void showBtChat();

        void hideBtChat();

        void hideLayoutTag();

        void setUserInfo(ContactBean contact);

        void toast(String msg);
    }
}
