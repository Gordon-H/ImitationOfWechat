package com.example.lbf.imitationofwechat.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lbf on 2016/7/3.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder>{

    protected Context context;
    private int layoutRes;
    protected List<T> beanList;
    private MultiItemTypeSupport typeSupport;

    public interface MultiItemTypeSupport<T>{
        int getLayoutId(int itemType);
        int getItemType(int position,T t);
    }

    public BaseAdapter(Context context, List<T> beanList, int layoutRes) {
        this.context = context;
        this.layoutRes = layoutRes;
        this.beanList = beanList;
    }
    public BaseAdapter(Context context, int layoutRes) {
        this.context = context;
        this.layoutRes = layoutRes;
    }

    public void setList(List<T> beanList){
        this.beanList = beanList;
    }

    public List<T> getList(){
        return beanList;
    }

    public void remove(int position){
        beanList.remove(position);
    }
    public T getItem(int position){
        return beanList.get(position);
    }
    public void setTypeSupport(MultiItemTypeSupport typeSupport) {
        this.typeSupport = typeSupport;
    }

    public BaseAdapter(Context context, List<T> beanList) {
        this.context = context;
        this.beanList = beanList;
    }
    public BaseAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        if(typeSupport!=null){
            return typeSupport.getItemType(position,beanList.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(typeSupport!=null){
            layoutRes = typeSupport.getLayoutId(viewType);
        }
        return BaseViewHolder.getViewHolder(layoutRes,parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        convert(holder,beanList.get(position));
    }

    protected abstract void convert(BaseViewHolder holder, T t);

    @Override
    public int getItemCount() {
        if(beanList == null){
            return 0;
        }else{
            return beanList.size();
        }
    }

}
