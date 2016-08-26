package com.example.lbf.imitationofwechat.module.imageDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.util.ActivityUtil;

public class ImageDetailActivity extends AppCompatActivity {
    private ImageDetailPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ImageDetailFragment fragment =
                (ImageDetailFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment == null) {
            // Create the fragment
            fragment = new ImageDetailFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.flContent);
        }
        presenter = new ImageDetailPresenter(this,fragment);
    }

    @Override
    public void onBackPressed() {
        presenter.back();
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
