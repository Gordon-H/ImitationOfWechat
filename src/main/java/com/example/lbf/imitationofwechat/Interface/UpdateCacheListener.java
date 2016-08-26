package com.example.lbf.imitationofwechat.Interface;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

/**
 * @author :smile
 * @project:UpdateCacheListener
 * @date :2016-02-01-16:23
 */
public abstract class UpdateCacheListener extends BmobListener {
    public abstract void done(BmobException e);

    @Override
    protected void postDone(Object o, BmobException e) {
        done(e);
    }
}
