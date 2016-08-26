package com.example.lbf.imitationofwechat.module.changeText;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseActivity;
import com.example.lbf.imitationofwechat.util.ActivityUtil;

public class changeTextActivity extends BaseActivity {

    public static final String KEY_CONTENT1 = "Content1";
    public static final String KEY_CONTENT2 = "Content2";
    public static final String KEY_TITLE = "Title";
    public static final String KEY_HINT1 = "Hint1";
    public static final String KEY_HINT2 = "Hint2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_text);
        changeTextFragment fragment =
                (changeTextFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment == null) {
            // Create the fragment
            fragment = new changeTextFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.flContent);
        }
        changeTextPresenter presenter = new changeTextPresenter(this,fragment);
    }

    @Override
    protected void initView() {
        super.initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
