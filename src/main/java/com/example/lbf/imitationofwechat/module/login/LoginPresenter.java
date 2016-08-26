package com.example.lbf.imitationofwechat.module.login;

import android.app.Activity;
import android.content.Intent;

import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.module.main.MainActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by lbf on 2016/7/29.
 */
public class LoginPresenter implements LoginContract.Presenter {
    private Activity mContext;
    private LoginContract.View mView;

    public LoginPresenter(Activity context, LoginContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void login(String username, String password) {
        ChatsRepository.getInstance().login(username,password, new LogInListener() {

            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    User user = (User)o;
                    BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                    mContext.startActivity(new Intent(mContext,MainActivity.class));
                    mContext.finish();
                } else {
                    mView.showError(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }
}
