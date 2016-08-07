package com.example.lbf.imatationofwechat.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lbf on 2016/7/3.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder>{

    private Context context;
    private int layoutRes;
    private List<T> beanList;
    private MultiItemTypeSupport typeSupport;

    public interface MultiItemTypeSupport<T>{
        int getLayoutId(int itemType);
        int getItemType(int position,T t);
    }

    public CommonAdapter(Context context,List<T> beanList,int layoutRes) {
        this.context = context;
        this.layoutRes = layoutRes;
        this.beanList = beanList;
    }

    public void setTypeSupport(MultiItemTypeSupport typeSupport) {
        this.typeSupport = typeSupport;
    }

    public CommonAdapter(Context context, List<T> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    @Override
    public int getItemViewType(int position) {
        if(typeSupport!=null){
            return typeSupport.getItemType(position,beanList.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(typeSupport!=null){
            layoutRes = typeSupport.getLayoutId(viewType);
        }
        return CommonViewHolder.getViewHolder(layoutRes,parent);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        convert(holder,beanList.get(position));
    }

    protected abstract void convert(CommonViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return beanList.size();
    }

}
