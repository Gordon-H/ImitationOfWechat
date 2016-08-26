package com.example.lbf.imitationofwechat.beans;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.data.source.local.db.NewFriend;
import com.example.lbf.imitationofwechat.data.source.local.db.NewFriendManager;
import com.example.lbf.imitationofwechat.module.newFriend.NewFriendActivity;
import com.example.lbf.imitationofwechat.BmobIMApplication;
import com.example.lbf.imitationofwechat.util.Config;


/**
 * 新朋友会话
 * Created by Administrator on 2016/5/25.
 */
public class NewFriendConversation extends Conversation{

    NewFriend lastFriend;

    public NewFriendConversation(NewFriend friend){
        this.lastFriend=friend;
        this.cName="新朋友";
    }

    @Override
    public String getLastMessageContent() {
        if(lastFriend!=null){
            Integer status =lastFriend.getStatus();
            String name = lastFriend.getName();
            if(TextUtils.isEmpty(name)){
                name = lastFriend.getUid();
            }
            //目前的好友请求都是别人发给我的
            if(status==null || status== Config.STATUS_VERIFY_NONE||status == Config.STATUS_VERIFY_READED){
                return name+"请求添加好友";
            }else{
                return "我已添加"+name;
            }
        }else{
            return "";
        }
    }

    @Override
    public boolean isOnTop() {
        return false;
    }

    @Override
    public void setOnTop(boolean isOnTop) {

    }

    @Override
    public long getLastMessageTime() {
        if(lastFriend!=null){
            return lastFriend.getTime();
        }else{
            return 0;
        }
    }

    @Override
    public String getAvatar() {
        return "drawable://"+R.drawable.display_picture_default;
    }

    @Override
    public int getUnReadCount() {
        return NewFriendManager.getInstance(BmobIMApplication.INSTANCE()).getNewInvitationCount();
    }

    @Override
    public void readAllMessages() {
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(BmobIMApplication.INSTANCE()).updateBatchStatus();
    }

    @Override
    public void onClick(Context context, View v) {
        Intent intent = new Intent();
        intent.setClass(context, NewFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onLongClick(Context context, View v) {
        NewFriendManager.getInstance(context).deleteNewFriend(lastFriend);
    }
}
