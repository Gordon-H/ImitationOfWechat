package com.example.lbf.imatationofwechat.follow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.data.source.AccountsLoader;
import com.example.lbf.imatationofwechat.data.source.ChatsRepository;
import com.example.lbf.imatationofwechat.data.source.local.ChatsLocalDataSource;
import com.example.lbf.imatationofwechat.util.ActivityUtil;

public class FollowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        initView();
        FollowFragment fragment =
                (FollowFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment == null) {
            // Create the fragment
            fragment = new FollowFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.flContent);
        }
        ChatsRepository repository = ChatsRepository.getInstance(null, ChatsLocalDataSource.getInstance(this));
        AccountsLoader loader = new AccountsLoader(this,repository);
        FollowPresenter mainPresenter = new FollowPresenter(
                getSupportLoaderManager(),loader,fragment,repository);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("公众号");
        toolbar.setNavigationIcon(R.drawable.actionbar_icon_back);
        setSupportActionBar(toolbar);
    }

}
