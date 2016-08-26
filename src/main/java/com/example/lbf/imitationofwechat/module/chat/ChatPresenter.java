package com.example.lbf.imitationofwechat.module.chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.loader.MessagesLoader;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.DataUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChatPresenter implements ChatContract.Presenter,
        LoaderManager.LoaderCallbacks<List<BmobIMMessage>>,
        MessageListHandler,
        ObseverListener {
    public static final int MSG_TYPE_TXT = 1;
    public static final int MSG_TYPE_IMAGE_LOCAL = 2;
    public static final int MSG_TYPE_IMAGE_REMOTE = 3;
    public static final int MSG_TYPE_AUDIO = 4;
    private static int LOAD_MESSAGES = 0;
//    每页的消息数量
    public static final int MESSAGES_PER_PAGE = 18;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private BmobIMConversation mConversation;
    private ChatContract.View mView;
    private ChatsRepository mRepository;
    private final LoaderManager mLoaderManager;
//    当前用户的ID
    private String currentId;
//    消息list
    private List<BmobIMMessage> messageList;
//    图片List
    private List<String> imageList;
//    屏幕高度
    private int screenHeight;
//    输入法的高度
    private int inputMethodHeight;
//    标志输入法的高度是否有变化
    private boolean isInputMethodHeightDirty = false;
    private BmobRecordManager recordManager;

    public ChatPresenter(Context context, LoaderManager loaderManager, ChatContract.View view, ChatsRepository repository) {
        mContext = CommonUtil.checkNotNull(context);
        mLoaderManager = CommonUtil.checkNotNull(loaderManager, "loader manager cannot be null!");
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mRepository =  CommonUtil.checkNotNull(repository, "repository cannot be null!");
        mView.setPresenter(this);
        currentId =  BmobUser.getCurrentUser(context).getObjectId();
        mSharedPreferences = context.getSharedPreferences(DataUtil.PREF_NAME,Context.MODE_PRIVATE);
        inputMethodHeight = mSharedPreferences.getInt(DataUtil.PREF_KEY_INPUT_METHOD_HEIGHT,0);
    }

    @Override
    public void start() {
        Log.i("info", "start: ");
        messageList = new ArrayList<>();
        imageList = new ArrayList<>();
        mView.setMessagesList(messageList);
        mView.setImageList(imageList);
        initRecordManager();
        mView.setVoiceBtListener(new VoiceTouchListener());
        mLoaderManager.initLoader(LOAD_MESSAGES,null,this);
    }

    @Override
    public void resume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(mContext).cancelNotification();
        BmobNotificationManager.getInstance(mContext).addObserver(this);
    }

    @Override
    public void pause() {
        BmobIM.getInstance().removeMessageListHandler(this);
        BmobNotificationManager.getInstance(mContext).removeObserver(this);
    }

    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(mContext).getNotificationCacheList();
        if(cache.size()>0){
            int size =cache.size();
            for(int i=0;i<size;i++){
                BmobIMMessage message = cache.get(i).getMessage();
                addMessage(message);
            }
        }
        mView.scrollToBottom();
    }

    @Override
    public Loader<List<BmobIMMessage>> onCreateLoader(int id, Bundle args) {
        Log.i("info", "onCreateLoader: ");
        mView.showLoading();
        MessagesLoader loader = new MessagesLoader(mContext,mRepository,mConversation);
        if(messageList != null && messageList.size()>0){
            loader.setMsg(messageList.get(0));
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<BmobIMMessage>> loader, List<BmobIMMessage> beanList) {
        Log.i("info", "onLoadFinished: ");
        if(beanList!=null && beanList.size()>0){
            if(messageList.size() > 0 && beanList.get(0).getId() == messageList.get(0).getId()){
//                此处判断是为了避免重复加载，比如当锁屏又解锁后会调用loadFinish，如果不处理就会重复添加数据
                return ;
            }
            for (int i = beanList.size()-1;i>=0;i--) {
                BmobIMMessage message = beanList.get(i);
                messageList.add(0,message);
                if(message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
                    boolean isSend = message.getFromId().equals( currentId);
                    final BmobIMImageMessage imageMessage = BmobIMImageMessage.buildFromDB(isSend, message);
                    final String content = TextUtils.isEmpty(imageMessage.getRemoteUrl()) ?
                            imageMessage.getLocalPath():imageMessage.getRemoteUrl();
                    imageList.add(0,content);
                }
            }
            mView.updateMessagesList();
            mView.updateImageList();
            if(messageList.size() <= MESSAGES_PER_PAGE){
//                消息数小于等于每页消息数时说明是第一次加载，需要滚动到底部
                mView.scrollToBottom();
            } else{
                mView.scrollToPosition(beanList.size());
            }
        }
        mView.hideLoading();
    }

    @Override
    public void onLoaderReset(Loader<List<BmobIMMessage>> loader) {
        LogUtil.i("onLoaderReset");
    }

    @Override
    public void stop() {
        saveInputMethodHeight();
//        更新所有消息为已读状态
        mConversation.updateLocalCache();
    }

    public void setConversation(BmobIMConversation mConversation) {
        this.mConversation = mConversation;
    }

    private void initRecordManager(){
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(mContext);
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

            @Override
            public void onVolumnChanged(int value) {
                mView.setVoiceLevel(value);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    mView.setVoiceBtClickable(false);
                    // 取消录音框
                    mView.hideVoiceLayout();
                    // 发送语音消息
                    sendMessage(localPath, MSG_TYPE_AUDIO);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mView.setVoiceBtClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtil.checkSdCard()) {
                        Toast.makeText(mContext, "SD卡不可用", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        mView.showVoiceLayout();
                        mView.setVoiceLayoutState(true);
                        // 开始录音
                        recordManager.startRecording(mConversation.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
//                    设手指移动在view的范围内时为正常状态，此时松开手指会发送。
//                    在view的范围外为异常状态，松开手指会取消发送。
                    boolean isNormal = event.getY() >= 0;
                    mView.setVoiceLayoutState(isNormal);

                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    mView.hideVoiceLayout();
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendMessage(recordManager.getRecordFilePath(mConversation.getConversationId()),MSG_TYPE_AUDIO);
                            } else {// 录音时间过短，则提示录音过短的提示
                                mView.showHintTooShort();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public void deleteMessage(int position) {
        messageList.remove(position);
        BmobIMMessage message = messageList.get(position);
        if(message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
            BmobIMImageMessage imageMessage = (BmobIMImageMessage) message;
            final String image = TextUtils.isEmpty(imageMessage.getRemoteUrl()) ?
                    imageMessage.getLocalPath():imageMessage.getRemoteUrl();
            imageList.remove(image);
            mView.updateImageList();
        }
        mConversation.deleteMessage(message);
        mView.updateMessagesList();
    }

    @Override
    public void addMessage(BmobIMMessage message) {
        if(message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
            boolean isSend = message.getFromId().equals( currentId);
            final BmobIMImageMessage imageMessage = BmobIMImageMessage.buildFromDB(isSend, message);
            final String content = TextUtils.isEmpty(imageMessage.getRemoteUrl()) ?
                    imageMessage.getLocalPath():imageMessage.getRemoteUrl();
            imageList.add(0,content);
            mView.updateImageList();
        }
        messageList.add(message);
        mView.updateMessagesList();
    }

    @Override
    public void sendMessage(String content, final int type) {
//        发送消息
        BmobIMMessage msg = null;
        switch (type){
            case MSG_TYPE_TXT:
                msg = new BmobIMTextMessage();
                msg.setContent(content);
                break;
            case MSG_TYPE_IMAGE_LOCAL:
                msg = new BmobIMImageMessage(content);
                break;
            case MSG_TYPE_IMAGE_REMOTE:
                msg = new BmobIMImageMessage();
                ((BmobIMImageMessage)msg).setRemoteUrl(content);
                break;
            case MSG_TYPE_AUDIO:
                msg = new BmobIMAudioMessage(content);
        }
        mConversation.sendMessage(msg, new MessageSendListener() {

            @Override
            public void onStart(BmobIMMessage msg) {
                super.onStart(msg);
                addMessage(msg);
                mView.scrollToBottom();
            }

            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                mView.updateMessagesList();
                if(type == MSG_TYPE_TXT){
                    mView.clearTextInputViewContent();
                    mView.hideSendButton();
                    mView.showMoreButton();
                }
                if (e != null) {
                    LogUtil.i(e.getMessage());
                }
            }
        });

    }

    @Override
    public void loadMessages() {
        mLoaderManager.restartLoader(LOAD_MESSAGES,null,this);
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
    public void showOrHideEmojiPanel(boolean showView) {
        if (showView) {
            mView.hideMorePanel();
            mView.setPanelHeight(inputMethodHeight);
            mView.setTextInputHighlight(true);
            setInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            mView.hideKeyboard();
            mView.showEmojiPanel();
            mView.scrollToBottom();
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
            mView.scrollToBottom();
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
        if (inputMethodHeight == 0) {
            inputMethodHeight = screenHeight - currentHeight;
            isInputMethodHeightDirty = true;
        }
        if(currentHeight < screenHeight){
//            当前高度小于屏幕高度说明处于输入状态，需要滚动到底部
            mView.scrollToBottom();

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

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        LogUtil.i("聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i=0;i<list.size();i++){
            addMessage(list.get(i).getMessage());
        }
        mView.scrollToBottom();
    }
}
