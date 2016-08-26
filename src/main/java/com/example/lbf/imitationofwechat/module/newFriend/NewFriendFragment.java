package com.example.lbf.imitationofwechat.module.newFriend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imitationofwechat.adapter.NewFriendListAdapter;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.data.source.local.db.NewFriend;
import com.example.lbf.imitationofwechat.data.source.local.db.NewFriendManager;
import com.example.lbf.imitationofwechat.util.CommonUtil;

import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class NewFriendFragment extends BaseFragment implements NewFriendContract.View,OnItemClickListener {

    private NewFriendContract.Presenter mPresenter;

    RecyclerView rc_view;
    LinearLayoutManager layoutManager;
    private NewFriendListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_friend,container,false);
        rc_view = (RecyclerView) root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        NewFriendManager.getInstance(mContext).updateBatchStatus();
    }

    private void initView() {
        mAdapter =new NewFriendListAdapter(mContext);
        mAdapter.setOnItemClickListener(this);
        layoutManager = new LinearLayoutManager(mContext);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void setPresenter(NewFriendContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void setUserList(List<NewFriend> userList) {
        mAdapter.setList(userList);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(View v, int position) {
        toast("you click "+position);
    }
}
