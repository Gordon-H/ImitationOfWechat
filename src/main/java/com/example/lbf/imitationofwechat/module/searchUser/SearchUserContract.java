package com.example.lbf.imitationofwechat.module.searchUser;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;
import com.example.lbf.imitationofwechat.beans.User;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface SearchUserContract {
    interface Presenter extends BasePresenter {
        void search(String username);

    }

    interface View extends BaseView<Presenter> {
        void setRefresh(boolean isRefresh);
        void setUserList(List<User> userList);
        void toast(String msg);
    }
}
