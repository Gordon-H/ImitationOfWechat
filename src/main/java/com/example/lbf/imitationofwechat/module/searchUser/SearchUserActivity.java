package com.example.lbf.imitationofwechat.module.searchUser;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseActivity;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.util.ActivityUtil;

public class SearchUserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        SearchUserFragment fragment =
                (SearchUserFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment == null) {
            // Create the fragment
            fragment = new SearchUserFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.flContent);
        }
        SearchUserPresenter presenter = new SearchUserPresenter(this,fragment, ChatsRepository.getInstance());
    }

    @Override
    protected void initView() {
        super.initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("添加好友");
        toolbar.setNavigationIcon(R.drawable.actionbar_icon_back);
        setSupportActionBar(toolbar);
    }
}
