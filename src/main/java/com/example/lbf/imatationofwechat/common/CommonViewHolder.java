package com.example.lbf.imatationofwechat.common;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lbf on 2016/7/3.
 */
public class CommonViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private View convertView;
    public CommonViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
        views = new SparseArray<>();
    }

    public static CommonViewHolder getViewHolder(int layoutRes, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        CommonViewHolder holder = new CommonViewHolder(itemView);
        return holder;
    }

    public View get(int id) {
        View view = views.get(id);
        if (view == null) {
            view = convertView.findViewById(id);
            views.put(id, view);
        }
        return view;
    }
    public CommonViewHolder setText(int id,String text){
        TextView textView = (TextView) get(id);
        textView.setText(text);
        return this;
    }
    public CommonViewHolder setImageRes(int id,int imageRes){
        ImageView imageView = (ImageView) get(id);
        imageView.setImageResource(imageRes);
        return this;
    }
    public CommonViewHolder setBackgroundResource(int id,int backgroundRes){
        View view = get(id);
        view.setBackgroundResource(backgroundRes);
        return this;
    }
    public CommonViewHolder setOnClickLisener(int id, View.OnClickListener listener){
        View imageView = get(id);
        imageView.setOnClickListener(listener);
        return this;
    }
    public CommonViewHolder setOnLongClickLisener(int id, View.OnLongClickListener listener){
        View imageView = get(id);
        imageView.setOnLongClickListener(listener);
        return this;
    }
}
