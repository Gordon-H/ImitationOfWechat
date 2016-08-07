package com.example.lbf.imatationofwechat.moments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.adapter.EmojiPagerAdapter;
import com.example.lbf.imatationofwechat.adapter.MomentsListAdapter;
import com.example.lbf.imatationofwechat.beans.MomentBean;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.InputMethodUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.views.CustomProgressDrawable;
import com.example.lbf.imatationofwechat.views.CustomSwipeRefreshLayout;
import com.example.lbf.imatationofwechat.views.DividerItemDecoration;
import com.example.lbf.imatationofwechat.views.TextInputView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class MomentsFragment extends Fragment implements MomentsContract.View, MomentsListAdapter.OnCommentStateListener, MomentsListAdapter.OnContactClickListener {

    private Context mContext;
    private MomentsContract.Presenter mPresenter;

    private static final int MAX_HEIGHT = 1000;
    private CustomSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View bottomLayout;
    private ImageButton imgBtNew;
    private TextInputView textInputView;
    private Button btSend;
    private ViewPager emojiPanel;
    private MomentsListAdapter adapter;
    private ArrayList<MomentBean> beanList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_moments,container,false);
        mContext = container.getContext();
        imgBtNew = (ImageButton) root.findViewById(R.id.imgBt_new);
        mRefreshLayout = (CustomSwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        bottomLayout = root.findViewById(R.id.rlBottomBar);
        textInputView = (TextInputView) root.findViewById(R.id.viewInputText);
        btSend = (Button) root.findViewById(R.id.btSend);
        emojiPanel = (ViewPager) root.findViewById(R.id.vpPanelEmoji);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        mPresenter.setEmojiPanelData();
        textInputView.setOnImageClickListener(new TextInputView.OnImageClickListener() {
            @Override
            public void onImageClick(boolean isImageHighlight) {
                if (!isImageHighlight) {
                    showKeyboard();
                }
                mPresenter.showOrHideEmojiPanel(isImageHighlight);
            }
        });
        textInputView.setOnImageClickListener(new TextInputView.OnImageClickListener() {
            @Override
            public void onImageClick(boolean isImageHighlight) {
                if (!isImageHighlight) {
                    showKeyboard();
                }
                mPresenter.showOrHideEmojiPanel(isImageHighlight);
            }
        });
    }

    private void initView() {
        beanList = new ArrayList<>();
        adapter = new MomentsListAdapter(mContext, beanList);
        adapter.setOnCommentStateListener(this);
        adapter.setOnContactClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(bottomLayout.getVisibility() == View.VISIBLE){
                        bottomLayout.setVisibility(View.GONE);
                        mPresenter.showOrHideEmojiPanel(false);
                        hideKeyboard();
                        return true;
                    }
                }
                return false;
            }
        });
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addComment(adapter.getTargetCommentView());
            }
        });
        CustomProgressDrawable drawable = new CustomProgressDrawable(mContext,mRefreshLayout);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.moments_refresh_icon);
        drawable.setBitmap(bitmap);
        mRefreshLayout.setProgressView(drawable);
        mRefreshLayout.setBackgroundColor(Color.BLACK);
        mRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK);
        mRefreshLayout.setOnRefreshListener(new CustomSwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                mPresenter.loadData(1);
                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mRefreshLayout.setRefreshing(false);
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(MomentsContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String errorMsg) {
        LogUtil.i("error! "+errorMsg);
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public boolean onCommentStart(int targetContactId) {
        mPresenter.startComment(targetContactId);
        return false;
    }

    @Override
    public void setMomentList(List<MomentBean> beanList) {
        this.beanList.clear();
        this.beanList.addAll(beanList);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void addMomentList(List<MomentBean> beanList) {
        this.beanList.addAll(beanList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setPanelAdapter(EmojiPagerAdapter adapter) {
        emojiPanel.setAdapter(adapter);
    }

    @Override
    public void setPanelHeight(int height) {
        if (height > MAX_HEIGHT || height <= 0) {
            return;
        } else if (height != emojiPanel.getMeasuredHeight()) {
            ViewGroup.LayoutParams params = emojiPanel.getLayoutParams();
            params.height = height;
            emojiPanel.setLayoutParams(params);
        }
    }

    @Override
    public void showBottomBar() {
        if(bottomLayout.getVisibility() == View.GONE){
            bottomLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideBottomBar() {
        if(bottomLayout.getVisibility() == View.VISIBLE){
            bottomLayout.setVisibility(View.GONE);
        }
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
        emojiPanel.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideEmojiPanel();
                textInputView.setImageHighlight(false);
                mPresenter.setInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        },200);
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
    public boolean onContactClick(int id) {
        mPresenter.clickContact(id);
        return false;
    }

    @Override
    public void appendString(SpannableString string) {
        textInputView.append(string);
    }

    @Override
    public void deleteString(int index1, int index2) {
        textInputView.getText().delete(index1,index2);
    }
}
