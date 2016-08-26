package com.example.lbf.imitationofwechat.module.chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.adapter.EmojiPagerAdapter;
import com.example.lbf.imitationofwechat.adapter.ImagePagerAdapter;
import com.example.lbf.imitationofwechat.adapter.MessagesListAdapter;
import com.example.lbf.imitationofwechat.base.BaseAdapter;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.base.BaseViewHolder;
import com.example.lbf.imitationofwechat.beans.BaseItemBean;
import com.example.lbf.imitationofwechat.module.chooseImage.ChooseImageActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.EmojiUtil;
import com.example.lbf.imitationofwechat.util.InputMethodUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;
import com.example.lbf.imitationofwechat.views.TextInputView;
import com.example.lbf.imitationofwechat.views.ZoomImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChatFragment extends BaseFragment implements ChatContract.View,View.OnClickListener {

    private static final int[] DRAWABLE_VOICE_LEVEL = {
            R.drawable.v1,R.drawable.v2,R.drawable.v3,
            R.drawable.v4,R.drawable.v5,R.drawable.v6,R.drawable.v7,} ;

    //    面板的最大高度
    private static int MAX_HEIGHT;
//    面板的最小高度
    private static int MIN_HEIGHT;
    private ChatContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private MessagesListAdapter adapter;
    private View root;
//    文字输入框
    private TextInputView textInputView;
//    语音输入时中间的布局
    private View layoutVoice;

    private ImageView ivVoiceState;
    private ImageView ivVoiceVolume;
    private TextView tvVoiceHint;
//    切换输入类型
    private ImageView ivChangeInputType;
//    显示“更多”
    private ImageView ivShowMore;
//    显示放大的图片
    private ViewPager imagePager;
//    表情面板
    private ViewPager emojiPanel;
//    语音输入按钮
    private Button btVoiceInput;
//    发送按钮
    private Button btSend;
//    “更多”面板
    private RecyclerView morePanel;
//    屏幕高度
    private ImagePagerAdapter imageAdapter;
    private ImageView scaleImageView;
    private LinearLayoutManager layoutManager;
    float startScale;
    final Rect startBounds = new Rect();
    final Rect finalBounds = new Rect();
    final Point globalOffset = new Point();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_chat,container,false);
        mContext = container.getContext();
        MAX_HEIGHT = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
        MIN_HEIGHT = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_chat_messageList);
        layoutVoice = root.findViewById(R.id.layout_voice);
        ivVoiceState = (ImageView) root.findViewById(R.id.iv_voiceState);
        ivVoiceVolume = (ImageView) root.findViewById(R.id.iv_voiceVolume);
        tvVoiceHint = (TextView) root.findViewById(R.id.tv_voiceHint);
        ivChangeInputType = (ImageView) root.findViewById(R.id.iv_chat_changeInputType);
        ivShowMore = (ImageView) root.findViewById(R.id.iv_chat_more);
        textInputView = (TextInputView) root.findViewById(R.id.tiv_chat_input);
        btVoiceInput = (Button) root.findViewById(R.id.bt_voiceInput);
        btSend = (Button) root.findViewById(R.id.bt_chat_send);
        emojiPanel = (ViewPager) root.findViewById(R.id.vp_chat_emojiPanel);
        morePanel = (RecyclerView) root.findViewById(R.id.recyclerView_chat_morePanel);
        imagePager = (ViewPager) getActivity().findViewById(R.id.vp_chat_imageDetail);
        setHasOptionsMenu(true);
        Log.i("info", "onCreateView: ");
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initMorePanelData();
        initEmojiPanelData();
        mPresenter.start();
        Log.i("info", "onActivityCreated: ");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chat_aty_menu, menu);
    }

    private void initView() {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPresenter.proposeLayoutChange(root);
            }
        });
        initRecyclerView();
        ivChangeInputType.setOnClickListener(this);
        ivShowMore.setOnClickListener(this);
        btSend.setOnClickListener(this);
        textInputView.setOnImageClickListener(new TextInputView.OnImageClickListener() {
            @Override
            public void onImageClick(boolean isImageHighlight) {
                if (!isImageHighlight) {
                    InputMethodUtil.showKeyboard(mContext,textInputView);
                }
                mPresenter.showOrHideEmojiPanel(isImageHighlight);
            }
        });
        textInputView.setOnTextStateChangeListener(new TextInputView.OnTextStateChangeListener() {
            @Override
            public void onTextStateChange(boolean isEmpty) {
                mPresenter.showOrHideSendButton(!isEmpty);
            }
        });
        imageAdapter = new ImagePagerAdapter(mContext);
        imageAdapter.setOnSingleTapListener(new ZoomImageView.OnSingleTapListener() {
            @Override
            public void onSingleTap() {
                hideImageDetail();
            }
        });
        imagePager.setAdapter(imageAdapter);
    }

    private void initRecyclerView() {
        adapter = new MessagesListAdapter(mContext);
        adapter.setOnImageTapListener(new MessagesListAdapter.OnImageTapListener() {
            @Override
            public boolean onImageTap(View view, String url) {
                showImageDetail((ImageView) view,url);
                return false;
            }
        });
        adapter.setOnItemDoubleTapListener(new MessagesListAdapter.OnItemDoubleTapListener() {
            @Override
            public boolean onItemDoubleTap(String content) {
                showMessageDetail(content);
                return true;
            }
        });
        mRecyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPos = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(adapter.getItemCount()>=ChatPresenter.MESSAGES_PER_PAGE && firstPos == 0){
                    mPresenter.loadMessages();
                }
            }
        });
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mPresenter.showOrHideEmojiPanel(false);
                    mPresenter.showOrHideMorePanel(false);
                    InputMethodUtil.hideKeyboard(mContext);
                    return true;
                }

                return false;
            }
        });

    }

    private void initEmojiPanelData() {
        List<RecyclerView> views = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            RecyclerView gridView = new RecyclerView(mContext);
            List<String> expressionList = new ArrayList<>();
            for (int j = i * 20; j < (i + 1) * 20; j++) {
                expressionList.add("smiley_" + j);
            }
            expressionList.add("emoji_delete");
            gridView.setLayoutManager(new GridLayoutManager(mContext, 7));
            gridView.setAdapter(new BaseAdapter<String>(mContext, expressionList, R.layout.chat_aty_emoji_grid_item) {
                @Override
                protected void convert(BaseViewHolder holder, final String s) {
                    Field field;
                    try {
                        field = R.drawable.class.getDeclaredField(s);
                        final int resourceId = Integer.parseInt(field.get(null).toString());
                        holder.setImageRes(R.id.iv_expression, resourceId)
                                .setOnClickLisener(R.id.iv_expression, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       mPresenter. addEmojiExpression(resourceId,s);
                                    }
                                });
                        ViewGroup root = (ViewGroup) holder.get(R.id.fl_chat_root);
                        ViewGroup.LayoutParams params = root.getLayoutParams();
                        params.height = emojiPanel.getHeight()/3;
                        root.setLayoutParams(params);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            views.add(gridView);
        }
        emojiPanel.setAdapter(new EmojiPagerAdapter(views));
    }

    private void initMorePanelData() {
        List<BaseItemBean> beanList = new ArrayList<>(10);
        String[] texts = {"图片", "小视频", "红包", "转账", "我的收藏"
                , "位置", "视频聊天", "名片", "语音输入", "卡券"};
        int[] images = {R.drawable.chat_aty_more1, R.drawable.chat_aty_more2,
                R.drawable.chat_aty_more3, R.drawable.chat_aty_more4,
                R.drawable.chat_aty_more5, R.drawable.chat_aty_more6,
                R.drawable.chat_aty_more7, R.drawable.chat_aty_more8,
                R.drawable.chat_aty_more9, R.drawable.chat_aty_more10};
        for (int i = 0; i < 10; i++) {
            BaseItemBean bean = new BaseItemBean(texts[i], images[i]);
            beanList.add(bean);
        }
        morePanel.setLayoutManager(new GridLayoutManager(mContext, 5));
        morePanel.setAdapter(new BaseAdapter<BaseItemBean>(mContext, beanList, R.layout.chat_aty_more_grid_item) {
            @Override
            protected void convert(final BaseViewHolder holder, BaseItemBean bean) {
                holder.setText(R.id.title, bean.getText())
                        .setImageRes(R.id.image, bean.getImage())
                        .setOnClickLisener(R.id.image, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showMorePanelItem(holder.getAdapterPosition());
                            }
                        });
                ViewGroup root = (ViewGroup) holder.get(R.id.fl_chat_root);
                ViewGroup.LayoutParams params = root.getLayoutParams();
                params.height = morePanel.getHeight()/2;
                root.setLayoutParams(params);
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i("info", "onStop: ");
        mPresenter.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("info", "onResume: ");
        mPresenter.resume();
    }


    @Override
    public void onStart() {
        super.onStart();
        LogUtil.i("onStart");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("info", "onDestroy: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("info", "onCreate: ");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getPosition();

        return super.onContextItemSelected(item);
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void setMessagesList(List<BmobIMMessage> beanList) {
        adapter.setList(beanList);
        updateMessagesList();
    }

    @Override
    public void setImageList(List<String> beanList) {
        imageAdapter.setList(beanList);
        updateImageList();
    }

    @Override
    public void updateMessagesList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateImageList() {
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEditMenu(View v, BmobIMMessage messageBean) {

    }

    @Override
    public void showMessageDetail(String content) {
        TextViewFragment fragment= new TextViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TextViewFragment.INTENT_KEY_INFO,content);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null).add(R.id.main,fragment).commit();
    }

    @Override
    public void showImageDetail(ImageView imageView, String path) {
        scaleImageView = imageView;
        int page = imageAdapter.getItemPosition(path);
        imagePager.setCurrentItem(page);
        imageView.getGlobalVisibleRect(startBounds);
        root.getGlobalVisibleRect(finalBounds, globalOffset);
        LogUtil.i("startBounds = "+startBounds.left+" , "+startBounds.top);
        LogUtil.i("finalBounds = "+finalBounds.left+" , "+finalBounds.top);
        LogUtil.i("globalOffset = "+globalOffset.x+" , "+globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        startScale = mPresenter.calculateStartScale(startBounds,finalBounds);
        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        imageView.setAlpha(0f);
        imagePager.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        imagePager.setPivotX(0f);
        imagePager.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(imagePager, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(imagePager, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(imagePager, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(imagePager, View.SCALE_Y,
                        startScale, 1f));
        set.setDuration(6000);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }

    @Override
    public void hideImageDetail() {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(imagePager, View.X, startBounds.left))
                .with(ObjectAnimator.ofFloat(imagePager,
                        View.Y,startBounds.top))
                .with(ObjectAnimator.ofFloat(imagePager,
                        View.SCALE_X, startScale))
                .with(ObjectAnimator.ofFloat(imagePager,
                        View.SCALE_Y, startScale));
        set.setDuration(6000);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scaleImageView.setAlpha(1f);
                imagePager.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                scaleImageView.setAlpha(1f);
                imagePager.setVisibility(View.GONE);
            }
        });
        set.start();
    }

    @Override
    public void setVoiceLevel(int level) {
        ivVoiceVolume.setImageResource(DRAWABLE_VOICE_LEVEL[level]);
    }

    @Override
    public void scrollToBottom() {
            mRecyclerView.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void scrollToPosition(int position) {
        layoutManager.scrollToPositionWithOffset(position,0);
    }

    @Override
    public void showVoiceLayout() {
        if(layoutVoice.getVisibility() == View.GONE){
            layoutVoice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideVoiceLayout() {
        if(layoutVoice.getVisibility() == View.VISIBLE){
            layoutVoice.setVisibility(View.GONE);
        }
    }

    @Override
    public void setVoiceBtListener(View.OnTouchListener listener) {
        btVoiceInput.setOnTouchListener(listener);
    }

    @Override
    public void setVoiceBtClickable(boolean isClickable) {
        btVoiceInput.setPressed(false);
        btVoiceInput.setClickable(isClickable);
    }

    @Override
    public void setVoiceLayoutState(boolean isNormal) {
        if(isNormal){
            tvVoiceHint.setText("手指上滑，取消发送");
            tvVoiceHint.setBackgroundColor(Color.TRANSPARENT);
            ivVoiceState.setImageResource(R.drawable.iv_chat_voice_recording);
            ivVoiceVolume.setVisibility(View.VISIBLE);
        }else{
            tvVoiceHint.setText("松开手指，取消发送");
            tvVoiceHint.setBackgroundColor(Color.RED);
            ivVoiceState.setImageResource(R.drawable.iv_chat_voice_cancel);
            ivVoiceVolume.setVisibility(View.GONE);
        }
    }

    @Override
    public void showHintTooShort() {
        layoutVoice.setVisibility(View.VISIBLE);
        tvVoiceHint.setText("录音时间太短");
        ivVoiceState.setImageResource(R.drawable.iv_chat_voice_too_short);
        ivVoiceVolume.setVisibility(View.GONE);
        layoutVoice.postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutVoice.setVisibility(View.GONE);
            }
        },1500);
    }

    @Override
    public void showSendButton() {
        if(btSend.getVisibility() == View.GONE){
            btSend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideSendButton() {
        if(btSend.getVisibility() == View.VISIBLE){
            btSend.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMoreButton() {
        if(ivShowMore.getVisibility() == View.GONE){
            ivShowMore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideMoreButton() {
        if(ivShowMore.getVisibility() == View.VISIBLE){
            ivShowMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmojiPanel() {
        if(emojiPanel.getVisibility() == View.GONE){
            emojiPanel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmojiPanel() {
        if(emojiPanel.getVisibility() == View.VISIBLE){
            emojiPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideEmojiPanelDelayed() {
//        这里要delayed执行否则依然会跳动，因为隐藏面板的同时要弹出键盘，
//        弹出键盘有个延迟，如果在弹出键盘前执行的话就会造成跳动
        emojiPanel.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideEmojiPanel();
                setTextInputHighlight(false);
                mPresenter.setInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        },200);
    }

    @Override
    public void showMorePanel() {
        if(morePanel.getVisibility() == View.GONE){
            morePanel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideMorePanel() {
        if(morePanel.getVisibility() == View.VISIBLE){
            morePanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideMorePanelDelayed() {
        morePanel.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideMorePanel();
                mPresenter.setInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        },200);
    }

    @Override
    public void showMorePanelItem(int position) {
        switch (position){
            case 0:
                Intent intent = new Intent(getActivity(), ChooseImageActivity.class);
                intent.putExtra(ChooseImageActivity.KEY_SELECT_MODE, ChooseImageActivity.MODE_MULTIPLE);
                getActivity().startActivityForResult(intent, ChatActivity.CODE_SELECT_IMAGES);
                break;

        }
    }

    @Override
    public void showVoiceInputView() {
        if(btVoiceInput.getVisibility() == View.GONE){
            btVoiceInput.setVisibility(View.VISIBLE);
            ivChangeInputType.setBackgroundResource(R.drawable.chat_bt_key_board_bg);
        }
    }

    @Override
    public void hideVoiceInputView() {
        if(btVoiceInput.getVisibility() == View.VISIBLE){
            btVoiceInput.setVisibility(View.GONE);
            ivChangeInputType.setBackgroundResource(R.drawable.chat_bt_sound_bg);
        }
    }

    @Override
    public void showTextInputView() {
        if(textInputView.getVisibility() == View.GONE){
            textInputView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideTextInputView() {
        if(textInputView.getVisibility() == View.VISIBLE){
            textInputView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showKeyboard() {
        InputMethodUtil.showKeyboard(mContext,textInputView);
    }

    @Override
    public void hideKeyboard() {
        InputMethodUtil.hideKeyboard(mContext);
    }

    @Override
    public boolean isTextInputViewEmpty() {
        return textInputView.getText().toString().isEmpty();
    }

    @Override
    public String getTextInputViewContent() {
        return textInputView.getText().toString();
    }

    @Override
    public void clearTextInputViewContent() {
        textInputView.setText("");
    }

    @Override
    public int getTextInputViewIndex() {
        return textInputView.getSelectionStart();
    }

    @Override
    public void setTextInputHighlight(boolean highlight) {
        textInputView.setImageHighlight(highlight);
    }

    @Override
    public void setPanelHeight(int height) {
        if (height > MAX_HEIGHT || height <= MIN_HEIGHT) {
            return;
        } else if (height != morePanel.getMeasuredHeight()) {
            ViewGroup.LayoutParams params = morePanel.getLayoutParams();
            params.height = height;
            morePanel.setLayoutParams(params);
            ViewGroup.LayoutParams params2 = emojiPanel.getLayoutParams();
            params2.height = height;
            emojiPanel.setLayoutParams(params2);
        }
    }

    @Override
    public void appendString(SpannableString string) {
        textInputView.append(string);
    }

    @Override
    public void deleteString(int index1, int index2) {
        textInputView.getText().delete(index1,index2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chat_changeInputType:
                boolean showVoiceInput = btVoiceInput.getVisibility() == View.GONE;
                mPresenter.showOrHideVoiceInput(showVoiceInput);
                break;
            case R.id.iv_chat_more:
                boolean showMorePanel = morePanel.getVisibility() == View.GONE;
                if (!showMorePanel) {
                    InputMethodUtil.showKeyboard(mContext,textInputView);
                }
                mPresenter.showOrHideMorePanel(showMorePanel);
                break;
            case R.id.bt_chat_send:
                mPresenter.sendMessage(textInputView.getText().toString(), ChatPresenter.MSG_TYPE_TXT);
                break;

        }
    }


    public static class TextViewFragment extends Fragment implements View.OnTouchListener{
        public static final String INTENT_KEY_INFO = "info";
        private TextView textView;
        public TextViewFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            textView = new TextView(getActivity());
            textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundColor(Color.WHITE);
            textView.setClickable(true);
            textView.setOnTouchListener(this);
            return textView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Bundle bundle = getArguments();
            String content = bundle.getString("info");
            SpannableString spannableString = EmojiUtil.convertString(getActivity(),content);
            textView.setText(spannableString);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP){
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        }
    }
}
