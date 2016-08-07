package com.example.lbf.imatationofwechat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;

import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.Interface.OnItemClickListener;
import com.example.lbf.imatationofwechat.Interface.OnItemLongClickListener;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.beans.ContactBean;

import java.util.List;

/**
 * Created by lbf on 2016/6/29.
 */
public class ContactsListAdapter extends CommonAdapter<ContactBean>{

    private SectionIndexer mIndexer;
    private int defaultItemNumber = 4;
    private static final int TYPE_CONTENT_WITH_HEADER = 0;
    private static final int TYPE_CONTENT_NO_HEADER = 1;
    private static final int TYPE_FOOTER = 2;
    private int position;

    public SectionIndexer getIndexer() {
        return mIndexer;
    }

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

    public ContactsListAdapter(Context context, List<ContactBean> contactBeanList) {
        super(context,contactBeanList);
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

    public void setIndexer(SectionIndexer mIndexer) {
        this.mIndexer = mIndexer;
    }

    @Override
    protected void convert(final CommonViewHolder holder, ContactBean contactBean) {
        int type = holder.getItemViewType();
        if(type == TYPE_FOOTER) {
            holder.setText(R.id.contact_item_last,(getItemCount() - defaultItemNumber - 1)+"位联系人");
            return;
        }else if(type == TYPE_CONTENT_WITH_HEADER) {
            String sortKey = contactBean.getSortKey();
            if(sortKey.equals("*")) {
                holder.setText(R.id.contact_item_section,"星标朋友");
            }else{
                holder.setText(R.id.contact_item_section,sortKey);
            }
        }
        holder.setImageRes(R.id.contact_item_image,contactBean.getImage())
                .setText(R.id.contact_item_name,contactBean.getName());
        RelativeLayout layout = (RelativeLayout) holder.get(R.id.contact_item_main);
        final int position = holder.getAdapterPosition();
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClickListener!=null) {
                    return onItemLongClickListener.onItemLongClick(v, position);
                }
                return false;
            }
        });
//        layout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add(0,R.id.context_set_remarks,1,"设置备注及标签");
//            }
//        });
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
        LogUtil.i("actualPosition = " + actualPosition);
        LogUtil.i("getItemCount = " + getItemCount());
        LogUtil.i("defaultItemNumber = " + defaultItemNumber);
        int section = mIndexer.getSectionForPosition(actualPosition);
        if(actualPosition == mIndexer.getPositionForSection(section)) {
            return true;
        }
        return false;
    }
}
