package com.example.lbf.imitationofwechat.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.lbf.imitationofwechat.util.LogUtil;
import com.example.lbf.imitationofwechat.views.ZoomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lbf on 2016/7/20.
 */
public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private String currentFolder;
    private List<String> imageUrls;
    private ImageLoader loader;

    public void setList(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ImagePagerAdapter(Context context) {
        this.context = context;
        loader = ImageLoader.getInstance();
    }

    public ImagePagerAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        loader = ImageLoader.getInstance();
    }

    public ImagePagerAdapter(Context context,String currentFolder, List<String> imageUrls) {
        this.context = context;
        this.currentFolder = currentFolder;
        this.imageUrls = imageUrls;
        loader = ImageLoader.getInstance();
    }
    private ZoomImageView.OnSingleTapListener onSingleTapListener;

    public void setOnSingleTapListener(ZoomImageView.OnSingleTapListener onSingleTapListener) {
        this.onSingleTapListener = onSingleTapListener;
    }

    public String getImagePath(int position) {
        String path;
        if (currentFolder != null && !currentFolder.isEmpty()) {
            path = currentFolder + "/" + imageUrls.get(position);
        } else {
            path = imageUrls.get(position);
        }
        return path;
    }

    public int getItemPosition(String s){
        return imageUrls.indexOf(s);
    }
    @Override
    public int getCount() {
        return imageUrls == null ?0:imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();
        ZoomImageView imageView = new ZoomImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setClickable(true);
        if(onSingleTapListener!=null){
            imageView.setOnSingleTapListener(onSingleTapListener);
        }
        String path = getImagePath(position);
//        path = Uri.parse(path).getPath();
        LogUtil.i("path = "+path);
        loader.displayImage(path, imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
