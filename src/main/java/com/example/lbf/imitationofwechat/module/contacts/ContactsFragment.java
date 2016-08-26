package com.example.lbf.imitationofwechat.module.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imitationofwechat.Interface.OnItemLongClickListener;
import com.example.lbf.imitationofwechat.adapter.ContactsListAdapter;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.module.follow.FollowActivity;
import com.example.lbf.imitationofwechat.module.newFriend.NewFriendActivity;
import com.example.lbf.imitationofwechat.module.userInfo.UserInfoActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.views.ContactsIndicatorView;
import com.example.lbf.imitationofwechat.views.DividerItemDecoration;

import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ContactsFragment extends BaseFragment implements ContactsContract.View {

    //    默认的四项item的标题和图片
    public static final String[] DEFAULT_ITEM_NAME = new String[]
            {"新的朋友", "群聊", "标签", "公众号"};
    public static final String[] DEFAULT_ITEM_IMAGE = new String[]
            {"drawable://"+R.drawable.default_fmessage,
                    "drawable://"+R.drawable.default_chatroom,
                    "drawable://"+R.drawable.default_contactlabel,
                    "drawable://"+R.drawable.default_servicebrand_contact};
    public static final int ITEM_POSITION_NEW_FRIEND = 0;
    public static final int ITEM_POSITION_GROUP_CHAT = 1;
    public static final int ITEM_POSITION_LABEL = 2;
    public static final int ITEM_POSITION_OFFICIAL_ACCOUNTS = 3;

    private ContactsContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private ContactsIndicatorView mIndicatorView;
    private TextView mTextView;

    private ContactsListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout mProgressView;
    private String contactsGroupList = "↑※ABCDEFGHIJKLMNOPQRSTUVWXYZ#";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_contacts,container,false);
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
        mAdapter = new ContactsListAdapter(mContext);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(position < 4){
                    showDefaultItem(position);
                }else{
                    ContactBean contact = mAdapter.getItem(position);
                    showContact(contact);
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
//                保存当前点击的位置
                ContactBean contactBean = mAdapter.getItem(position);
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
            case R.id.context_set_remarks:
                setRemarksAndTag(mAdapter.getItem(position));
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void setContactsList(List<ContactBean> beanList) {
        mAdapter.setList(beanList);
        mAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(mContext, NewFriendActivity.class));
                break;
            case ITEM_POSITION_GROUP_CHAT:
            case ITEM_POSITION_LABEL:
                CommonUtil.showNoImplementText(mContext);
        }
    }

    @Override
    public void showContact(ContactBean contactBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(UserInfoActivity.KEY_CONTACT, contactBean);
        bundle.putBoolean(UserInfoActivity.KEY_IS_FRIEND, true);
        startActivity(UserInfoActivity.class,bundle);
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
    public void setRemarksAndTag(ContactBean user) {
        CommonUtil.showNoImplementText(mContext);
    }

    @Override
    public void moveToGroup(int group) {
//        注意，这里不能直接用recyclerview的scrollToPosition方法，
//        该方法不能实现滚动到刚好把对应项展示在顶部位置的效果。
        mLayoutManager.scrollToPositionWithOffset(mAdapter.getGroupPosition(group),0);
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
        log("error! "+errorMsg);
    }

    @Override
    public void hideLoading() {
        mProgressView.setVisibility(View.GONE);
    }

}
