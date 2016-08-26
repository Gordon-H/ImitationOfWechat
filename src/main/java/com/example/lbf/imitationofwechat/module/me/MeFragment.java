package com.example.lbf.imitationofwechat.module.me;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseFragment;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.module.login.LoginActivity;
import com.example.lbf.imitationofwechat.module.personalInfo.PersonalInfoActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.views.ItemView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lbf on 2016/7/29.
 */
public class MeFragment extends BaseFragment implements MeContract.View,View.OnClickListener {
    private static final int itemSize = 6;
    private static final String[] nameList =
            {"相册","收藏","钱包","卡券","表情","退出"};
    private static final int[] imageList = {
            R.drawable.me_my_posts,R.drawable.me_favorites,
            R.drawable.me_wallet,R.drawable.me_cards_offers,
            R.drawable.me_striker_gallery, R.drawable.me_setting
    };

    private Context mContext;
    private MeContract.Presenter mPresenter;
    private View viewUserInfo;
    private ImageView ivAvatar;
    private TextView tvName;
    private TextView tvUsername;
    private ItemView[] itemViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_me, container, false);
        mContext = container.getContext();
        viewUserInfo = root.findViewById(R.id.rl_userInfo);
        ivAvatar = (ImageView) root.findViewById(R.id.iv_avatar);
        tvName = (TextView) root.findViewById(R.id.tv_name);
        tvUsername = (TextView) root.findViewById(R.id.tv_username);
        itemViews = new ItemView[itemSize];
        itemViews[0] = (ItemView) root.findViewById(R.id.view_me_myPosts);
        itemViews[1] = (ItemView) root.findViewById(R.id.view_me_favorites);
        itemViews[2] = (ItemView) root.findViewById(R.id.view_me_wallet);
        itemViews[3] = (ItemView) root.findViewById(R.id.view_me_cardsOffers);
        itemViews[4] = (ItemView) root.findViewById(R.id.view_me_striker);
        itemViews[5] = (ItemView) root.findViewById(R.id.view_me_setting);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mPresenter.start();
    }

    private void initView() {
        viewUserInfo.setOnClickListener(this);
        for(int i = 0;i<itemSize;i++){
            itemViews[i].setTextLeft(nameList[i]);
            itemViews[i].setImageLeft(imageList[i]);
            itemViews[i].setOnClickListener(this);
        }
    }

    @Override
    public void setPresenter(MeContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showItem(View v) {
        switch (v.getId()){
            case R.id.rl_userInfo:
                showPersonalInfo();
                break;
            case R.id.view_me_myPosts:
            case R.id.view_me_favorites:
            case R.id.view_me_wallet:
            case R.id.view_me_cardsOffers:
            case R.id.view_me_striker:
                CommonUtil.showNoImplementText(mContext);
                break;
            case R.id.view_me_setting:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提醒")
                        .setMessage("确定退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ChatsRepository.getInstance().logout();
                                startActivity(LoginActivity.class,null);
                                getActivity().finish();
                            }
                        })
                        .setCancelable(true);
                builder.show();
        }
    }

    private void showPersonalInfo() {
        startActivity(PersonalInfoActivity.class,null);
    }

    @Override
    public void setUserInfo(String avatar, String name, String username) {
        ImageLoader.getInstance().displayImage(avatar,ivAvatar);
        tvName.setText(name);
        tvUsername.setText("账号："+username);
    }

    @Override
    public void onClick(View v) {
        showItem(v);
    }
}
