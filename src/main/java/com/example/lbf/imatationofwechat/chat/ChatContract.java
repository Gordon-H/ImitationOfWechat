package com.example.lbf.imatationofwechat.chat;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.widget.ImageView;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;
import com.example.lbf.imatationofwechat.adapter.EmojiPagerAdapter;
import com.example.lbf.imatationofwechat.beans.MessageBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ChatContract {
    interface Presenter extends BasePresenter {
//        fragment在onPause时执行此方法，用于保存一些数据
        void stop();
//        保存消息列表
        void saveMessagesList(List<MessageBean> beanList, int contactId);
//        发送消息
        void sendMessage(String content,int type);
//        加载消息
        void loadMessages(int page);
//        计算出缩放因子
        float calculateStartScale(Rect startBounds, Rect finalBounds);
//        获取联系人头像
        int getContactImage(int contactId);
//        获取用户头像
        int getUserImage();
//        显示或关闭表情面板
        void showOrHideEmojiPanel(boolean showView);
//        显示或关闭“更多”面板
        void showOrHideMorePanel(boolean showView);
//        显示或关闭发送按钮
        void showOrHideSendButton(boolean showView);
//        显示或关闭发送按钮
        void showOrHideVoiceInput(boolean showView);
//        插入表情
        void addEmojiExpression(int resourceId,String s);
//        删除表情
        void deleteEmojiExpression();

//        调整输入法高度时，根布局会发生变化，此时应记录下输入法的高度
        void proposeLayoutChange(android.view.View root);
//        保存输入法高度
        void saveInputMethodHeight();
//        设置输入法模式
        void setInputMode(int mode);
    }

    interface View extends BaseView<Presenter> {
//        设置消息列表
        void addMessagesList(List<MessageBean> beanList);
//        获取消息列表
        List<MessageBean> getMessagesList();
//        更新消息列表
        void updateMessagesList();
//        删除消息
        void deleteMessage(int position);
//        添加消息
        void addMessage(MessageBean messageBean);
//        长按chat item后展开菜单进行编辑
        void showEditMenu(android.view.View v, MessageBean messageBean);
//        双击消息后全屏展示
        void showMessageDetail(String content);
//        点击图片后全屏展示
        void showImageDetail(ImageView imageView, String imagePath);
//        再次点击图片退出全屏
        void hideImageDetail();
//        显示发送按钮
        void showSendButton();
//        隐藏发送按钮
        void hideSendButton();
//        显示更多按钮
        void showMoreButton();
//        隐藏更多按钮
        void hideMoreButton();
//        显示表情面板
        void showEmojiPanel();
//        隐藏表情面板
        void hideEmojiPanel();
//        延迟执行隐藏表情面板（用于调整输入法和面板切换的闪动问题）
        void hideEmojiPanelDelayed();
//        显示“更多”面板
        void showMorePanel();
//        隐藏“更多”面板
        void hideMorePanel();
//        延迟执行隐藏“更多”面板（用于调整输入法和面板切换的闪动问题）
        void hideMorePanelDelayed();
//        点击更多面板中的选项时执行相应方法
        void showMorePanelItem(int position);
//        显示语音输入面板
        void showVoiceInputView();
//        隐藏语音输入面板
        void hideVoiceInputView();
//        显示语音输入面板
        void showTextInputView();
//        隐藏语音输入面板
        void hideTextInputView();
//        显示键盘
        void showKeyboard();
//        隐藏键盘
        void hideKeyboard();
//        输入框是否为空
        boolean isTextInputViewEmpty();
//        获取输入框的内容
        String getTextInputViewContent();
//        清空输入空内容
        void clearTextInputViewContent();
//        获取输入框当前的位置
        int getTextInputViewIndex();

//        设置文本输入框的图片是否取得焦点
        void setTextInputHighlight(boolean highlight);
//        设置面板的高度
        void setPanelHeight(int height);
//        设置表情面板的adapter
        void setEmojiAdapter(EmojiPagerAdapter adapter);
//        设置更多面板的adapter
        void setMoreAdapter(RecyclerView.Adapter adapter);
//        添加string
        void appendString(SpannableString string);
//        删除string
        void deleteString(int index1, int index2);


    }
}
