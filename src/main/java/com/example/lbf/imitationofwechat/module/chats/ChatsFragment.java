package com.example.lbf.imitationofwechat.module.chats;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.adapter.ChatsListAdapter;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.beans.Conversation;
import com.example.lbf.imitationofwechat.event.RefreshEvent;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;
import com.example.lbf.imitationofwechat.views.DividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChatsFragment extends BaseFragment implements ChatsContract.View {

    private Context mContext;
    private ChatsContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;

    private ChatsListAdapter adapter;
    private LinearLayout mProgressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_chats, container, false);
        mContext = container.getContext();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_chats_list);
        mProgressView = (LinearLayout) root.findViewById(R.id.ll_progress);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new ChatsListAdapter(mContext);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext));
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getPosition();
        switch (item.getItemId()) {
            case R.id.context_delete_chat:
                mPresenter.deleteChats(position);
                break;
            case R.id.context_set_on_top:
                mPresenter.setOnTop(position);
                break;
            case R.id.context_unset_on_top:
                mPresenter.unsetOnTop(position);
                break;
        }
        updateList();
        return super.onContextItemSelected(item);
    }

    @Override
    public void setChatsList(List<Conversation> beanList) {
        if (beanList != null) {
            adapter.setList(beanList);
        }
        updateList();
    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showChat(int position) {
        //        如果需要更新用户资料，开发者只需要传新的info进去就可以了
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ChatActivity.KEY_CONVERSATION, adapter.getItem(position).getBmobIMConversation());
//        startActivity(ChatActivity.class,bundle);
    }

    @Override
    public void showEditMenu(View v, final Conversation c) {
        v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (c.isOnTop()) {
                    menu.add(0, R.id.context_unset_on_top, 0, "取消置顶");
                } else {
                    menu.add(0, R.id.context_set_on_top, 0, "置顶聊天");
                }
                menu.add(0, R.id.context_delete_chat, 1, "删除该聊天");
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
        LogUtil.i("error! " + errorMsg);
    }

    @Override
    public void hideLoading() {
        mProgressView.setVisibility(View.GONE);
    }

    @Subscribe
    public void onEventMainThread(RefreshEvent event){
        LogUtil.i("---会话页接收到自定义消息---");
        //因为新增`新朋友`这种会话类型
//        mLoaderManager.restartLoader(LOAD_CHATS,null,this);
        mPresenter.restartLoader();
    }

    /**注册离线消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表
        Log.i("info", "onEventMainThread() called with: " + "event = [" + event + "]");
//        mLoaderManager.restartLoader(LOAD_CHATS,null,this);
        mPresenter.restartLoader();
    }

    /**注册消息接收事件
     * @param event
     * 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     * 2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        Log.i("info", "onEventMainThread: ");
        //重新获取本地消息并刷新列表
//        mLoaderManager.restartLoader(LOAD_CHATS,null,this);
        mPresenter.restartLoader();
    }
}
