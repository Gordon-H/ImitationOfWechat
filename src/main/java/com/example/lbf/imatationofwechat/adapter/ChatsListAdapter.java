package com.example.lbf.imatationofwechat.adapter;

import android.content.Context;
import android.view.View;

import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imatationofwechat.Interface.OnItemLongClickListener;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.beans.ChatBean;
import com.example.lbf.imatationofwechat.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lbf on 2016/6/28.
 */
public class ChatsListAdapter extends CommonAdapter<ChatBean>{
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

    public ChatsListAdapter(Context context, List<ChatBean> chatBeanList) {
        super(context,chatBeanList);
        setTypeSupport(new MultiItemTypeSupport<ChatBean>() {
            @Override
            public int getLayoutId(int itemType) {
//                置顶和普通聊天使用相同的资源布局，只是background不同
                return R.layout.chat_item;
            }

            @Override
            public int getItemType(int position, ChatBean chatBean) {
                if(chatBean.isOnTop()){
                    return TYPE_STARRED;
                }
                return TYPE_NORMAL;
            }
        });
    }

    @Override
    protected void convert(final CommonViewHolder holder, final ChatBean chatBean) {
        int type = holder.getItemViewType();
        if(type == TYPE_STARRED){
            holder.setBackgroundResource(R.id.chat_item_main,
                    R.drawable.item_stressed_background);
        }
        LogUtil.i("time = ");
        String time = formatTime(chatBean.getTime());
        holder.setText(R.id.chat_item_name,chatBean.getName())
                .setText(R.id.chat_item_content,chatBean.getContent())
                .setText(R.id.chat_item_time,time)
                .setImageRes(R.id.chat_item_image,chatBean.getImage());
        final int position = holder.getAdapterPosition();
        holder.get(R.id.chat_item_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    setPosition(position);
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });
        holder.get(R.id.chat_item_main).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClickListener != null){
                    setPosition(position);
                    return onItemLongClickListener.onItemLongClick(v,position);
                }
                return false;
            }
        });

//        holder.get(R.id.chat_item_main).setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                boolean isOnTop = chatBean.isOnTop();
//                int itemType = chatBean.getType();
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
//        if(chatBean.getType() == WeChatDBHelper.CHAT_TYPE_CONTACT){
//            holder.get(R.id.chat_item_main).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ChatActivity.class);
//                    intent.putExtra("id",chatBean.getId());
//                    context.startActivity(intent);
//                }
//            });
//        }

    }

    private String formatTime(long time) {
        Calendar currentCal = Calendar.getInstance();
        Calendar msgCal = Calendar.getInstance();
        msgCal.setTimeInMillis(time);
        int interval = currentCal.get(Calendar.DAY_OF_YEAR) - msgCal.get(Calendar.DAY_OF_YEAR);
        SimpleDateFormat ft;
        if(interval > 1){
            ft = new SimpleDateFormat("MM/dd/yyyy");
            return ft.format(msgCal.getTime());
        }else if(interval ==1){
            return "昨天";
        }else{
            ft = new SimpleDateFormat("HH:mm");
            return ft.format(msgCal.getTime());
        }
    }
}
