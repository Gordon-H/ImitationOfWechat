package com.example.lbf.imitationofwechat.module.newFriend;

import android.content.Context;

import com.example.lbf.imitationofwechat.data.source.local.db.NewFriendManager;
import com.example.lbf.imitationofwechat.util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class NewFriendPresenter implements NewFriendContract.Presenter {
    private Context mContext;
    private NewFriendContract.View mView;

    public NewFriendPresenter(Context context, NewFriendContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.setUserList(NewFriendManager.getInstance(mContext).getAllNewFriend());
    }

    @Override
    public void showUserInfo(int position) {

    }

    @Override
    public void acceptNewFriend(int position) {

    }
}
