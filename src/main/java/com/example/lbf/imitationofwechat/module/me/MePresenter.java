package com.example.lbf.imitationofwechat.module.me;

import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class MePresenter implements MeContract.Presenter {

    private MeContract.View mView;

    public MePresenter(MeContract.View view) {
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        User user = ChatsRepository.getInstance().getCurrentUser();
        mView.setUserInfo(user.getAvatar(),user.getName(),user.getUsername());
    }
}
