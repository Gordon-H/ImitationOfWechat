package com.example.lbf.imatationofwechat.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.ActivityUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        MainFragment mainFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (mainFragment == null) {
            // Create the fragment
            mainFragment = new MainFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), mainFragment, R.id.flContent);
        }
        MainPresenter mainPresenter = new MainPresenter(this,mainFragment);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("微信");
    }

}
