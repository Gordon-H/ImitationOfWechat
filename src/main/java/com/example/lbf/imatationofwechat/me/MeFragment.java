package com.example.lbf.imatationofwechat.me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.views.ItemView;

/**
 * Created by lbf on 2016/7/29.
 */
public class MeFragment extends Fragment implements MeContract.View,View.OnClickListener {
    private static final int itemSize = 6;
    private static final String[] nameList =
            {"相册","收藏","钱包","卡券","表情","设置"};
    private static final int[] imageList = {
            R.drawable.me_my_posts,R.drawable.me_favorites,
            R.drawable.me_wallet,R.drawable.me_cards_offers,
            R.drawable.me_striker_gallery, R.drawable.me_setting
    };

    private Context mContext;
    private MeContract.Presenter mPresenter;
    private ItemView[] itemViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_me, container, false);
        mContext = container.getContext();
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
    }

    private void initView() {
        for(int i = 0;i<itemSize;i++){
            itemViews[i].setText(nameList[i]);
            itemViews[i].setImage(imageList[i]);
            itemViews[i].setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(MeContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void showError(String errorMsg) {
        LogUtil.i("error! " + errorMsg);
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showItem(View v) {
        switch (v.getId()){
            case R.id.view_me_myPosts:
            case R.id.view_me_favorites:
            case R.id.view_me_wallet:
            case R.id.view_me_cardsOffers:
            case R.id.view_me_striker:
            case R.id.view_me_setting:
                CommonUtil.showNoImplementText(mContext);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        showItem(v);
    }
}
