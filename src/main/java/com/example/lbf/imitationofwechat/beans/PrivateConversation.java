package com.example.lbf.imitationofwechat.beans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.module.chat.ChatActivity;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMConversationType;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;

public class PrivateConversation extends Conversation{

    private BmobIMMessage lastMsg;

    public PrivateConversation(BmobIMConversation conversation){
        bmobIMConversation = conversation;
        cType = BmobIMConversationType.setValue(conversation.getConversationType());
        cId = conversation.getConversationId();
        if (cType == BmobIMConversationType.PRIVATE){
            cName=conversation.getConversationTitle();
            if (TextUtils.isEmpty(cName)) cName = cId;
        }else{
            cName="未知会话";
        }
        List<BmobIMMessage> msgs =conversation.getMessages();
        if(msgs!=null && msgs.size()>0){
            lastMsg =msgs.get(0);
        }
    }

    @Override
    public void readAllMessages() {
        bmobIMConversation.updateLocalCache();
    }

    @Override
    public void onClick(Context context,View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChatActivity.KEY_CONVERSATION, bmobIMConversation);
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onLongClick(Context context,View v) {
        v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (isOnTop()) {
                    menu.add(0, R.id.context_unset_on_top, 0, "取消置顶");
                } else {
                    menu.add(0, R.id.context_set_on_top, 0, "置顶聊天");
                }
                menu.add(0, R.id.context_delete_chat, 1, "删除该聊天");
            }
        });
    }

    @Override
    public String getAvatar() {
        if (cType == BmobIMConversationType.PRIVATE){
            String avatar =  bmobIMConversation.getConversationIcon();
            if (TextUtils.isEmpty(avatar)){//头像为空，使用默认头像
                return "drawable:"+R.drawable.display_picture_default;
            }else{
                return avatar;
            }
        }else{
            return "drawable:"+R.drawable.display_picture_default;
        }
    }

    @Override
    public String getLastMessageContent() {
        if(lastMsg!=null){
            String content =lastMsg.getContent();
            if(lastMsg.getMsgType().equals(BmobIMMessageType.TEXT.getType()) || lastMsg.getMsgType().equals("agree")){
                return content;
            }else if(lastMsg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
                return "[图片]";
            }else if(lastMsg.getMsgType().equals(BmobIMMessageType.VOICE.getType())){
                return "[语音]";
            }else if(lastMsg.getMsgType().equals(BmobIMMessageType.LOCATION.getType())){
                return"[位置]";
            }else if(lastMsg.getMsgType().equals(BmobIMMessageType.VIDEO.getType())){
                return "[视频]";
            }else{//开发者自定义的消息类型，需要自行处理
                return "[未知]";
            }
        }else{//防止消息错乱
            return "";
        }
    }

    @Override
    public boolean isOnTop() {
        Log.i("info", "isOnTop: "+bmobIMConversation.getIsTop());
        if(bmobIMConversation.getIsTop()!=null){
            return bmobIMConversation.getIsTop();
        }else{
            return false;
        }
    }

    @Override
    public void setOnTop(boolean isOnTop) {
        if(bmobIMConversation!=null){
            bmobIMConversation.setIsTop(isOnTop);
            bmobIMConversation.update();
        }

    }


    @Override
    public long getLastMessageTime() {
        if(lastMsg!=null) {
            return lastMsg.getCreateTime();
        }else{
            return 0;
        }
    }

    @Override
    public int getUnReadCount() {
        return (int)BmobIM.getInstance().getUnReadCount(bmobIMConversation.getConversationId());
    }

}
