package com.example.lbf.imitationofwechat.module.newFriend;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;
import com.example.lbf.imitationofwechat.data.source.local.db.NewFriend;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface NewFriendContract {
    interface Presenter extends BasePresenter {
        void showUserInfo(int position);
        void acceptNewFriend(int position);

    }

    interface View extends BaseView<Presenter> {
        void setUserList(List<NewFriend> userList);
        void toast(String msg);
    }
}
