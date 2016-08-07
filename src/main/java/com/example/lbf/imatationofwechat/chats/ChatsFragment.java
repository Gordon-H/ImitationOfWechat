package com.example.lbf.imatationofwechat.chats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.lbf.imatationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imatationofwechat.Interface.OnItemLongClickListener;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.DataUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.adapter.ChatsListAdapter;
import com.example.lbf.imatationofwechat.beans.ChatBean;
import com.example.lbf.imatationofwechat.data.source.local.WeChatDBHelper;
import com.example.lbf.imatationofwechat.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChatsFragment extends Fragment implements ChatsContract.View {

    private Context mContext;
    private ChatsContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private List<ChatBean> chatBeanList;
    private ChatsListAdapter adapter;
    private boolean isDirty = false;
    private LinearLayout mProgressView;
//    置顶聊天的数目
    private int onTopNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_chats,container,false);
        mContext = container.getContext();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_chats_list);
        mProgressView = (LinearLayout) root.findViewById(R.id.ll_progress);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(DataUtil.PREF_NAME,Context.MODE_PRIVATE);
        onTopNumber = sharedPreferences.getInt(DataUtil.PREF_KEY_ON_TOP_NUMBER,0);
        chatBeanList = new ArrayList<>();
        adapter = new ChatsListAdapter(mContext,chatBeanList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ChatBean chatBean = chatBeanList.get(position);
                if(chatBean.getType() == WeChatDBHelper.CHAT_TYPE_CONTACT){
                    showChat(chatBean.getId());
                }
            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                ChatBean chatBean = chatBeanList.get(position);
                showEditMenu(v,chatBean);
                return false;
            }
        });
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
    public void onStop() {
        super.onStop();
        if (isDirty) {
            isDirty = false;
            mPresenter.saveChatsList(chatBeanList,onTopNumber);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getPosition();
        isDirty = true;
        switch (item.getItemId()){
            case R.id.context_delete_chat:
                deleteChats(position);
                break;
            case R.id.context_set_on_top:
                setOnTop(position);
                break;
            case R.id.context_unset_on_top:
                unsetOnTop(position);
                break;
            case R.id.context_un_follow:
                unFollowAccount(position);
                break;
        }
        adapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    @Override
    public void setChatsList(List<ChatBean> beanList) {
        chatBeanList.clear();
        chatBeanList.addAll(beanList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public List<ChatBean> getChatsList() {
        return chatBeanList;
    }

    @Override
    public void updateChatsList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteChats(int position) {
        chatBeanList.remove(position);
        if(position<onTopNumber){
            onTopNumber--;
        }
    }

    @Override
    public void setOnTop(int position) {
        ChatBean bean = chatBeanList.get(position);
        bean.setOnTop(true);
        chatBeanList.remove(position);
        chatBeanList.add(0,bean);
        onTopNumber++;
    }

    @Override
    public void unsetOnTop(int position) {
        ChatBean bean = chatBeanList.get(position);
        bean.setOnTop(false);
        long time = bean.getTime();
        chatBeanList.remove(position);
        onTopNumber--;
        int i;
        for(i = onTopNumber;i<chatBeanList.size();i++){
            long t = chatBeanList.get(i).getTime();
            if(time <= t){
                chatBeanList.add(i,bean);
                break;
            }
        }
        if(i == chatBeanList.size()){
            chatBeanList.add(bean);
        }
    }

    @Override
    public void unFollowAccount(int position) {
        ChatBean bean = chatBeanList.get(position);
        chatBeanList.remove(position);
        mPresenter.deleteAccount(bean.getId());
        if(position<onTopNumber){
            onTopNumber--;
        }
    }

    @Override
    public void showChat(int contactId) {
        Intent intent = new Intent(mContext, com.example.lbf.imatationofwechat.chat.ChatActivity.class);
        intent.putExtra(com.example.lbf.imatationofwechat.chat.ChatActivity.INTENT_KEY_CONTACT_ID,contactId);
        mContext.startActivity(intent);
    }

    @Override
    public void showEditMenu(View v, final ChatBean chatBean) {
        v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                boolean isOnTop = chatBean.isOnTop();
                int itemType = chatBean.getType();
                if (isOnTop) {
                    menu.add(0, R.id.context_unset_on_top, 0, "取消置顶");
                } else {
                    menu.add(0, R.id.context_set_on_top, 0, "置顶聊天");
                }
                menu.add(0, R.id.context_delete_chat, 1, "删除该聊天");
                if (itemType == WeChatDBHelper.CHAT_TYPE_OFFICIAL_ACCOUNT) {
                    menu.add(0, R.id.context_un_follow, 2, "取消关注");
                }
            }
        });
    }

    @Override
    public void setPresenter(ChatsContract.Presenter presenter) {
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
