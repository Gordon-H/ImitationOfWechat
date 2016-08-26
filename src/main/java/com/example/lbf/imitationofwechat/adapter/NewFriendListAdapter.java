package com.example.lbf.imitationofwechat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imitationofwechat.Interface.QueryUserListener;
import com.example.lbf.imitationofwechat.beans.AgreeAddFriendMessage;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.base.BaseAdapter;
import com.example.lbf.imitationofwechat.base.BaseViewHolder;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.local.db.NewFriend;
import com.example.lbf.imitationofwechat.data.source.local.db.NewFriendManager;
import com.example.lbf.imitationofwechat.util.Config;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lbf on 2016/6/28.
 */
public class NewFriendListAdapter extends BaseAdapter<NewFriend> {
    private Context mContext;

    public NewFriendListAdapter(Context context) {
        super(context,R.layout.item_new_friend);
        mContext = context;
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final NewFriend newFriend) {
        holder.setImage(R.id.iv_avatar,newFriend.getAvatar())
                .setText(R.id.tv_name,newFriend.getName());
        holder.get(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(v,holder.getAdapterPosition());
                }
            }
        });
        holder.setOnClickLisener(R.id.bt_accept, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreeAdd(newFriend, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        holder.setText(R.id.bt_accept,"已添加");
                        holder.setEnabled(R.id.bt_accept,false);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        holder.setEnabled(R.id.bt_accept,true);
                        Toast.makeText(mContext,"添加好友失败:" + s,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void agreeAdd(final NewFriend add, final SaveListener listener){
        ChatsRepository.getInstance().queryUserInfo(add.getUid(), new QueryUserListener() {
            @Override
            public void done(User s, BmobException e) {
                ContactBean contact = new ContactBean(s);
                ChatsRepository.getInstance().addContact(contact, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        sendAgreeAddFriendMessage(add, listener);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        listener.onFailure(i, s);
                    }
                });
            }
        });

    }

    /**
     * 发送同意添加好友的请求
     */
    private void sendAgreeAddFriendMessage(final NewFriend add, final SaveListener listener){
        BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), add.getAvatar());
        //如果为true,则表明为暂态会话，也就是说该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
        //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
        AgreeAddFriendMessage msg =new AgreeAddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(mContext, User.class);
        msg.setContent("我通过了你的好友验证请求，我们可以开始聊天了!");//---这句话是直接存储到对方的消息表中的
        Map<String,Object> map =new HashMap<>();
        map.put("msg",currentUser.getUsername()+"同意添加你为好友");//显示在通知栏上面的内容
        map.put("uid",add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
        map.put("time", add.getTime());//添加好友的请求时间
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e){
                if (e == null) {//发送成功
                    //修改本地的好友请求记录
                    NewFriendManager.getInstance(mContext).updateNewFriend(add, Config.STATUS_VERIFIED);
                    listener.onSuccess();
                } else {//发送失败
                    listener.onFailure(e.getErrorCode(),e.getMessage());
                }
            }
        });
    }
}
