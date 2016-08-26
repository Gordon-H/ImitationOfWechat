package com.example.lbf.imitationofwechat.module.searchUser;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imitationofwechat.adapter.UserListAdapter;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.module.userInfo.UserInfoActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class SearchUserFragment extends BaseFragment implements View.OnClickListener,SearchUserContract.View,OnItemClickListener {

    private Context mContext;
    private SearchUserContract.Presenter mPresenter;

    EditText etSearchUsername;
    SwipeRefreshLayout sw_refresh;
    Button btn_search;
    RecyclerView rc_view;
    LinearLayoutManager layoutManager;
    private UserListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_user,container,false);
        mContext = container.getContext();
        etSearchUsername = (EditText) root.findViewById(R.id.et_searchUsername);
        sw_refresh = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        btn_search = (Button) root.findViewById(R.id.btn_search);
        rc_view = (RecyclerView) root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initView() {
        btn_search.setOnClickListener(this);
        mAdapter =new UserListAdapter(mContext);
        mAdapter.setOnItemClickListener(this);
        layoutManager = new LinearLayoutManager(mContext);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(mAdapter);
        sw_refresh.setEnabled(false);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.search(etSearchUsername.getText().toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(SearchUserContract.Presenter presenter) {
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
            case R.id.btn_search:
                setRefresh(true);
                mPresenter.search(etSearchUsername.getText().toString());
                break;
        }
    }

    @Override
    public void setRefresh(boolean isRefresh) {
        sw_refresh.setRefreshing(isRefresh);
    }

    @Override
    public void setUserList(List<User> userList) {
        mAdapter.setList(userList);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(View v, int position) {
        Bundle bundle = new Bundle();
        User user = mAdapter.getItem(position);
        bundle.putParcelable(UserInfoActivity.KEY_CONTACT, new ContactBean(user));
        bundle.putBoolean(UserInfoActivity.KEY_IS_FRIEND, false);
        startActivity(UserInfoActivity.class,bundle);
    }
}
