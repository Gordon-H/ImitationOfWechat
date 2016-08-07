package com.example.lbf.imatationofwechat.chooseImage;

import android.content.Intent;
import android.widget.PopupWindow;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ChooseImageContract {
    interface Presenter extends BasePresenter {
//        发送图片
        void sendImages(List<String> selectImages);

        void createSelectFolderWindow();

        void selectFolder(int position);

        void result(int requestCode, int resultCode, Intent data);

    }

    interface View extends BaseView<Presenter> {
//        设置图片列表
        void setImagesList(List<String> nameList);

        void setSelectedImagesList(String[] selectedImagesList);

        void setCurrentFolder(String path);
        //        显示选择文件夹的popupwindow
        void setSelectFolderWindow(PopupWindow popupWindow);
        void showSelectFolderWindow();
        //        点击图片后显示大图
        void showItem(int position);
        //        拍摄照片
        void showTakePhoto();
    }
}
