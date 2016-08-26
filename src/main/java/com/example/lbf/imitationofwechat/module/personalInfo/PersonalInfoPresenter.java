package com.example.lbf.imitationofwechat.module.personalInfo;

import android.content.Context;

import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by lbf on 2016/7/29.
 */
public class PersonalInfoPresenter implements PersonalInfoContract.Presenter {

    private Context mContext;
    private PersonalInfoContract.View mView;
    private User user;
    private boolean isUserInfoDirty = false;
    public PersonalInfoPresenter(Context context, PersonalInfoContract.View view) {
        mContext = CommonUtil.checkNotNull(context);
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        user = ChatsRepository.getInstance().getCurrentUser();
        mView.setUserInfo(user.getAvatar(),user.getName(),user.getUsername(),
                user.getMale(),user.getRegion(), user.getSignature());
    }

    @Override
    public void saveAvatar(String imageUrl) {
        user.setAvatar(imageUrl);
        isUserInfoDirty = true;
    }

    @Override
    public void saveName(String name) {
        user.setName(name);
        isUserInfoDirty = true;
    }

    @Override
    public void saveSignature(String signature) {
        user.setSignature(signature);
        isUserInfoDirty = true;
    }

    @Override
    public void saveGender(boolean isMale) {
        user.setMale(isMale);
        isUserInfoDirty = true;
    }

    @Override
    public void saveLocation(String location) {
        user.setRegion(location);
        isUserInfoDirty = true;
    }

    @Override
    public void stop() {
        if(isUserInfoDirty){
            isUserInfoDirty = false;
            user.update(mContext, new UpdateListener() {
                @Override
                public void onSuccess() {
                    LogUtil.i("update successfully");
                }

                @Override
                public void onFailure(int i, String s) {
                    LogUtil.i("update failed!"+s);
                }
            });
        }
    }
}
