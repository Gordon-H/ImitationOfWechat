package com.example.lbf.imitationofwechat.module.userInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.module.changeText.changeTextActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lbf on 2016/7/29.
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener,UserInfoContract.View {

    private static final int CODE_SET_TAG =1;

    private UserInfoContract.Presenter mPresenter;

    private ImageView iv_avatar;
    private TextView tv_username;
    private TextView tv_remarks;
    private TextView tv_name;
    private View layoutTag;
    private TextView tvTagTitle;
    private TextView tvTagContent;
    private TextView tvLocationContent;
    private TextView tvSignatureContent;
    private Button btn_add_friend;
    private Button btn_chat;
//    menu中星标朋友那一项item
    private MenuItem starItem;
    private boolean isInfoDirty = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_info,container,false);
        iv_avatar = (ImageView) root.findViewById(R.id.iv_avatar);
        tv_name = (TextView) root.findViewById(R.id.tv_name);
        tv_remarks = (TextView) root.findViewById(R.id.tv_remarks);
        tv_username = (TextView) root.findViewById(R.id.tv_username);
        btn_add_friend = (Button) root.findViewById(R.id.btn_addFriend);
        btn_chat = (Button) root.findViewById(R.id.btn_chat);
        layoutTag = root.findViewById(R.id.ll_tag);
        tvTagTitle = (TextView) root.findViewById(R.id.tv_tagTitle);
        tvTagContent = (TextView) root.findViewById(R.id.tv_tagContent);
        tvLocationContent = (TextView) root.findViewById(R.id.tv_locationContent);
        tvSignatureContent = (TextView) root.findViewById(R.id.tv_signatureContent);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ContactBean contact= mPresenter.getContact();
        if(contact.getFriendId()==null){
//            说明不是好友
            return;
        }
        inflater.inflate(R.menu.menu_userinfo, menu);
        starItem = menu.findItem(R.id.menu_item_star);
        if(mPresenter.getContact().getSortKey().charAt(0) == '1'){
            starItem.setTitle("取消星标朋友");
        }else{
            starItem.setTitle("标为星标朋友");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_star:
                if(item.getTitle().equals("取消星标朋友")){
                    starItem.setTitle("标为星标朋友");
                    mPresenter.setIsStarred(false);
                }else{
                    starItem.setTitle("取消星标朋友");
                    mPresenter.setIsStarred(true);
                }
                break;
            case R.id.menu_item_delete:
                mPresenter.deleteFriend();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        btn_add_friend.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        layoutTag.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(UserInfoContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chat:
                mPresenter.chat();
                break;
            case R.id.btn_addFriend:
                mPresenter.addFriend();
                break;
            case R.id.ll_tag:
                Bundle bundle = new Bundle();
                bundle.putString(changeTextActivity.KEY_CONTENT1,tv_remarks.getText().toString());
                bundle.putString(changeTextActivity.KEY_CONTENT2,tvTagContent.getText().toString());
                bundle.putString(changeTextActivity.KEY_HINT1,"设置备注");
                bundle.putString(changeTextActivity.KEY_HINT2,"设置标签");
                bundle.putString(changeTextActivity.KEY_TITLE,"设置标签和标签");
                startActivityForResult(changeTextActivity.class,bundle,CODE_SET_TAG);
        }
    }

    @Override
    public void showBtAddFriend() {
        if(btn_add_friend.getVisibility() == View.GONE){
            btn_add_friend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideBtAddFriend() {
        if(btn_add_friend.getVisibility() == View.VISIBLE){
            btn_add_friend.setVisibility(View.GONE);
        }
    }

    @Override
    public void showBtChat() {
        if(btn_chat.getVisibility() == View.GONE){
            btn_chat.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideBtChat() {
        if(btn_chat.getVisibility() == View.VISIBLE){
            btn_chat.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLayoutTag() {
        if(layoutTag.getVisibility() == View.VISIBLE){
            layoutTag.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserInfo(ContactBean contact) {
        tv_username.setText("账号："+contact.getAccount());
        tv_name.setText("昵称："+contact.getName());
        tv_remarks.setText(contact.getRemarks());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(contact.getImage(), iv_avatar);
        if(contact.getTag() == null){
            tvTagTitle.setText("设置备注和标签");
            tvTagContent.setVisibility(View.GONE);

        }else{
            tvTagTitle.setText("标签");
            tvTagContent.setText(contact.getTag());
        }
        if(contact.getLocation() == null){
            tvLocationContent.setText(contact.getLocation());
        }
        if(contact.getSignature() == null){
            tvSignatureContent.setText(contact.getSignature());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tv_remarks.setText(data.getStringExtra(changeTextActivity.KEY_CONTENT1));
        tvTagContent.setText(data.getStringExtra(changeTextActivity.KEY_CONTENT2));
        isInfoDirty = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isInfoDirty){
            mPresenter.saveRemarksAndTag(tv_remarks.getText().toString(),
                    tvTagContent.getText().toString());
            isInfoDirty = false;

        }
    }
}
