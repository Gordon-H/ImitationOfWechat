package com.example.lbf.imatationofwechat.chooseImage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.adapter.ImageGridAdapter2;
import com.example.lbf.imatationofwechat.beans.ImageFolderBean;
import com.example.lbf.imatationofwechat.imageDetail.ImageDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class ChooseImageFragment extends Fragment implements ChooseImageContract.View, View.OnClickListener,ImageGridAdapter2.OnImageTouchListener {

    private Context mContext;
    private ChooseImageContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private RelativeLayout rlBottomBar;
    private Button btSend;
    private TextView tvFolderName;
    private TextView tvPreview;
    private ProgressDialog mProgressView;
    private PopupWindow ppwSelectFolder;
    private ArrayList<String> nameList;
    private List<ImageFolderBean> imageFolderList;
    private ImageGridAdapter2 imageAdapter;
    private String currentFolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_choose_image,container,false);
        mContext = container.getContext();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        rlBottomBar = (RelativeLayout) root.findViewById(R.id.rlBottomBar);
        tvFolderName = (TextView) root.findViewById(R.id.tvFolderName);
        tvPreview = (TextView) root.findViewById(R.id.tvPreview);
        btSend = (Button) getActivity().findViewById(R.id.btSend);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        nameList = new ArrayList<>();
        imageFolderList = new ArrayList<>();
        imageAdapter = new ImageGridAdapter2(mContext, nameList,ChooseImageActivity.selectMode);
        imageAdapter.setListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(imageAdapter);
        btSend.setOnClickListener(this);
        tvFolderName.setOnClickListener(this);
        tvPreview.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ppwSelectFolder!=null){
            ppwSelectFolder.dismiss();
            ppwSelectFolder = null;
        }
    }
    @Override
    public void setPresenter(ChooseImageContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showLoading() {
        mProgressView = ProgressDialog.show(mContext,null,"正在加载...");
    }

    @Override
    public void showError(String errorMsg) {
        LogUtil.i("error! "+errorMsg);
    }

    @Override
    public void hideLoading() {
        mProgressView.dismiss();
    }

    @Override
    public void onImageClick(int position) {
        if(position==0){
            showTakePhoto();
        }else{
            showItem(position);
        }
    }

    @Override
    public void onViewUpdate(int oldSize, int newSize) {
        if(oldSize == 0 && newSize>0){
            btSend.setEnabled(true);
            btSend.setText("发送"+"("+newSize+"/9)");
            btSend.setAlpha(1f);
            tvPreview.setEnabled(true);
            tvPreview.setText("预览"+"("+newSize+")");
            tvPreview.setAlpha(1f);
        }else if(oldSize>0&&newSize == 0){
            btSend.setEnabled(false);
            btSend.setText("发送");
            btSend.setAlpha(0.6f);
            tvPreview.setEnabled(false);
            tvPreview.setText("预览");
            tvPreview.setAlpha(0.6f);
        }else {
            btSend.setText("发送" + "(" + newSize + "/9)");
            tvPreview.setText("预览" + "(" + newSize + ")");
        }
    }

    @Override
    public void setImagesList(List<String> nameList) {
        this.nameList.clear();
//        第一项显示“拍摄照片”
        this.nameList.add("");
        this.nameList.addAll(nameList);
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSelectedImagesList(String[] selectedImages) {
        List<String> imagesList = imageAdapter.getSelectedImages();
        imagesList.clear();
        for(String s:selectedImages){
            imagesList.add(s);
        }
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void setCurrentFolder(String path) {
        currentFolder = path;
        imageAdapter.setCurrentFolder(path);
    }

    @Override
    public void setSelectFolderWindow(PopupWindow popupWindow) {
        ppwSelectFolder = popupWindow;
    }

    @Override
    public void showSelectFolderWindow() {
        ppwSelectFolder.showAsDropDown(rlBottomBar);
    }

    @Override
    public void showItem(int position) {
        Intent intent = new Intent(mContext, ImageDetailActivity.class);
        intent.putExtra(ChooseImageActivity.KEY_SELECT_MODE,ChooseImageActivity.selectMode);
        intent.putExtra(ChooseImageActivity.KEY_POSITION,position);
        intent.putExtra(ChooseImageActivity.KEY_CURRENT_FOLDER,currentFolder);
        intent.putStringArrayListExtra(ChooseImageActivity.KEY_NAME_LIST, nameList);
        intent.putStringArrayListExtra(ChooseImageActivity.KEY_SELECTED_LIST, imageAdapter.getSelectedImages());
        startActivityForResult(intent,ChooseImageActivity.CODE_VIEW_DETAIL);
    }

    @Override
    public void showTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,ChooseImageActivity.CODE_TAKE_PHOTO);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvFolderName:
                showSelectFolderWindow();
                break;
            case R.id.btSend:
                mPresenter.sendImages(imageAdapter.getSelectedImages());
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode,resultCode, data);
    }
}
