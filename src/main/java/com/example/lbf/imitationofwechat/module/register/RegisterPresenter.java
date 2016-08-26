package com.example.lbf.imitationofwechat.module.register;

import android.app.Activity;
import android.content.Intent;

import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.remote.ChatsRemoteDataSource;
import com.example.lbf.imitationofwechat.event.FinishEvent;
import com.example.lbf.imitationofwechat.module.main.MainActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by lbf on 2016/7/29.
 */
public class RegisterPresenter implements RegisterContract.Presenter {
    private Activity mContext;
    private RegisterContract.View mView;

    public RegisterPresenter(Activity context, RegisterContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void register(String username, String name, String password, String passwordAgain) {
        ChatsRepository.getInstance().register(username,name, password, passwordAgain,new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                if(e==null){
                    EventBus.getDefault().post(new FinishEvent());
                    mContext.startActivity(new Intent(mContext,MainActivity.class));
                    mContext.finish();
                }else{
                    if(e.getErrorCode()== ChatsRemoteDataSource.CODE_NOT_EQUAL){
                        mView.clearPassword();
                    }
                    mView.showError(e.getMessage()+"("+e.getErrorCode()+")");
                }
            }
        });
    }
}
