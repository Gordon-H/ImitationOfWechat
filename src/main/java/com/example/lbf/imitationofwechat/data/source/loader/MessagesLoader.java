package com.example.lbf.imitationofwechat.data.source.loader;

import android.content.Context;

import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.module.chat.ChatPresenter;
import com.example.lbf.imitationofwechat.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by lbf on 2016/7/30.
 */
public class MessagesLoader extends BaseLoader<List<BmobIMMessage>> {
    private BmobIMConversation mConversation;
    private BmobIMMessage msg;

    public void setMsg(BmobIMMessage msg) {
        this.msg = msg;
    }

    public MessagesLoader(Context context, ChatsRepository repository, BmobIMConversation c) {
        super(context,repository);
        mConversation = c;
    }

    @Override
    public List<BmobIMMessage> loadInBackground() {
        final List<BmobIMMessage> messageList = new ArrayList();
        mConversation.queryMessages(msg, ChatPresenter.MESSAGES_PER_PAGE, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        messageList.addAll(list);
                    }
                } else {
                    LogUtil.i(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
        return messageList;
    }
}
