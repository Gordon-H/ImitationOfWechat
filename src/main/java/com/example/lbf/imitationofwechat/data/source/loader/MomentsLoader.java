package com.example.lbf.imitationofwechat.data.source.loader;

import android.content.Context;

import com.example.lbf.imitationofwechat.beans.MomentBean;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;

import java.util.List;

/**
 * Created by lbf on 2016/7/30.
 */
public class MomentsLoader extends BaseLoader<List<MomentBean>> {
    private int page = 0;
    public MomentsLoader(Context context, ChatsRepository repository) {
        super(context,repository);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage(){
        return page;
    }
    @Override
    public List<MomentBean> loadInBackground() {
        return mRepository.getMomentList(page);
    }

}
