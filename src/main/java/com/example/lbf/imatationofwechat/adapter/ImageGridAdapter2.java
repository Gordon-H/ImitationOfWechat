package com.example.lbf.imatationofwechat.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.chooseImage.ChooseImageActivity;
import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/6/29.
 */
public class ImageGridAdapter2 extends CommonAdapter<String>{

//    普通照片
    private static final int TYPE_NORMAL = 1;
//    拍摄新照片
    private static final int TYPE_NEW = 2;

    private Context context;
    private int selectMode;
    private ImageLoader loader;
    private String currentFolder;
    private List<String> imageNameList;
    private ArrayList<String> selectedImages;

    public ArrayList<String> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(ArrayList<String> selectedImages) {
        this.selectedImages = selectedImages;
    }

    public interface OnImageTouchListener{
        void onImageClick(int position);
        void onViewUpdate(int oldSize, int newSize);
    }
    private OnImageTouchListener listener;

    public void setListener(OnImageTouchListener listener) {
        this.listener = listener;
    }

    public ImageGridAdapter2(Context context, List<String> beanList, int selectMode) {
        super(context,beanList);
        this.context = context;
        this.selectMode = selectMode;
        setTypeSupport(new MultiItemTypeSupport<String>() {
            @Override
            public int getLayoutId(int itemType) {
                switch (itemType){
                    case TYPE_NORMAL:
                        return R.layout.choose_image_item;
                    case TYPE_NEW:
                        return R.layout.choose_image_item_new;
                }
                return 0;
            }
            @Override
            public int getItemType(int position, String bean) {
                if(position == 0){
                    return TYPE_NEW;
                }else{
                    return TYPE_NORMAL;
                }
            }
        });
        loader = ImageLoader.getInstance();
        imageNameList = beanList;
        imageNameList.add(0,"");
        selectedImages = new ArrayList<>();
    }

    public String getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(String currentFolder) {
        this.currentFolder = currentFolder;
    }

    @Override
    protected void convert(final CommonViewHolder holder, String bean) {
        int type = holder.getItemViewType();
        if(type == TYPE_NEW) {
            holder.setOnClickLisener(R.id.fl_chat_root, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onImageClick(0);
                    }
                }
            });
        }else{
            final ImageView image = (ImageView) holder.get(R.id.image);
            final ImageView state = (ImageView) holder.get(R.id.state);
            final String path = currentFolder + "/" + bean;
            image.setImageDrawable(new ColorDrawable(ContextCompat.getColor(
                    context,R.color.imageDefaultBg)));
            image.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onImageClick(holder.getAdapterPosition());
                    }
                }
            });
            loader.loadImage(path, image);
            if(selectMode == ChooseImageActivity.MODE_SINGLE){
//                如果是单选模式就隐藏多选框直接返回
                state.setVisibility(View.GONE);
                return;
            }

            if(selectedImages.contains(path)){
                image.setAlpha(0.6f);
                state.setSelected(true);
            }else{
                image.setAlpha(1.0f);
                state.setSelected(false);
            }
            state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oldSize = selectedImages.size();
                    if(!state.isSelected()){
                        if(selectedImages.size() == 9){
                            Toast.makeText(context,"你最多只能选择9张图片",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        image.setAlpha(0.6f);
                        selectedImages.add(path);
                        state.setSelected(true);
                    }else{
                        image.setAlpha(1.0f);
                        selectedImages.remove(path);
                        state.setSelected(false);
                    }
                    int newSize = selectedImages.size();
                    if(listener!=null){
                        listener.onViewUpdate(oldSize,newSize);
                    }
                }
            });
        }
    }
}
