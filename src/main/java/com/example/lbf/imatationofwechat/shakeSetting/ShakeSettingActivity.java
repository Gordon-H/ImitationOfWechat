package com.example.lbf.imatationofwechat.shakeSetting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.ActivityUtil;

public class ShakeSettingActivity extends AppCompatActivity {

    public static final int CODE_SELECT_IMAGE = 0;
    //    用于表示默认图片
    public static final String DEFAULT_IMAGE = "default_image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ShakeSettingFragment mainFragment =
                (ShakeSettingFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (mainFragment == null) {
            // Create the fragment
            mainFragment = new ShakeSettingFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), mainFragment, R.id.flContent);
        }
        ShakeSettingPresenter mainPresenter = new ShakeSettingPresenter(this,mainFragment);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("摇一摇设置");
        setSupportActionBar(toolbar);
    }

}
