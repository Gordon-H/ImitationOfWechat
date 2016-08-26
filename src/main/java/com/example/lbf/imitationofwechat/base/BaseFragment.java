package com.example.lbf.imitationofwechat.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.lbf.imitationofwechat.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by lbf on 2016/8/15.
 */
public class BaseFragment extends Fragment {
    protected ProgressDialog progressDialog;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(Boolean empty){

    }


    public void toast(final String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public <T> void startActivity(Class<T> clazz, Bundle bundle) {
        Intent intent = new Intent(getContext(),clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public <T> void startActivityForResult(Class<T> clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getContext(),clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    public void log(String msg){
        LogUtil.i("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        LogUtil.i(msg);
        LogUtil.i("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    public void showLoading() {
        progressDialog = ProgressDialog.show(mContext,null,"正在加载...");
    }

    public void showError(String errorMsg) {
        LogUtil.i("error! " + errorMsg);
    }

    public void hideLoading() {
        progressDialog.dismiss();
    }

}
