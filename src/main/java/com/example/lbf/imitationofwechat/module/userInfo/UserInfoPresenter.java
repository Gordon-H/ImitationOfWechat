package com.example.lbf.imitationofwechat.module.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.lbf.imitationofwechat.beans.AddFriendMessage;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.event.RefreshEvent;
import com.example.lbf.imitationofwechat.module.chat.ChatActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by lbf on 2016/7/29.
 */
public class UserInfoPresenter implements UserInfoContract.Presenter {

    private Activity mContext;
    private UserInfoContract.View mView;
    private ChatsRepository repository;
    private ContactBean contact;
    private BmobIMUserInfo info;

    public UserInfoPresenter(Activity context, UserInfoContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
        repository = ChatsRepository.getInstance();
    }

    @Override
    public void start() {
        contact = mContext.getIntent().getExtras().getParcelable(UserInfoActivity.KEY_CONTACT);
        boolean isFriend = mContext.getIntent().getExtras().getBoolean(UserInfoActivity.KEY_IS_FRIEND);
        if(isFriend){
            mView.hideBtAddFriend();
            mView.showBtChat();
        }else{
            mView.showBtAddFriend();
            mView.hideBtChat();
            mView.hideLayoutTag();
        }
        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(contact.getUserId(),contact.getRemarks(),contact.getImage());
        LogUtil.i("userId = "+info.getUserId());
        LogUtil.i("getName = "+info.getName());
        LogUtil.i("getImage = "+info.getAvatar());
        mView.setUserInfo(contact);
    }

    @Override
    public ContactBean getContact() {
        return contact;
    }

    @Override
    public void addFriend() {
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        LogUtil.i("info"+info.getUserId()+info.getName()+info.getAvatar());
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg =new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(mContext,User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String,Object> map =new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar",currentUser.getAvatar());//发送者的头像
        map.put("uid",currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    mView.toast("好友请求发送成功，等待验证");
                } else {//发送失败
                    mView.toast("发送失败:" + e.getMessage());
                }
            }
        });

    }

    @Override
    public void chat() {

        BmobIM.getInstance().startPrivateConversation( info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if(e==null){
                    //在此跳转到聊天页面
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ChatActivity.KEY_CONVERSATION, c);
                    Intent intent = new Intent(mContext,ChatActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }else{
                    LogUtil.i(e.getMessage()+"("+e.getErrorCode()+")");
                }
            }
        });

    }

    @Override
    public void saveRemarksAndTag(String remarks,String tag) {
        contact.setRemarks(remarks);
        contact.setTag(tag);
        ChatsRepository.getInstance().updateContact(contact);
    }

    @Override
    public void deleteFriend() {
        repository.deleteContact(contact, new DeleteListener() {
            @Override
            public void onSuccess() {
                LogUtil.i("删除成功");
                EventBus.getDefault().post(new RefreshEvent());
                mContext.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("删除失败"+s);
            }
        });
    }

    @Override
    public void setIsStarred(boolean isStarred) {
        String sortKey = contact.getSortKey();
        char c0 = sortKey.charAt(0);
        char c1 = sortKey.charAt(1);
        if(isStarred){
            contact.setSortKey("1"+sortKey.substring(1));
        }else{
            if(c1>='A' && c1<='Z'){
                contact.setSortKey("2"+sortKey.substring(1));
            }else{
                contact.setSortKey("3"+sortKey.substring(1));
            }
        }
        repository.updateContact(contact);
    }
}
