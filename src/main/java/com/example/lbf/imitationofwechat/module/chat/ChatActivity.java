package com.example.lbf.imitationofwechat.module.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseActivity;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.module.chooseImage.ChooseImageActivity;
import com.example.lbf.imitationofwechat.util.ActivityUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.core.BmobIMClient;

public class ChatActivity extends BaseActivity {

    public static final String KEY_CONVERSATION = "Conversation";
    public static final int CODE_SELECT_IMAGES = 1;
    private ChatFragment mChatFragment;
    private ChatPresenter mPresenter;
    private ChatsRepository mRepository;
    private BmobIMConversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mRepository = ChatsRepository.getInstance();
        mChatFragment =
                (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (mChatFragment == null) {
            // Create the fragment
            mChatFragment = new ChatFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), mChatFragment, R.id.flContent);
        }
        mPresenter = new ChatPresenter(this,getSupportLoaderManager(),mChatFragment,mRepository);
        mPresenter.setConversation(conversation);
    }

    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        BmobIMConversation c = (BmobIMConversation) bundle.getSerializable(KEY_CONVERSATION);
        conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        String contactName = conversation.getConversationTitle();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(contactName);
        toolbar.setNavigationIcon(R.drawable.actionbar_icon_back);
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        sendMessages(intent);
    }

    void sendMessages(Intent intent){
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
        switch (requestCode){
            case CODE_SELECT_IMAGES:
                if(resultCode == Activity.RESULT_OK){
                    sendMessages(data);
                }
                break;
        }
    }
}
