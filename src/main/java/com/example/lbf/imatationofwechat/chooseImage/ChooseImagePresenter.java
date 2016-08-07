package com.example.lbf.imatationofwechat.chooseImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.ImageLoader;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.beans.ImageFolderBean;
import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.data.source.ImagesScanLoader;
import com.example.lbf.imatationofwechat.data.source.local.WeChatPersistenceContract;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChooseImagePresenter implements ChooseImageContract.Presenter,LoaderManager.LoaderCallbacks<String> {

    private static final int LOAD_IMAGES_SCAN = 1;

    private Activity mContext;
    private ChooseImageContract.View mView;
    private final LoaderManager mLoaderManager;
    private final ImagesScanLoader mLoader;
    private List<ImageFolderBean> imageFolderList;
    private int selectedFolderPosition;
    private PopupWindow popupWindow;

    public ChooseImagePresenter(Activity context, LoaderManager loaderManager, ChooseImageContract.View view) {
        mContext = CommonUtil.checkNotNull(context,"context cannot be null!");
        mLoaderManager = CommonUtil.checkNotNull(loaderManager, "loader manager cannot be null!");
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
        imageFolderList = new ArrayList<>();
        mLoader = new ImagesScanLoader(context,imageFolderList);
    }

    @Override
    public void start() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)){
            Toast.makeText(mContext,"存储不可用！",Toast.LENGTH_LONG).show();
            return;
        }
        mLoaderManager.initLoader(LOAD_IMAGES_SCAN,null,this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        mView.showLoading();
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String path) {
        mView.setCurrentFolder(path);
        File file = new File(path);
        List<String> nameList = Arrays.asList(file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
                    return true;
                }
                return false;
            }
        }));
        mView.setImagesList(nameList);
        createSelectFolderWindow();
        mView.hideLoading();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        LogUtil.i("onLoaderReset");
    }

    @Override
    public void sendImages(List<String> selectImages) {
        Intent intent = new Intent();
        String[] contents = new String[selectImages.size()];
        for(int i = 0;i<contents.length;i++){
            contents[i] =  selectImages.get(i);
        }
        intent.putExtra(ChooseImageActivity.KEY_CONTENT, contents);
        intent.putExtra(ChooseImageActivity.KEY_TYPE, WeChatPersistenceContract.MsgsEntry.MSG_TYPE_IMAGE);
        mContext.setResult(Activity.RESULT_OK,intent);
        mContext.finish();
    }

    @Override
    public void createSelectFolderWindow() {
        final ImageLoader loader = ImageLoader.getInstance();
        final RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new CommonAdapter<ImageFolderBean>(mContext,
                imageFolderList, R.layout.choose_image_folder_layout) {
            @Override
            protected void convert(final CommonViewHolder holder, ImageFolderBean bean) {
                if(holder.getAdapterPosition() == selectedFolderPosition){
                    holder.get(R.id.state).setVisibility(View.VISIBLE);
                }else{
                    holder.get(R.id.state).setVisibility(View.GONE);
                }
                ImageView imageView = (ImageView) holder.get(R.id.image);
                imageView.setImageDrawable(new ColorDrawable(ContextCompat.getColor(
                        mContext,R.color.imageDefaultBg)));
                String name = bean.getPath().substring(bean.getPath().lastIndexOf("/")+1);
                holder.setText(R.id.title,name);
                holder.setText(R.id.content,bean.getImageCount()+"张");
                loader.loadImage(bean.getPath()+"/"+bean.getFirstImageName(),imageView);
                holder.setOnClickLisener(R.id.fl_chat_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        改变右边的选中框
                        CommonViewHolder vh = (CommonViewHolder)
                                recyclerView.findViewHolderForAdapterPosition(selectedFolderPosition);
//                        有可能被回收了所以需要判断一下避免nullpointerexception
                        if(vh !=null){
                            vh.get(R.id.state).setVisibility(View.GONE);
                        }
                        selectedFolderPosition = holder.getAdapterPosition();
                        holder.get(R.id.state).setVisibility(View.VISIBLE);
                        selectFolder(holder.getAdapterPosition());
                        popupWindow.dismiss();
                    }
                });
            }
        });
        popupWindow = new PopupWindow(recyclerView, ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (mContext.getResources().getDisplayMetrics().heightPixels*0.7));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupMenuAnimation);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        mView.setSelectFolderWindow(popupWindow);
    }

    @Override
    public void selectFolder(int position) {
        String path = imageFolderList.get(position).getPath();
        mView.setCurrentFolder(path);
        File file = new File(path);
        List<String> nameList = Arrays.asList(file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
                    return true;
                }
                return false;
            }
        }));
        mView.setImagesList(nameList);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ChooseImageActivity.CODE_TAKE_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    Uri result = data.getData();
                    LogUtil.i("result = "+result);
                }
                break;
            case ChooseImageActivity.CODE_VIEW_DETAIL:
                if(resultCode == Activity.RESULT_OK){
                    String action = data.getStringExtra(ChooseImageActivity.KEY_ACTION);
                    String[] selectedImages = data.getStringArrayExtra(ChooseImageActivity.KEY_CONTENT);
                    mView.setSelectedImagesList(selectedImages);
                    if(action!=null && action.equals("quit"));{
                        Intent intent = new Intent();
                        LogUtil.i("selectedImages[0] = "+selectedImages[0]);
                        intent.putExtra(ChooseImageActivity.KEY_CONTENT, selectedImages);
                        intent.putExtra(ChooseImageActivity.KEY_TYPE, WeChatPersistenceContract.MsgsEntry.MSG_TYPE_IMAGE);
                        mContext.setResult(Activity.RESULT_OK,intent);
                        mContext.finish();
                    }
                }
        }
    }

}
