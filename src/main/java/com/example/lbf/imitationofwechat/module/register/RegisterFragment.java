package com.example.lbf.imitationofwechat.module.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener,RegisterContract.View {

    private Context mContext;
    private RegisterContract.Presenter mPresenter;

    private EditText etUsername;
    private EditText etName;
    private EditText etPassword;
    private EditText etPasswordAgain;
    private Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register,container,false);
        mContext = container.getContext();
        etUsername = (EditText) root.findViewById(R.id.et_username);
        etName = (EditText) root.findViewById(R.id.et_name);
        etPassword = (EditText) root.findViewById(R.id.et_password);
        etPasswordAgain = (EditText) root.findViewById(R.id.et_password_again);
        btnRegister = (Button) root.findViewById(R.id.btn_register);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(mContext,errorMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                mPresenter.register(etUsername.getText().toString(),
                        etName.getText().toString(),
                        etPassword.getText().toString(),
                        etPasswordAgain.getText().toString());
                break;
        }
    }

    @Override
    public void clearPassword() {
        etPassword.setText("");
        etPasswordAgain.setText("");
    }
}
