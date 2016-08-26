package com.example.lbf.imitationofwechat.module.userInfo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseActivity;
import com.example.lbf.imitationofwechat.util.ActivityUtil;

public class UserInfoActivity extends BaseActivity {

    public static final String KEY_CONTACT = "contact";
    public static final String KEY_IS_FRIEND = "isFriend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        UserInfoFragment fragment =
                (UserInfoFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment == null) {
            // Create the fragment
            fragment = new UserInfoFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.flContent);
        }
        UserInfoPresenter presenter = new UserInfoPresenter(this,fragment);
    }

    @Override
    protected void initView() {
        super.initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("详细资料");
        toolbar.setNavigationIcon(R.drawable.actionbar_icon_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
