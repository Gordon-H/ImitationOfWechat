package com.example.lbf.imatationofwechat.shakeSetting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.chooseImage.ChooseImageActivity;

/**
 * Created by lbf on 2016/7/29.
 */
public class ShakeSettingFragment extends Fragment implements View.OnClickListener,ShakeSettingContract.View {

    private Context mContext;
    private ShakeSettingContract.Presenter mPresenter;

    private TextView tvSetDefaultImage;
    private TextView tvChangeImage;
    private TextView tvPeople;
    private TextView tvMessage;
    private TextView tvHistory;
    private ToggleButton tgBtnSound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shake_setting,container,false);
        mContext = container.getContext();
        tvSetDefaultImage = (TextView) root.findViewById(R.id.tv_shakeSetting_setDefaultImage);
        tvChangeImage = (TextView) root.findViewById(R.id.tv_shakeSetting_changeImage);
        tvPeople = (TextView) root.findViewById(R.id.tv_shakeSetting_people);
        tvMessage = (TextView) root.findViewById(R.id.tv_shakeSetting_message);
        tvHistory = (TextView) root.findViewById(R.id.tv_shakeSetting_history);
        tgBtnSound = (ToggleButton) root.findViewById(R.id.tgBtn_sound);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initView() {
        tvSetDefaultImage.setOnClickListener(this);
        tvChangeImage.setOnClickListener(this);
        tvPeople.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        tgBtnSound.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ShakeSettingContract.Presenter presenter) {
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
            case R.id.tv_shakeSetting_setDefaultImage:
                mPresenter.saveBackgroundImage(ShakeSettingActivity.DEFAULT_IMAGE);
                Toast.makeText(mContext,"已恢复默认背景",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_shakeSetting_changeImage:
                Intent intent = new Intent(mContext, ChooseImageActivity.class);
                intent.putExtra(ChooseImageActivity.KEY_SELECT_MODE, ChooseImageActivity.MODE_SINGLE);
                startActivityForResult(intent, ShakeSettingActivity.CODE_SELECT_IMAGE);
                break;
            case R.id.tgBtn_sound:
                tgBtnSound.setSelected(!tgBtnSound.isSelected());
                break;
            default:
                CommonUtil.showNoImplementText(mContext);
        }
    }

    @Override
    public void setUseSoundSelected(boolean isSelected) {
        tgBtnSound.setSelected(isSelected);
    }

    @Override
    public boolean isUseSoundSelected() {
        return tgBtnSound.isSelected();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode,resultCode,data);
    }
}
