package com.example.lbf.imatationofwechat.imageDetail;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ImageDetailContract {
    interface Presenter extends BasePresenter {
//        发送图片
        void sendImages(List<String> selectImages);

        void changePage(int page);

        void back();

    }

    interface View extends BaseView<Presenter> {
//        设置图片列表
        void setImagesList(List<String> imageList);

        void addSelectedImage(String path);

        void removeSelectedImage(String path);

        List<String> getSelectedImagesList();

        boolean isContainImage(String path);

        String getImagePath(int position);

        void updateView(int size);

        void checkSelectImage();

        void unCheckSelectImage();

        void showStatusBar();

        void hideStatusBar();

    }
}
