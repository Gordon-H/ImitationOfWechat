package com.example.lbf.imatationofwechat.searchLocalAccount;

import com.example.lbf.imatationofwechat.BasePresenter;
import com.example.lbf.imatationofwechat.BaseView;
import com.example.lbf.imatationofwechat.beans.ContactBean;

import java.util.List;

/**
 * Created by lbf on 2016/7/28.
 */
public interface SearchLocalAccountContract {
    interface Presenter extends BasePresenter {
        void searchAccount(String name);
    }

    interface View extends BaseView<Presenter> {
        void setResultList(List<ContactBean> resultList);
        List<ContactBean> getAccountList();
    }
}
