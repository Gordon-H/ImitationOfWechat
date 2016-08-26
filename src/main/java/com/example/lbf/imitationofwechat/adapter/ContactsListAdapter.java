package com.example.lbf.imitationofwechat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imitationofwechat.Interface.OnItemLongClickListener;
import com.example.lbf.imitationofwechat.base.BaseAdapter;
import com.example.lbf.imitationofwechat.base.BaseViewHolder;
import com.example.lbf.imitationofwechat.beans.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/6/29.
 */
public class ContactsListAdapter extends BaseAdapter<ContactBean> {

    private int defaultItemNumber = 4;
    private static final int TYPE_CONTENT_WITH_HEADER = 0;
    private static final int TYPE_CONTENT_NO_HEADER = 1;
    private static final int TYPE_FOOTER = 2;
//    保存分组的位置
    private int[] groupPositions;
//    分组List
    private List<Character> charList;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setList(List<ContactBean> beanList) {
        super.setList(beanList);
        charList = new ArrayList<>();
        for(int i = 4;i<beanList.size()-1;i++){
            String key = beanList.get(i).getSortKey();
            if(key!=null){
                if(key.charAt(0) == '2'){
                    charList.add(key.charAt(1));
                }else{
                    charList.add(key.charAt(0));
                }
            }else{
                charList.add('3');
            }
        }
        createGroupPositions(charList);
    }

    public void createGroupPositions(List<Character> charList){
        groupPositions = new int[29];
        for(int i = 0; i < 29; i++){
//            初始化
            groupPositions[i] = -1;
        }
//        回到顶部
        groupPositions[0] = 0;
//        星标朋友
        groupPositions[1] = defaultItemNumber;
        int j = 0;
        while(j<charList.size() && charList.get(j).equals('1')){
            j++;
        }
        while(j<charList.size()){
            if(charList.get(j) == '3'){
                break;
            }
            int pos = charList.get(j)-'A';
            groupPositions[pos+2] = 4+j;
            for(j++;j<charList.size() && charList.get(j-1).equals(charList.get(j));j++);
        }
        for(int i = 1; i < 29; i++){
            if(groupPositions[i] == -1){
                groupPositions[i] = groupPositions[i-1];
            }
        }
    }

    public int getGroupPosition(int group){
        return groupPositions[group];
    }

    public ContactsListAdapter(Context context) {
        super(context);
        setTypeSupport(new MultiItemTypeSupport<ContactBean>() {
            @Override
            public int getLayoutId(int itemType) {
                switch (itemType){
                    case TYPE_FOOTER:
                        return R.layout.item_contacts_footer;
                    case TYPE_CONTENT_WITH_HEADER:
                        return R.layout.item_contacts_with_header;
                    case TYPE_CONTENT_NO_HEADER:
                        return R.layout.item_contacts_no_header;
                    default:
                        return -1;
                }
            }

            @Override
            public int getItemType(int position, ContactBean contactBean) {
                if(position == getItemCount()-1){
                    return TYPE_FOOTER;
                }else if(hasHeader(position)){
                    return TYPE_CONTENT_WITH_HEADER;
                }else{
                    return TYPE_CONTENT_NO_HEADER;
                }
            }
        });
    }

    @Override
    protected void convert(final BaseViewHolder holder, ContactBean contactBean) {
        int type = holder.getItemViewType();
        final int position = holder.getAdapterPosition();
        if(type == TYPE_FOOTER) {
            holder.setText(R.id.contact_item_last,(getItemCount() - defaultItemNumber - 1)+"位联系人");
            return;
        }else if(type == TYPE_CONTENT_WITH_HEADER) {
            Character sortKey = charList.get(position-defaultItemNumber);
            if(sortKey.equals('1')) {
                holder.setText(R.id.contact_item_section,"星标朋友");
            }else if(sortKey.equals('3')){
                holder.setText(R.id.contact_item_section, "#");
            }else{
                holder.setText(R.id.contact_item_section, String.valueOf(sortKey));
            }
        }
        holder.setText(R.id.contact_item_name,contactBean.getName())
            .setImage(R.id.contact_item_image,contactBean.getImage());
        RelativeLayout layout = (RelativeLayout) holder.get(R.id.contact_item_main);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClickListener!=null) {
                    return onItemLongClickListener.onItemLongClick(v, position);
                }
                return false;
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    private boolean hasHeader(int position) {
        if(position < defaultItemNumber){
            return false;
        }
//        position是在recyclerView中的位置，前面包括了默认项，而要计算出分组位置，应该减去这部分
        int actualPosition = position - defaultItemNumber;
        if(actualPosition == 0 || !charList.get(actualPosition).equals(charList.get(actualPosition-1))){
            return true;
        }
        return false;
    }
}
