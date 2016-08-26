package com.example.lbf.imitationofwechat.module.changeText;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class changeTextFragment extends BaseFragment implements View.OnClickListener,changeTextContract.View {

    private changeTextContract.Presenter mPresenter;

    private EditText etContent1;
    private TextView tvHint1;
    private EditText etContent2;
    private TextView tvHint2;
    private Button btSave;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_text,container,false);
        etContent1 = (EditText) root.findViewById(R.id.etContent1);
        etContent2 = (EditText) root.findViewById(R.id.etContent2);
        tvHint1 = (TextView) root.findViewById(R.id.tvHint1);
        tvHint2 = (TextView) root.findViewById(R.id.tvHint2);
        btSave = (Button) getActivity().findViewById(R.id.btSave);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolBar);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btSave.setOnClickListener(this);
        mPresenter.start();
    }

    @Override
    public void setPresenter(changeTextContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSave:
                mPresenter.saveText(etContent1.getText().toString(),etContent2.getText().toString());
                break;
        }
    }

    @Override
    public void setHint(String hint1,String hint2) {
        tvHint1.setText(hint1);
        if(hint2!=null){
            tvHint2.setText(hint2);
        }
    }

    @Override
    public void setContent(String content1,String content2) {
        etContent1.setText(content1);
        if(content2!=null){
            etContent2.setText(content2);
        }else{
            etContent2.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }
}
