package com.example.lbf.imitationofwechat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseActivity;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.module.login.LoginActivity;
import com.example.lbf.imitationofwechat.module.main.MainActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = ChatsRepository.getInstance().getCurrentUser();
                Intent intent;
                if (user == null) {
                    intent = new Intent(SplashActivity.this,LoginActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },1000);

    }
}
