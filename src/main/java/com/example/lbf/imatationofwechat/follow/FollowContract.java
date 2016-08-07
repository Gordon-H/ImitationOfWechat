package com.example.lbf.imatationofwechat.follow;

import android.widget.SectionIndexer;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;
import com.example.lbf.imatationofwechat.beans.ContactBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface FollowContract {
    interface Presenter extends BasePresenter {
//        手指触摸右边的指示条
        void touchIndicator(int section);
//        手指离开右边的指示条
        void unTouchIndicator();

        void unFollowAccount(int accountId);
    }

    interface View extends BaseView<Presenter> {
//        设置联系人列表
        void setContactsList(List<ContactBean> beanList);
//        设置Indexer实现分组
        void setIndexer(SectionIndexer indexer);
//        获取联系人列表
        List<ContactBean> getContactsList();
//        更新聊天列表
        void updateContactsList();
//        点击item后展示内容
        void showContact(int contactId);
//        长按item后展开菜单进行编辑
        void showEditMenu(android.view.View v, ContactBean contactBean);
//        移动到对应的分组
        void moveToPosition(int position);
//        触摸指示条时显示中间的组名
        void showSectionTv(int position);
//        手指离开指示条时隐藏中间的组名
        void hideSectionTv();
    }
}
