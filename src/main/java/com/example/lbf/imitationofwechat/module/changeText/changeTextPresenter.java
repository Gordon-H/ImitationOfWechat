package com.example.lbf.imitationofwechat.module.changeText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.lbf.imitationofwechat.util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class changeTextPresenter implements changeTextContract.Presenter {
    private Activity mContext;
    private changeTextContract.View mView;

    public changeTextPresenter(Activity context, changeTextContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        Bundle bundle = mContext.getIntent().getExtras();
        mView.setContent(bundle.getString(changeTextActivity.KEY_CONTENT1),
                bundle.getString(changeTextActivity.KEY_CONTENT2));
        mView.setTitle(bundle.getString(changeTextActivity.KEY_TITLE));
        mView.setHint(bundle.getString(changeTextActivity.KEY_HINT1),
                bundle.getString(changeTextActivity.KEY_HINT2));
    }

    @Override
    public void saveText(String content1,String content2) {
        Intent intent = new Intent();
        intent.putExtra(changeTextActivity.KEY_CONTENT1,content1);
        intent.putExtra(changeTextActivity.KEY_CONTENT2,content2);
        mContext.setResult(Activity.RESULT_OK,intent);
        mContext.finish();
    }
}
