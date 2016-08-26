package com.example.lbf.imitationofwechat.base;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lbf on 2016/7/3.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private View convertView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
        views = new SparseArray<>();
    }

    public static BaseViewHolder getViewHolder(int layoutRes, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        BaseViewHolder holder = new BaseViewHolder(itemView);
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

    public BaseViewHolder setText(int id, String text){
        TextView textView = (TextView) get(id);
        textView.setText(text);
        return this;
    }
    public BaseViewHolder setImageRes(int id, int imageRes){
        ImageView imageView = (ImageView) get(id);
        imageView.setImageResource(imageRes);
        return this;
    }
    public BaseViewHolder setEnabled(int id, boolean isEnabled){
        get(id).setEnabled(isEnabled);
        return this;
    }
    public BaseViewHolder setImage(int id, String uri){
        ImageView imageView = (ImageView) get(id);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(uri,imageView);
        return this;
    }

    public BaseViewHolder setImageBitmap(int id, Bitmap bitmap){
        ImageView imageView = (ImageView) get(id);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setVisibility(int id, int state){
        get(id).setVisibility(state);
        return this;
    }

    public BaseViewHolder setBackgroundResource(int id, int backgroundRes){
        View view = get(id);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public BaseViewHolder setOnClickLisener(int id, View.OnClickListener listener){
        View imageView = get(id);
        imageView.setOnClickListener(listener);
        return this;
    }
    public BaseViewHolder setOnLongClickLisener(int id, View.OnLongClickListener listener){
        View imageView = get(id);
        imageView.setOnLongClickListener(listener);
        return this;
    }
}
