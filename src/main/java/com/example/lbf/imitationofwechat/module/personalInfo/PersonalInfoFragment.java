package com.example.lbf.imitationofwechat.module.personalInfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.module.changeText.changeTextActivity;
import com.example.lbf.imitationofwechat.module.chooseImage.ChooseImageActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.views.ItemView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * Created by lbf on 2016/7/29.
 */
public class PersonalInfoFragment extends BaseFragment implements PersonalInfoContract.View,View.OnClickListener {
    private static final int CODE_SELECT_IMAGE = 0;
    private static final int CODE_CHANGE_NAME = 1;
    private static final int CODE_CHANGE_SIGNATURE = 2;
    private static final int itemSize = 8;

    private PersonalInfoContract.Presenter mPresenter;
    private ItemView[] itemViews;
    private Dialog dialogSetGender;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_personal_info, container, false);
        itemViews = new ItemView[itemSize];
        itemViews[0] = (ItemView) root.findViewById(R.id.view_avatar);
        itemViews[1] = (ItemView) root.findViewById(R.id.view_name);
        itemViews[2] = (ItemView) root.findViewById(R.id.view_username);
        itemViews[3] = (ItemView) root.findViewById(R.id.view_qrImage);
        itemViews[4] = (ItemView) root.findViewById(R.id.view_address);
        itemViews[5] = (ItemView) root.findViewById(R.id.view_gender);
        itemViews[6] = (ItemView) root.findViewById(R.id.view_location);
        itemViews[7] = (ItemView) root.findViewById(R.id.view_signature);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
        initView();
    }

    private void initView() {
        for(int i = 0;i<itemSize;i++){
            itemViews[i].setOnClickListener(this);
        }
        dialogSetGender = new Dialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_gender,null);
        RadioButton cbMale = (RadioButton) view.findViewById(R.id.rb_male);
        RadioButton cbFemale = (RadioButton) view.findViewById(R.id.rb_female);
        if(itemViews[5].getTextRight().equals("男")){
            cbMale.setChecked(true);
        }else{
            cbFemale.setChecked(true);
        }
        cbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    itemViews[5].setTextRight("男");
                }else{
                    itemViews[5].setTextRight("女");
                }
                dialogSetGender.dismiss();
            }
        });
        dialogSetGender.setContentView(view);
        dialogSetGender.setTitle(null);
        dialogSetGender.setCancelable(true);
    }

    @Override
    public void setPresenter(PersonalInfoContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }


    @Override
    public void showItem(View v) {
        switch (v.getId()){
            case R.id.view_avatar:
                Intent intent = new Intent(mContext,ChooseImageActivity.class);
                intent.putExtra(ChooseImageActivity.KEY_SELECT_MODE,ChooseImageActivity.MODE_SINGLE);
                startActivityForResult(intent,CODE_SELECT_IMAGE);
                break;
            case R.id.view_name:
                Bundle bundle = new Bundle();
                bundle.putString(changeTextActivity.KEY_CONTENT1,itemViews[1].getTextRight());
                bundle.putString(changeTextActivity.KEY_TITLE,"更改昵称");
                bundle.putString(changeTextActivity.KEY_HINT1,"改个酷炫的名字吧！");
                startActivityForResult(changeTextActivity.class,bundle,CODE_CHANGE_NAME);
                break;
            case R.id.view_username:
            case R.id.view_qrImage:
            case R.id.view_address:
            case R.id.view_location:
                CommonUtil.showNoImplementText(mContext);
                break;
            case R.id.view_gender:
                dialogSetGender.show();
                break;
            case R.id.view_signature:
                Bundle bundle2 = new Bundle();
                bundle2.putString(changeTextActivity.KEY_CONTENT1,itemViews[7].getTextRight());
                bundle2.putString(changeTextActivity.KEY_TITLE,"个性签名");
                bundle2.putString(changeTextActivity.KEY_HINT1,"改个酷炫的签名吧！");
                startActivityForResult(changeTextActivity.class,bundle2,CODE_CHANGE_SIGNATURE);
                break;
        }
    }

    @Override
    public void setUserInfo(String avatar, String name, String username, boolean isMale, String location, String signature) {
        int imageSize = itemViews[0].getIvRightSize();
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(avatar,new ImageSize(imageSize,imageSize));
        itemViews[0].setImageRight(bitmap);
        itemViews[1].setTextRight(name);
        itemViews[2].setTextRight(username);
        itemViews[5].setTextRight((Boolean)isMale!=null && isMale?"男":"女");
        itemViews[6].setTextRight(location);
        itemViews[7].setTextRight(signature);
    }

    @Override
    public void onClick(View v) {
        showItem(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CODE_SELECT_IMAGE:
                if(resultCode == Activity.RESULT_OK){
                    if(data!=null){
                        String imageUrl = "file://"+data.getStringArrayExtra(ChooseImageActivity.KEY_CONTENT)[0];
                        ImageLoader.getInstance().displayImage(imageUrl,itemViews[0].getIvRight());
                        mPresenter.saveAvatar(imageUrl);
                    }

                }
                break;
            case CODE_CHANGE_NAME:
                if(data!=null){
                    String name = data.getStringExtra(changeTextActivity.KEY_CONTENT1);
                    itemViews[1].setTextRight(name);
                    mPresenter.saveName(name);
                }

                break;
            case CODE_CHANGE_SIGNATURE:
                if(data!=null){
                    String signature = data.getStringExtra(changeTextActivity.KEY_CONTENT1);
                    itemViews[7].setTextRight(signature);
                    mPresenter.saveSignature(signature);
                    break;
                }


        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }
}
