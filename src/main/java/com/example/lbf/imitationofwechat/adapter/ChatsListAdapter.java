package com.example.lbf.imitationofwechat.adapter;

import android.content.Context;
import android.view.View;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imitationofwechat.Interface.OnItemLongClickListener;
import com.example.lbf.imitationofwechat.base.BaseAdapter;
import com.example.lbf.imitationofwechat.base.BaseViewHolder;
import com.example.lbf.imitationofwechat.beans.Conversation;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

/**
 * Created by lbf on 2016/6/28.
 */
public class ChatsListAdapter extends BaseAdapter<Conversation> {
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_STARRED = 2;
    private int position;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ChatsListAdapter(Context context) {
        super(context);
        setTypeSupport(new MultiItemTypeSupport<Conversation>() {
            @Override
            public int getLayoutId(int itemType) {
//                置顶和普通聊天使用相同的资源布局，只是background不同
                return R.layout.item_chats;
            }

            @Override
            public int getItemType(int position, Conversation c) {
                if(c.isOnTop()){
                    return TYPE_STARRED;
                }
                return TYPE_NORMAL;
            }
        });
    }

    @Override
    protected void convert(final BaseViewHolder holder, final Conversation c) {
        int type = holder.getItemViewType();
        if(type == TYPE_STARRED){
            holder.setBackgroundResource(R.id.layout_root,
                    R.drawable.item_stressed_background);
        }
        String time = CommonUtil.formatTime(c.getLastMessageTime());
        holder.setText(R.id.tv_name,c.getcName())
                .setText(R.id.tv_content,c.getLastMessageContent())
                .setText(R.id.tv_time,time)
        .setImage(R.id.iv_avatar,c.getAvatar());
        int unReadCount = c.getUnReadCount();
        LogUtil.i("unReadCount = "+unReadCount);
        if(unReadCount > 0){
            holder.setText(R.id.tv_badge,unReadCount+"")
                    .setVisibility(R.id.tv_badge,View.VISIBLE);
        }else{
            holder.setVisibility(R.id.tv_badge,View.GONE);
        }

        final int position = holder.getAdapterPosition();
        holder.get(R.id.layout_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setPosition(position);
                    c.onClick(context,v);
            }
        });
        holder.get(R.id.layout_root).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                c.onLongClick(context,v);
                return false;
            }
        });

//        holder.get(R.id.chat_item_main).setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                boolean isOnTop = c.isOnTop();
//                int itemType = c.getType();
//                    if(isOnTop){
//                        menu.add(0,R.id.context_unset_on_top,0,"取消置顶");
//                    }else{
//                        menu.add(0,R.id.context_set_on_top,0,"置顶聊天");
//                    }
//                menu.add(0,R.id.context_delete_chat,1,"删除该聊天");
//                if(itemType == WeChatDBHelper.CHAT_TYPE_OFFICIAL_ACCOUNT){
//                    menu.add(0,R.id.context_un_follow,2,"取消关注");
//                }
//            }
//        });
//
//        if(c.getType() == WeChatDBHelper.CHAT_TYPE_CONTACT){
//            holder.get(R.id.chat_item_main).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ChatActivity.class);
//                    intent.putExtra("id",c.getId());
//                    context.startActivity(intent);
//                }
//            });
//        }

    }


}
