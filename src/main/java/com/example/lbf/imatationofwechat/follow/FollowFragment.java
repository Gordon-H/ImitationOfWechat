package com.example.lbf.imatationofwechat.follow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imatationofwechat.Interface.OnItemLongClickListener;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.adapter.ContactsListAdapter;
import com.example.lbf.imatationofwechat.beans.ContactBean;
import com.example.lbf.imatationofwechat.search.SearchActivity;
import com.example.lbf.imatationofwechat.searchLocalAccount.SearchLocalAccountActivity;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.views.ContactsIndicatorView;
import com.example.lbf.imatationofwechat.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class FollowFragment extends Fragment implements FollowContract.View {

    private Context mContext;
    private FollowContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private ContactsIndicatorView mIndicatorView;
    private TextView mTextView;
    private List<ContactBean> contactBeanList;
    private ContactsListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout mProgressView;
    private String contactsGroupList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_contacts,container,false);
        mContext = container.getContext();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_contacts_list);
        mIndicatorView = (ContactsIndicatorView) root.findViewById(R.id.view_contacts_indicator);
        mTextView = (TextView) root.findViewById(R.id.tv_contacts_section);
        mProgressView = (LinearLayout) root.findViewById(R.id.ll_progress);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        contactBeanList = new ArrayList<>();
        mAdapter = new ContactsListAdapter(mContext, contactBeanList);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ContactBean contactBean = contactBeanList.get(position);
                showContact(contactBean.getId());
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
//                保存当前点击的位置
                ContactBean contactBean = contactBeanList.get(position);
                showEditMenu(v,contactBean);
                return false;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext));
        mIndicatorView.setCharList(contactsGroupList);
        mIndicatorView.setOnItemSelectedListener(new ContactsIndicatorView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                mPresenter.touchIndicator(position);
            }

            @Override
            public void onItemUnselected() {
                mPresenter.unTouchIndicator();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = mAdapter.getPosition();
        switch (item.getItemId()){
            case R.id.context_un_follow:
                mPresenter.unFollowAccount(contactBeanList.get(position).getId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void setContactsList(List<ContactBean> beanList) {
        contactBeanList.clear();
        contactBeanList.addAll(beanList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setIndexer(SectionIndexer indexer) {
        mAdapter.setIndexer(indexer);
    }

    @Override
    public List<ContactBean> getContactsList() {
        return contactBeanList;
    }

    @Override
    public void updateContactsList() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showContact(int contactId) {
        CommonUtil.showNoImplementText(mContext);
    }

    @Override
    public void showEditMenu(View v, final ContactBean contactBean) {
        v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0,R.id.context_un_follow,1,"不再关注");
            }
        });
    }

    @Override
    public void moveToPosition(int position) {
//        注意，这里不能直接用recyclerview的scrollToPosition方法，
//        该方法不能实现滚动到刚好把对应项展示在顶部位置的效果。
        mLayoutManager.scrollToPositionWithOffset(position,0);
    }

    @Override
    public void showSectionTv(int position) {
        mTextView.setText(contactsGroupList.substring(position,position+1));
        mTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSectionTv() {
        mTextView.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(FollowContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showLoading() {
        mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String errorMsg) {
        LogUtil.i("error! "+errorMsg);
    }

    @Override
    public void hideLoading() {
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_follow,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.menu_item_search:
                intent = new Intent(mContext, SearchLocalAccountActivity.class);
                Bundle bundle = new Bundle();
                ContactBean[] contactBeens = new ContactBean[contactBeanList.size()-1];
                contactBeanList.toArray(contactBeens);
                bundle.putParcelableArray("info",contactBeens);
                intent.putExtras(bundle);
                break;
            case R.id.menu_item_plus:
                intent = new Intent(mContext, SearchActivity.class);
                break;
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
