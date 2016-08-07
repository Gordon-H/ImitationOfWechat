package com.example.lbf.imatationofwechat.shakeSetting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.DataUtil;
import com.example.lbf.imatationofwechat.chooseImage.ChooseImageActivity;

/**
 * Created by lbf on 2016/7/29.
 */
public class ShakeSettingPresenter implements ShakeSettingContract.Presenter {

    private Context mContext;
    private ShakeSettingContract.View mView;
    private SharedPreferences mSharedPreferences;
    private boolean useSound;
    private String imagePath;

    public ShakeSettingPresenter(Context context, ShakeSettingContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
        mSharedPreferences = context.getSharedPreferences(DataUtil.PREF_NAME,Context.MODE_PRIVATE);
    }

    @Override
    public void start() {
        useSound = mSharedPreferences.getBoolean(DataUtil.PREF_KEY_SHAKE_USE_SOUND,true);
        mView.setUseSoundSelected(useSound);
    }

    @Override
    public void saveBackgroundImage(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void pause() {
        boolean isSelected = mView.isUseSoundSelected();
        String savedImagePath = mSharedPreferences.getString(
                DataUtil.PREF_KEY_SHAKE_BACKGROUND,ShakeSettingActivity.DEFAULT_IMAGE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
//            不相同说明设置发生了变化，需要写回sharedPreference文件
        if(isSelected != useSound) {
            editor.putBoolean(DataUtil.PREF_KEY_SHAKE_USE_SOUND, isSelected);
        }
        if(imagePath != savedImagePath) {
            editor.putString(DataUtil.PREF_KEY_SHAKE_BACKGROUND,imagePath);
        }
        editor.commit();
    }

    @Override
    public void result(int requestCode, int resultCode, Intent result) {
        switch (requestCode){
            case ShakeSettingActivity.CODE_SELECT_IMAGE:
                if(resultCode == Activity.RESULT_OK){
//                    结果正常，取得照片的URL并保存
                    String[] image = result.getStringArrayExtra(ChooseImageActivity.KEY_CONTENT);
                    saveBackgroundImage(image[0]);
                }
                break;
        }
    }
}
