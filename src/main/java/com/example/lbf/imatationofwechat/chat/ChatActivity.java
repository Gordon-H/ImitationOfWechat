package com.example.lbf.imatationofwechat.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.chooseImage.ChooseImageActivity;
import com.example.lbf.imatationofwechat.data.source.ChatsRepository;
import com.example.lbf.imatationofwechat.data.source.local.ChatsLocalDataSource;
import com.example.lbf.imatationofwechat.util.ActivityUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;

public class ChatActivity extends AppCompatActivity {
    public static final int CODE_SELECT_IMAGES = 1;
    public static final String INTENT_KEY_CONTACT_ID = "contact_id";
    public static int contactId;
    public static String contactName;
    private ChatFragment mChatFragment;
    private ChatPresenter mPresenter;
    private ChatsRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mRepository = ChatsRepository.getInstance(null,ChatsLocalDataSource.getInstance(this));
        initView();
        mChatFragment =
                (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (mChatFragment == null) {
            // Create the fragment
            mChatFragment = new ChatFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), mChatFragment, R.id.flContent);
        }
        mPresenter = new ChatPresenter(this,getSupportLoaderManager(),mChatFragment,mRepository);
    }

    private void initView() {
        Intent intent = getIntent();
        contactId = intent.getIntExtra(INTENT_KEY_CONTACT_ID,0);
        contactName = mRepository.getContactInfo(contactId).getName();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(contactName);
        toolbar.setNavigationIcon(R.drawable.actionbar_icon_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        sendMessage(intent);
    }

    void sendMessage(Intent intent){
        String[] contents = intent.getStringArrayExtra(ChooseImageActivity.KEY_CONTENT);
        int type = intent.getIntExtra(ChooseImageActivity.KEY_TYPE,-1);
        if(contents!=null && type != -1){
            for(int i = 0 ;i<contents.length;i++){
                String s = contents[i];
                mPresenter.sendMessage(s,type);
            }
        } else {
            LogUtil.i("no intent data");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("olt =requestCode "+requestCode);
        LogUtil.i("olt =result "+resultCode);
        switch (requestCode){
            case ChatFragment.CODE_SELECT_IMAGES:
                if(resultCode == Activity.RESULT_OK){
                    sendMessage(data);
                }
        }
    }
}
