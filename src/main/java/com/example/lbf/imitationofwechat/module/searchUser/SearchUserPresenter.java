package com.example.lbf.imitationofwechat.module.searchUser;

import android.content.Context;
import android.text.TextUtils;

import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.remote.ChatsRemoteDataSource;
import com.example.lbf.imitationofwechat.util.CommonUtil;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by lbf on 2016/7/29.
 */
public class SearchUserPresenter implements SearchUserContract.Presenter {
    private Context mContext;
    private SearchUserContract.View mView;
    private ChatsRepository mRepository;

    public SearchUserPresenter(Context context, SearchUserContract.View view, ChatsRepository repository) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mRepository = CommonUtil.checkNotNull(repository,"repository cannot be null!");
        mView.setPresenter(this);

    }

    @Override
    public void start() {
    }

    @Override
    public void search(String name) {
        if(TextUtils.isEmpty(name)){
            mView.toast("请填写用户名");
            mView.setRefresh(false);
            return;
        }
        mRepository.getInstance().queryUsers(name, ChatsRemoteDataSource.DEFAULT_LIMIT, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                mView.setRefresh(false);
                mView.setUserList(list);
            }

            @Override
            public void onError(int i, String s) {
                mView.setRefresh(false);
                mView.toast(s + "(" + i + ")");
            }
        });
    }
}
