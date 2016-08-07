package com.example.lbf.imatationofwechat.moments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.data.source.ChatsRepository;
import com.example.lbf.imatationofwechat.data.source.local.ChatsLocalDataSource;
import com.example.lbf.imatationofwechat.util.ActivityUtil;

public class MomentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        initView();
        MomentsFragment fragment =
                (MomentsFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment == null) {
            // Create the fragment
            fragment = new MomentsFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.flContent);
        }
        ChatsRepository repository = ChatsRepository.getInstance(null,
                ChatsLocalDataSource.getInstance(this));
        MomentsPresenter presenter = new MomentsPresenter(this,getSupportLoaderManager(),
                fragment,repository);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

}
