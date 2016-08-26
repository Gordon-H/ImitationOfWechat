package com.example.lbf.imitationofwechat.data.source.loader;

import android.content.Context;

import com.example.lbf.imitationofwechat.beans.Conversation;
import com.example.lbf.imitationofwechat.beans.PrivateConversation;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by lbf on 2016/7/30.
 */
public class ChatsLoader extends BaseLoader<List<Conversation>> {

    public ChatsLoader(Context context, ChatsRepository repository) {
        super(context,repository);
    }

    @Override
    public List<Conversation> loadInBackground() {
        List<BmobIMConversation> bmobIMConversationList = BmobIM.getInstance().loadAllConversation();
        if(bmobIMConversationList == null || bmobIMConversationList.size() == 0){
            return null;
        }
        List<Conversation> conversationList = new ArrayList<>(bmobIMConversationList.size());
        for(BmobIMConversation c:bmobIMConversationList){
            Conversation conversation = new PrivateConversation(c);
            conversationList.add(conversation);
        }
        Collections.sort(conversationList);
        return conversationList;
    }


}
