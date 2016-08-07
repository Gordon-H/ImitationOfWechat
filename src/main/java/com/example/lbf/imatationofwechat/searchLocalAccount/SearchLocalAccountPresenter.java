package com.example.lbf.imatationofwechat.searchLocalAccount;

import android.content.Context;

import com.example.lbf.imatationofwechat.beans.ContactBean;
import com.example.lbf.imatationofwechat.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class SearchLocalAccountPresenter implements SearchLocalAccountContract.Presenter {
    private Context mContext;
    private SearchLocalAccountContract.View mView;
    private List<ContactBean> accountList;
    public SearchLocalAccountPresenter(Context context, SearchLocalAccountContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        accountList = mView.getAccountList();
    }

    @Override
    public void searchAccount(String text) {
        List<ContactBean> resultList = new ArrayList<>();
        for(int i = 0; i< accountList.size(); i++){
            ContactBean bean = accountList.get(i);
            if(bean.getName().contains(text.trim())){
                resultList.add(bean);
            }
        }
        mView.setResultList(resultList);
    }
}
