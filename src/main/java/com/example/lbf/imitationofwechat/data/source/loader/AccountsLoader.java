package com.example.lbf.imitationofwechat.data.source.loader;

import android.content.Context;

import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;

import java.util.List;

/**
 * Created by lbf on 2016/7/30.
 */
public class AccountsLoader extends BaseLoader<List<ContactBean>> {

    public AccountsLoader(Context context, ChatsRepository repository) {
        super(context,repository);
    }

    @Override
    public List<ContactBean> loadInBackground() {
        return mRepository.getAccountsList();
    }
}
