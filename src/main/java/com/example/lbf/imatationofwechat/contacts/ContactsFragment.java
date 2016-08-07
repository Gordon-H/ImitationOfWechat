package com.example.lbf.imatationofwechat.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imatationofwechat.Interface.OnItemLongClickListener;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.adapter.ContactsListAdapter;
import com.example.lbf.imatationofwechat.beans.ContactBean;
import com.example.lbf.imatationofwechat.follow.FollowActivity;
import com.example.lbf.imatationofwechat.views.ContactsIndicatorView;
import com.example.lbf.imatationofwechat.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ContactsFragment extends Fragment implements ContactsContract.View {

    //    默认的四项item的标题和图片
    public static final String[] DEFAULT_ITEM_NAME = new String[]
            {"新的朋友", "群聊", "标签", "公众号"};
    public static final int[] DEFAULT_ITEM_IMAGE = new int[]
            {R.drawable.default_fmessage, R.drawable.default_chatroom,
                    R.drawable.default_contactlabel, R.drawable.default_servicebrand_contact};
    public static final int ITEM_POSITION_NEW_FRIEND = 0;
    public static final int ITEM_POSITION_GROUP_CHAT = 1;
    public static final int ITEM_POSITION_LABEL = 2;
    public static final int ITEM_POSITION_OFFICIAL_ACCOUNTS = 3;
    private Context mContext;
    private ContactsContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private ContactsIndicatorView mIndicatorView;
    private TextView mTextView;
    private List<ContactBean> contactBeanList;
    private ContactsListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout mProgressView;
    private String contactsGroupList = "↑※ABCDEFGHIJKLMNOPQRSTUVWXYZ#";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_contacts,container,false);
        mContext = container.getContext();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_contacts_list);
        mIndicatorView = (ContactsIndicatorView) root.findViewById(R.id.view_contacts_indicator);
        mTextView = (TextView) root.findViewById(R.id.tv_contacts_section);
        mProgressView = (LinearLayout) root.findViewById(R.id.ll_progress);
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
                if(position < 4){
                    showDefaultItem(position);
                }else{
                    ContactBean contactBean = contactBeanList.get(position);
                    showContact(contactBean.getId());
                }
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
    public void onResume() {
        super.onResume();
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
            case R.id.context_set_remarks:
                setRemarksAndTag(contactBeanList.get(position).getId());
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
    public void showDefaultItem(int position) {
        switch (position){
            case ITEM_POSITION_OFFICIAL_ACCOUNTS:
                startActivity(new Intent(mContext, FollowActivity.class));
                break;
            case ITEM_POSITION_NEW_FRIEND:
            case ITEM_POSITION_GROUP_CHAT:
            case ITEM_POSITION_LABEL:
                CommonUtil.showNoImplementText(mContext);
        }
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
                menu.add(0,R.id.context_set_remarks,1,"设置备注及标签");
            }
        });
    }

    @Override
    public void setRemarksAndTag(int contactId) {
        CommonUtil.showNoImplementText(mContext);
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
    public void setPresenter(ContactsContract.Presenter presenter) {
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

}
