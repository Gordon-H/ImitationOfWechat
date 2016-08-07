package com.example.lbf.imatationofwechat.imageDetail;

import android.app.Activity;
import android.content.Intent;

import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.chooseImage.ChooseImageActivity;
import com.example.lbf.imatationofwechat.data.source.local.WeChatPersistenceContract;

import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ImageDetailPresenter implements ImageDetailContract.Presenter {

    private static final int LOAD_IMAGES_SCAN = 1;

    private Activity mContext;
    private ImageDetailContract.View mView;

    public ImageDetailPresenter(Activity context, ImageDetailContract.View view) {
        mContext = CommonUtil.checkNotNull(context,"context cannot be null!");
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

     @Override
    public void sendImages(List<String> selectImages) {
        Intent intent = new Intent();
        saveImages(intent);
        intent.putExtra(ChooseImageActivity.KEY_ACTION,"quit");
        mContext.setResult(Activity.RESULT_OK,intent);
        mContext.finish();
    }

    private void saveImages(Intent intent) {
        List<String> selectedImages = mView.getSelectedImagesList();
        String[] contents = new String[selectedImages.size()];
        for(int i = 0;i<contents.length;i++){
            contents[i] = selectedImages.get(i);
        }
        intent.putExtra(ChooseImageActivity.KEY_CONTENT, contents);
        intent.putExtra(ChooseImageActivity.KEY_TYPE, WeChatPersistenceContract.MsgsEntry.MSG_TYPE_IMAGE);
    }

    @Override
    public void changePage(int position) {
        String path = mView.getImagePath(position);
        if(mView.isContainImage(path)){
            mView.checkSelectImage();
        }else{
            mView.unCheckSelectImage();
        }
    }

    @Override
    public void back() {
        Intent intent = new Intent();
        saveImages(intent);
        mContext.setResult(Activity.RESULT_OK,intent);
    }

}
