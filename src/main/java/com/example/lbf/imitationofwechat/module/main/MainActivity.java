package com.example.lbf.imitationofwechat.module.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseActivity;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.local.db.NewFriendManager;
import com.example.lbf.imitationofwechat.event.RefreshEvent;
import com.example.lbf.imitationofwechat.util.ActivityUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends BaseActivity {

    private MainFragment mainFragment;
    private MainPresenter mainPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (mainFragment == null) {
            // Create the fragment
            mainFragment = new MainFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), mainFragment, R.id.flContent);
        }
        ChatsRepository repository = ChatsRepository.getInstance();
        mainPresenter = new MainPresenter(this, repository, mainFragment);
        User user = BmobUser.getCurrentUser(this, User.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "connect success", Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    LogUtil.i(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                LogUtil.i("" + status.getMsg());
            }
        });
    }

    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("微信");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示小红点
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BmobNotificationManager.getInstance(this).clearObserver();
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        LogUtil.i("---主页接收到自定义消息---");
        checkRedPoint();
    }


    private void checkRedPoint() {
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        LogUtil.i("count = " + count);
        mainFragment.setUnreadCount(count);
        //是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            LogUtil.i(" NewFriendManager.getInstance(this).hasNewFriendInvitation() ");
//            iv_contact_tips.setVisibility(View.VISIBLE);
        } else {
//            iv_contact_tips.setVisibility(View.GONE);
        }
    }
}
