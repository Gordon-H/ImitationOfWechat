package com.example.lbf.imatationofwechat.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.widget.SectionIndexer;

import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.beans.ContactBean;

import java.util.List;

/**
 * Created by lbf on 2016/6/29.
 */
public class FollowListAdapter extends CommonAdapter<ContactBean>{
    private SectionIndexer mIndexer;
    private static final int TYPE_CONTENT_WITH_HEADER = 0;
    private static final int TYPE_CONTENT_NO_HEADER = 1;
    private static final int TYPE_FOOTER = 2;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FollowListAdapter(Context context, List<ContactBean> contactBeanList) {
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
            holder.setText(R.id.contact_item_last,(getItemCount() - 1)+"个公众号");
            return;
        }else if(type == TYPE_CONTENT_WITH_HEADER) {
            String sortKey = contactBean.getSortKey();
            holder.setText(R.id.contact_item_section,sortKey);
        }
        holder.setImageRes(R.id.contact_item_image,contactBean.getImage())
                .setText(R.id.contact_item_name,contactBean.getName());
        holder.get(R.id.contact_item_main).setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0,R.id.context_set_remarks,1,"取消关注");
            }
        });
        holder.get(R.id.contact_item_main).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });

    }

    private boolean hasHeader(int position) {
        int section = mIndexer.getSectionForPosition(position);
        if(position == mIndexer.getPositionForSection(section)) {
            return true;
        }
        return false;
    }
}
