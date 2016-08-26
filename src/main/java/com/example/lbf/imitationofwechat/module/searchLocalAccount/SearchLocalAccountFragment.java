package com.example.lbf.imitationofwechat.module.searchLocalAccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.base.BaseAdapter;
import com.example.lbf.imitationofwechat.base.BaseViewHolder;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;
import com.example.lbf.imitationofwechat.views.DividerItemDecoration;
import com.example.lbf.imitationofwechat.views.Search_Input_View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class SearchLocalAccountFragment extends Fragment implements SearchLocalAccountContract.View {

    private Context mContext;
    private SearchLocalAccountContract.Presenter mPresenter;

    private Search_Input_View inputView;
    private RecyclerView mRecyclerView;
    private BaseAdapter adapter;
    private List<ContactBean> resultList = new ArrayList<>();
    private List<ContactBean> accountList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_local_account,container,false);
        mContext = container.getContext();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        inputView = (Search_Input_View) getActivity().findViewById(R.id.viewSearch);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        Parcelable[] pars = bundle.getParcelableArray("info");
        for(Parcelable par:pars){
            ContactBean bean = (ContactBean) par;
            accountList.add(bean);
        }
    }

    private void initView() {
        inputView.setOnTextChangeListener(new Search_Input_View.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if(text.isEmpty()){
                    mRecyclerView.setVisibility(View.GONE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mPresenter.searchAccount(text);
            }
        });
        adapter = new BaseAdapter<ContactBean>(mContext, resultList, R.layout.item_contacts_no_header) {
            @Override
            protected void convert(BaseViewHolder holder, ContactBean contactBean) {
                holder.setText(R.id.contact_item_name,contactBean.getName())
                        .setImage(R.id.contact_item_image,contactBean.getImage());
            }
        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(SearchLocalAccountContract.Presenter presenter) {
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
    public void setResultList(List<ContactBean> resultList) {
        this.resultList.clear();
        this.resultList.addAll(resultList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public List<ContactBean> getAccountList() {
        return accountList;
    }
}
