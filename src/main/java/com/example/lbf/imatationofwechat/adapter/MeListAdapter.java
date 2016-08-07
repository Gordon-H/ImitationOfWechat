package com.example.lbf.imatationofwechat.adapter;

import android.content.Context;
import android.view.View;

import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.beans.DiscoverBean;

import java.util.List;

/**
 * Created by lbf on 2016/6/28.
 */
public class MeListAdapter extends CommonAdapter<DiscoverBean>{
    public MeListAdapter(Context context, List<DiscoverBean> beanList) {
        super(context,beanList,R.layout.item_discover);
    }

    public interface OnItemSelectedListener{
        void OnItemSelected(int position);
    }

    private OnItemSelectedListener listener;

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(final CommonViewHolder holder, DiscoverBean discoverBean) {
        View header = holder.get(R.id.discover_item_header);
        header.setVisibility(View.GONE);
        if(discoverBean.isHasHeader()){
            header.setVisibility(View.VISIBLE);
        }
        holder.setImageRes(R.id.discover_item_image,discoverBean.getImage());
        holder.setText(R.id.discover_item_name,discoverBean.getName());
        holder.get(R.id.view_discover_moments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.OnItemSelected(holder.getAdapterPosition());
                }
            }
        });
    }

}
