package com.example.lbf.imatationofwechat.chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.DataUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.beans.MessageBean;
import com.example.lbf.imatationofwechat.data.source.ChatsRepository;
import com.example.lbf.imatationofwechat.data.source.MessagesLoader;
import com.example.lbf.imatationofwechat.data.source.local.WeChatPersistenceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChatPresenter implements ChatContract.Presenter,LoaderManager.LoaderCallbacks<List<MessageBean>> {

    public static final long MINIMUM_INTERVAL = 1000 * 60 * 5;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private static int LOAD_MESSAGES = 0;
    private ChatContract.View mView;
    private ChatsRepository mRepository;
    private final LoaderManager mLoaderManager;
    private long latestMessageTime = 0;
//    新增的消息List
    private List<MessageBean> newMessageBeanList;
//    屏幕高度
    private int screenHeight;
//    输入法的高度
    private int inputMethodHeight;
//    标志输入法的高度是否有变化
    private boolean isInputMethodHeightDirty = false;

    public ChatPresenter(Context context, LoaderManager loaderManager, ChatContract.View view, ChatsRepository repository) {
        mContext = CommonUtil.checkNotNull(context);
        mLoaderManager = CommonUtil.checkNotNull(loaderManager, "loader manager cannot be null!");
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mRepository =  CommonUtil.checkNotNull(repository, "repository cannot be null!");
        mView.setPresenter(this);
        newMessageBeanList = new ArrayList<>();
        mSharedPreferences = context.getSharedPreferences(DataUtil.PREF_NAME,Context.MODE_PRIVATE);
        inputMethodHeight = mSharedPreferences.getInt(DataUtil.PREF_KEY_INPUT_METHOD_HEIGHT,0);
    }

    @Override
    public void start() {
        Bundle bundle = new Bundle();
        bundle.putInt("page",0);
        mLoaderManager.initLoader(LOAD_MESSAGES,bundle,this);
    }

    @Override
    public Loader<List<MessageBean>> onCreateLoader(int id, Bundle args) {
        mView.showLoading();
        MessagesLoader loader = new MessagesLoader(mContext,mRepository,ChatActivity.contactId);
        int page = args.getInt("page");
        loader.setPage(page);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<MessageBean>> loader, List<MessageBean> chatBeanList) {
        if(latestMessageTime == 0){
            latestMessageTime = chatBeanList.get(0).getTime();
            LogUtil.i("create time = "+latestMessageTime);
        }
        mView.addMessagesList(chatBeanList);
        mView.hideLoading();
    }

    @Override
    public void onLoaderReset(Loader<List<MessageBean>> loader) {
        LogUtil.i("onLoaderReset");
    }

    @Override
    public void stop() {
        saveInputMethodHeight();
        saveMessagesList(newMessageBeanList,ChatActivity.contactId);
    }

    @Override
    public void saveMessagesList(List<MessageBean> beanList, int contactId) {
        mRepository.saveMessageList(beanList,contactId);
    }

    @Override
    public void sendMessage(String content, int type) {
        long time = System.currentTimeMillis();
        boolean displayTime;
        if(time - latestMessageTime > MINIMUM_INTERVAL){
            displayTime = true;
            latestMessageTime = time;
        }else{
            displayTime = false;
        }
        MessageBean bean = new MessageBean(content,true,time,displayTime,type);
        newMessageBeanList.add(bean);
        mView.addMessage(bean);
        if(type == WeChatPersistenceContract.MsgsEntry.MSG_TYPE_TEXT){
            mView.clearTextInputViewContent();
            mView.hideSendButton();
            mView.showMoreButton();
        }
    }

    @Override
    public void loadMessages(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt("page",page);
        mLoaderManager.restartLoader(LOAD_MESSAGES,bundle,this);
    }

    @Override
    public float calculateStartScale(Rect startBounds, Rect finalBounds) {
        // 使用“center crop”的方式将初始的宽高比调整为与最终的宽高比相同，这样避免了
        // 动画期间不必要的伸缩。
        // 计算初始的scaling factor（设最终的scaling factor为1）
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        return startScale;
    }

    @Override
    public int getContactImage(int contactId) {
        return mRepository.getContactInfo(contactId).getImage();
    }

    @Override
    public int getUserImage() {
        return mRepository.getUserInfo().getImage();
    }

    @Override
    public void showOrHideEmojiPanel(boolean showView) {
        if (showView) {
            mView.hideMorePanel();
            mView.setPanelHeight(inputMethodHeight);
            mView.setTextInputHighlight(true);
            setInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            mView.hideKeyboard();
            mView.showEmojiPanel();
        } else {
            mView.hideEmojiPanelDelayed();

        }
    }

    @Override
    public void showOrHideMorePanel(boolean showView) {
        if (showView) {
            mView.hideVoiceInputView();
            mView.showTextInputView();
            mView.hideEmojiPanel();
            mView.setTextInputHighlight(false);
            mView.setPanelHeight(inputMethodHeight);
            setInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            mView.hideKeyboard();
            mView.showMorePanel();
        } else {
            mView.hideMorePanelDelayed();
        }
    }

    @Override
    public void showOrHideSendButton(boolean showView) {
        if (!showView) {
            mView.hideSendButton();
            mView.showMoreButton();
        } else {
            mView.showSendButton();
            mView.hideMoreButton();
        }
    }

    @Override
    public void showOrHideVoiceInput(boolean showView) {
        if (showView) {
            mView.hideMorePanel();
            mView.hideEmojiPanel();
            mView.hideSendButton();
            mView.hideTextInputView();
            mView.hideKeyboard();
            mView.showVoiceInputView();
            mView.showMoreButton();

        } else {
            if(!mView.isTextInputViewEmpty()){
                mView.hideMoreButton();
                mView.showSendButton();
            }
            mView.hideVoiceInputView();
            mView.showTextInputView();
            mView.showKeyboard();
        }
    }

    @Override
    public void addEmojiExpression(int resourceId, String s) {
        if(resourceId == R.drawable.emoji_delete){
            if(!mView.isTextInputViewEmpty()){
                deleteEmojiExpression();
            }
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,30,mContext.getResources().getDisplayMetrics());
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resourceId,options);
        bitmap = Bitmap.createScaledBitmap(bitmap,size,size,true);
        //  根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
        //  创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        SpannableString spannableString = new SpannableString("["+s+"]");
        //  用ImageSpan对象替换face
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mView.appendString(spannableString);
    }

    @Override
    public void deleteEmojiExpression() {
        String text = mView.getTextInputViewContent();
        int index2 = mView.getTextInputViewIndex();
        if(text.isEmpty()||index2 == 0){
            return;
        }
        int index1 = text.substring(0,index2-1).lastIndexOf("[");
        if(text.charAt(index2-1)!=']'||index1==-1){
            mView.deleteString(index2-1,index2);
        }else{
            mView.deleteString(index1,index2);
        }
    }

    @Override
    public void proposeLayoutChange(View root) {
        int currentHeight = root.getHeight();
        if (screenHeight == 0) {
            screenHeight = root.getHeight();
        }
        LogUtil.i("**************onGlobalLayout************* ");
        LogUtil.i("currentHeight = " + currentHeight);
        LogUtil.i("inputMethodHeight = " + inputMethodHeight);
        if (inputMethodHeight == 0) {
            inputMethodHeight = screenHeight - currentHeight;
            isInputMethodHeightDirty = true;
        }
//                不等于屏幕高度或者记录的输入法高度，说明输入法高度发生了变化，需要更新记录的高度
        if (currentHeight != screenHeight && currentHeight != (screenHeight - inputMethodHeight)) {
            inputMethodHeight = screenHeight - currentHeight;
            isInputMethodHeightDirty = true;
        }
    }

    @Override
    public void saveInputMethodHeight() {
        if (isInputMethodHeightDirty) {
            mSharedPreferences.edit().putInt(DataUtil.PREF_KEY_INPUT_METHOD_HEIGHT, inputMethodHeight).commit();
            isInputMethodHeightDirty = false;
        }
    }

    @Override
    public void setInputMode(int mode) {
        WindowManager.LayoutParams layoutParams = ((Activity)mContext).getWindow().getAttributes();
        if(layoutParams.softInputMode!=mode){
            layoutParams.softInputMode = mode;
            ((Activity)mContext).getWindow().setAttributes(layoutParams);
        }
    }
}
