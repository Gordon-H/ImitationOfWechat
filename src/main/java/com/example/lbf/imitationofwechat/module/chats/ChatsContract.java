package com.example.lbf.imitationofwechat.module.chats;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;
import com.example.lbf.imitationofwechat.beans.Conversation;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ChatsContract {
    interface Presenter extends BasePresenter {
//        删除聊天
        void deleteChats(int position);
//        置顶聊天
        void setOnTop(int position);
//        取消置顶
        void unsetOnTop(int position);

        void restartLoader();
    }

    interface View extends BaseView<Presenter> {
//        设置聊天列表
        void setChatsList(List<Conversation> conversationList);

        void updateList();
//        点击chat item后展示聊天内容
        void showChat(int contactId);
//        长按chat item后展开菜单进行编辑
        void showEditMenu(android.view.View v, Conversation c);

    }
}
