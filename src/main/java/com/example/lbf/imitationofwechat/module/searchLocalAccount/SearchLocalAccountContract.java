package com.example.lbf.imitationofwechat.module.searchLocalAccount;

import com.example.lbf.imitationofwechat.BasePresenter;
import com.example.lbf.imitationofwechat.BaseView;
import com.example.lbf.imitationofwechat.beans.ContactBean;

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
