package com.example.lbf.imatationofwechat.moments;

import android.text.SpannableString;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;
import com.example.lbf.imatationofwechat.adapter.EmojiPagerAdapter;
import com.example.lbf.imatationofwechat.beans.MomentBean;
import com.example.lbf.imatationofwechat.views.CommentsTextView;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface MomentsContract {
    interface Presenter extends BasePresenter {
        void loadData(int page);

        void addComment( CommentsTextView view);

        void startComment(int targetContactId);

        void clickContact(int id);

        void setEmojiPanelData();

        void addEmojiExpression(int resourceId,String s);

        void deleteEmojiExpression();

        void showOrHideEmojiPanel(boolean showPanel);

        void setInputMode(int mode);
    }

    interface View extends BaseView<Presenter> {

        void setMomentList(List<MomentBean> beanList);

        void addMomentList(List<MomentBean> beanList);

        void setPanelAdapter(EmojiPagerAdapter adapter);

        void setPanelHeight(int height);

        void showBottomBar();

        void hideBottomBar();

        void showEmojiPanel();

        void hideEmojiPanel();

        void hideEmojiPanelDelayed();

        boolean isTextInputViewEmpty();

        String getTextInputViewContent();

        void clearTextInputViewContent();

        int getTextInputViewIndex();

        void appendString(SpannableString string);

        void deleteString(int index1, int index2);

        void showKeyboard();

        void hideKeyboard();
    }
}
