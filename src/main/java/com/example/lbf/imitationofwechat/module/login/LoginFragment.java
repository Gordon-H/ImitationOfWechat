package com.example.lbf.imitationofwechat.module.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.module.register.RegisterActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class LoginFragment extends Fragment implements View.OnClickListener,LoginContract.View {

    private Context mContext;
    private LoginContract.Presenter mPresenter;

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvResigter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login,container,false);
        mContext = container.getContext();
        etUsername = (EditText) root.findViewById(R.id.et_username);
        etPassword = (EditText) root.findViewById(R.id.et_password);
        btnLogin = (Button) root.findViewById(R.id.btn_login);
        tvResigter = (TextView) root.findViewById(R.id.tv_register);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnLogin.setOnClickListener(this);
        tvResigter.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String errorMsg) {
        LogUtil.i("error! "+errorMsg);
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                mPresenter.login(etUsername.getText().toString(),etPassword.getText().toString());
                break;
            case R.id.tv_register:
                registerAccount();
                break;
        }
    }

    private void registerAccount() {
        mContext.startActivity(new Intent(mContext,RegisterActivity.class));
    }
}
