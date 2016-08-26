package com.example.lbf.imitationofwechat.module.newFriend;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseActivity;
import com.example.lbf.imitationofwechat.util.ActivityUtil;

public class NewFriendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        NewFriendFragment fragment =
                (NewFriendFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment == null) {
            // Create the fragment
            fragment = new NewFriendFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.flContent);
        }
        NewFriendPresenter presenter = new NewFriendPresenter(this,fragment);
    }

    @Override
    protected void initView() {
        super.initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("新的朋友");
        toolbar.setNavigationIcon(R.drawable.actionbar_icon_back);
        setSupportActionBar(toolbar);
    }
}
