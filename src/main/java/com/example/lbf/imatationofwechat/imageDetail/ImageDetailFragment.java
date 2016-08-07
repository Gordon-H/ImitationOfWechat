package com.example.lbf.imatationofwechat.imageDetail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.adapter.ImagePagerAdapter;
import com.example.lbf.imatationofwechat.chooseImage.ChooseImageActivity;
import com.example.lbf.imatationofwechat.views.ZoomImageView;

import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ImageDetailFragment extends Fragment implements ImageDetailContract.View, View.OnClickListener, ZoomImageView.OnSingleTapListener {

    private Context mContext;
    private ImageDetailContract.Presenter mPresenter;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private RelativeLayout rlBottomBar;
    private Button btSend;
    private CheckBox cbRawImage;
    private CheckBox cbChooseImage;
    private ImagePagerAdapter adapter;
    private List<String> imageList;
    private List<String> selectedImageList;
    private View decorView;
    private ProgressDialog mProgressView;

    //    状态栏是否可见
    private boolean isStatusBarVisible = true;
    private int selectMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_detail,container,false);
        mContext = container.getContext();
        mToolbar = (Toolbar) root.findViewById(R.id.toolBar);
        mViewPager = (ViewPager) root.findViewById(R.id.viewPager);
        rlBottomBar = (RelativeLayout) root.findViewById(R.id.rlBottomBar);
        cbRawImage = (CheckBox) root.findViewById(R.id.tvRawImage);
        cbChooseImage = (CheckBox) root.findViewById(R.id.cbChooseImage);
        btSend = (Button) root.findViewById(R.id.btSend);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        btSend.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mPresenter.changePage(position);
            }
        });
        if(selectMode == ChooseImageActivity.MODE_SINGLE){
            cbChooseImage.setVisibility(View.INVISIBLE);
            return;
        }
        cbChooseImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(selectedImageList.size()>=9 && !cbChooseImage.isChecked()){
//                    选择图片满九张时通过返回true拦截点击事件
                        Toast.makeText(mContext,"你最多只能选择9张图片",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
        cbChooseImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String path = adapter.getImagePath(mViewPager.getCurrentItem());
                if(isChecked){
                    addSelectedImage(path);
                }else{
                    removeSelectedImage(path);
                }
                updateView(selectedImageList.size());
            }
        });
        initStatusBar();
    }

    private void initStatusBar() {
        decorView = getActivity().getWindow().getDecorView();
//        初始时显示状态栏，设置全屏模式
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        获取状态栏高度，以改变toolbar的位置
//        onCreate中无法直接获取，这里使用post的方法等加载完毕后再获取
        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int statusHeight = rect.top;
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
                params.topMargin = statusHeight;
                mToolbar.setLayoutParams(params);
            }
        });
    }

    private void initData() {
        Intent intent = getActivity().getIntent();
        String currentFolder = intent.getStringExtra(ChooseImageActivity.KEY_CURRENT_FOLDER);
        selectMode = intent.getIntExtra(ChooseImageActivity.KEY_SELECT_MODE,ChooseImageActivity.MODE_SINGLE);
        imageList = intent.getStringArrayListExtra(ChooseImageActivity.KEY_NAME_LIST);
        selectedImageList = intent.getStringArrayListExtra(ChooseImageActivity.KEY_SELECTED_LIST);
        int position = intent.getIntExtra(ChooseImageActivity.KEY_POSITION, 0);
        if (currentFolder == null) {
            adapter = new ImagePagerAdapter(mContext,imageList);
        } else {
//            减去第一项"拍摄照片"
            position -= 1;
            imageList.remove(0);
            adapter = new ImagePagerAdapter(mContext,currentFolder, imageList);
        }
        adapter.setOnSingleTapListener(this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ImageDetailContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showLoading() {
        mProgressView = ProgressDialog.show(mContext,null,"正在加载...");
    }

    @Override
    public void showError(String errorMsg) {
        LogUtil.i("error! "+errorMsg);
    }

    @Override
    public void hideLoading() {
        mProgressView.dismiss();
    }

    @Override
    public void setImagesList(List<String> imageList) {
        this.imageList.clear();
//        第一项显示“拍摄照片”
        this.imageList.addAll(imageList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addSelectedImage(String path) {
        selectedImageList.add(path);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeSelectedImage(String path) {
        selectedImageList.remove(path);
        adapter.notifyDataSetChanged();

    }

    @Override
    public List<String> getSelectedImagesList() {
        return selectedImageList;
    }

    @Override
    public boolean isContainImage(String path) {
        return selectedImageList.contains(path);
    }

    @Override
    public String getImagePath(int position) {
        return adapter.getImagePath(position);
    }

    @Override
    public void updateView(int size) {
        if(size == 0){
            btSend.setText("发送");
        }else{
            btSend.setText("发送" + "(" + size + "/9)");
        }
    }

    @Override
    public void checkSelectImage() {
        cbChooseImage.setChecked(true);
    }

    @Override
    public void unCheckSelectImage() {
        cbChooseImage.setChecked(false);

    }

    @Override
    public void showStatusBar() {
        final int duration = 300;
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        rlBottomBar.animate().alpha(0.9f).setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        rlBottomBar.setVisibility(View.VISIBLE);
                    }
                }).start();
        mToolbar.animate().translationYBy(mToolbar.getHeight()).setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mToolbar.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    @Override
    public void hideStatusBar() {
        final int duration = 300;
        rlBottomBar.animate().alpha(0).setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        rlBottomBar.setVisibility(View.GONE);
                    }
                }).start();
        mToolbar.animate().translationYBy(-mToolbar.getHeight()).setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mToolbar.setVisibility(View.GONE);
                        decorView.setSystemUiVisibility(
//                                注意这里要设置immersive模式才能在隐藏status bar的情况下切换图片
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
                    }
                }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSend:
                mPresenter.sendImages(selectedImageList);
                break;

        }
    }

    @Override
    public void onSingleTap() {
        if (isStatusBarVisible) {
            hideStatusBar();
        } else {
            showStatusBar();
        }
        isStatusBarVisible = !isStatusBarVisible;
    }
}
