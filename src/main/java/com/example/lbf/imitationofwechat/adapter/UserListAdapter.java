package com.example.lbf.imitationofwechat.adapter;

import android.content.Context;
import android.view.View;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.base.BaseAdapter;
import com.example.lbf.imitationofwechat.base.BaseViewHolder;

/**
 * Created by lbf on 2016/6/28.
 */
public class UserListAdapter extends BaseAdapter<User> {
    public UserListAdapter(Context context) {
        super(context,R.layout.item_user);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, User user) {
        holder.setImage(R.id.iv_avatar,user.getAvatar())
                .setText(R.id.tv_name,user.getName());
        holder.get(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(v,holder.getAdapterPosition());
                }
            }
        });
    }

}
