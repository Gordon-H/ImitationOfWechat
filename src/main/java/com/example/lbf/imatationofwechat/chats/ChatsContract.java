package com.example.lbf.imatationofwechat.chats;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;
import com.example.lbf.imatationofwechat.beans.ChatBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ChatsContract {
    interface Presenter extends BasePresenter {
//        保存聊天列表
        void saveChatsList(List<ChatBean> chatBeanList, int onTopNumber);
//        删除公众号
        void deleteAccount(int accountId);
    }

    interface View extends BaseView<Presenter> {
//        设置聊天列表
        void setChatsList(List<ChatBean> beanList);
//        获取聊天列表
        List<ChatBean> getChatsList();
//        更新聊天列表
        void updateChatsList();
//        删除聊天
        void deleteChats(int position);
//        置顶聊天
        void setOnTop(int position);
//        取消置顶
        void unsetOnTop(int position);
//        不再关注公众号
        void unFollowAccount(int position);
//        点击chat item后展示聊天内容
        void showChat(int contactId);
//        长按chat item后展开菜单进行编辑
        void showEditMenu(android.view.View v, ChatBean chatBean);

    }
}
