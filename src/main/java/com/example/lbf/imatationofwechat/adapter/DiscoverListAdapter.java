package com.example.lbf.imatationofwechat.adapter;

import android.content.Context;
import android.view.View;

import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.beans.DiscoverBean;

import java.util.List;

/**
 * Created by lbf on 2016/6/28.
 */
public class DiscoverListAdapter extends CommonAdapter<DiscoverBean> {
    public DiscoverListAdapter(Context context,List<DiscoverBean> beanList) {
        super(context,beanList,R.layout.item_discover);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(final CommonViewHolder holder, DiscoverBean discoverBean) {
        View newsImage = holder.get(R.id.discover_item_news_image);
        View newsText = holder.get(R.id.discover_item_news_text);
        View header = holder.get(R.id.discover_item_header);
        newsImage.setVisibility(View.GONE);
        newsText.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
        if(discoverBean.isHasHeader()){
            header.setVisibility(View.VISIBLE);
        }
        int type = discoverBean.getType();
        if(type == DiscoverBean.TYPE_NEWS_IMAGE){
            newsImage.setVisibility(View.VISIBLE);
        } else if(type == DiscoverBean.TYPE_NEWS_TEXT_IMAGE){
            newsImage.setVisibility(View.VISIBLE);
            newsText.setVisibility(View.VISIBLE);
        }
        holder.setImageRes(R.id.discover_item_image,discoverBean.getImage());
        holder.setText(R.id.discover_item_name,discoverBean.getName());
        holder.get(R.id.view_discover_moments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(v,holder.getAdapterPosition());
                }
            }
        });
    }
}
