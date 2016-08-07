package com.example.lbf.imatationofwechat.shake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.shakeSetting.ShakeSettingActivity;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class ShakeFragment extends Fragment implements View.OnClickListener,ShakeContract.View {

    private Context mContext;
    private ShakeContract.Presenter mPresenter;
    private Toolbar toolbar;
    private ImageView ivHidden;
    private ImageView ivUp;
    private ImageView ivDown;
    private View viewPeople;
    private View viewMusic;
    private View viewTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shake,container,false);
        mContext = container.getContext();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolBar);
        ivHidden = (ImageView) root.findViewById(R.id.iv_shake_imageHidden);
        ivUp = (ImageView) root.findViewById(R.id.iv_shake_imageUp);
        ivDown = (ImageView) root.findViewById(R.id.iv_shake_imageDown);
        viewPeople = root.findViewById(R.id.ll_shake_people);
        viewMusic= root.findViewById(R.id.ll_shake_music);
        viewTv = root.findViewById(R.id.ll_shake_tv);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mPresenter.create();
    }

    private void initView() {
        viewPeople.setOnClickListener(this);
        viewMusic.setOnClickListener(this);
        viewTv.setOnClickListener(this);
        viewPeople.setSelected(true);
        ivHidden.post(new Runnable() {
            @Override
            public void run() {
                int imageHeight = ivDown.getHeight();
                ivUp.setTranslationY(-imageHeight/2);
                ivDown.setTranslationY(imageHeight/2);
                ViewGroup.LayoutParams params = ivHidden.getLayoutParams();
                params.height = imageHeight*2;
                ivHidden.setLayoutParams(params);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void setPresenter(ShakeContract.Presenter presenter) {
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_shake_people:
                changeSelection(0);
                break;
            case R.id.ll_shake_music:
                changeSelection(1);
                break;
            case R.id.ll_shake_tv:
                changeSelection(2);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_shake, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_setting:
                startActivity(new Intent(mContext, ShakeSettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeSelection(int position) {
        clearState();
        switch (position){
            case 0:
                toolbar.setTitle("摇一摇");
                viewPeople.setSelected(true);
                break;
            case 1:
                toolbar.setTitle("摇歌曲");
                viewMusic.setSelected(true);
                break;
            case 2:
                toolbar.setTitle("摇电视");
                viewTv.setSelected(true);
                break;
        }

    }

    @Override
    public void setHiddenBackground(Bitmap background) {
        ivHidden.setImageBitmap(background);
    }

    @Override
    public void setHiddenBackground(int background) {
        ivHidden.setImageResource(background);
    }

    @Override
    public void startAnimation(Animation animationUp, Animation animationDown) {
        ivUp.startAnimation(animationUp);
        ivDown.startAnimation(animationDown);
    }

    private void clearState() {
        viewPeople.setSelected(false);
        viewMusic.setSelected(false);
        viewTv.setSelected(false);
    }
}
