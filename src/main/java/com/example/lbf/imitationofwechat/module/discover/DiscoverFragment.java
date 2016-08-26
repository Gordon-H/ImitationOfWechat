package com.example.lbf.imitationofwechat.module.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.module.moments.MomentsActivity;
import com.example.lbf.imitationofwechat.module.shake.ShakeActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;
import com.example.lbf.imitationofwechat.views.ItemView;

/**
 * Created by lbf on 2016/7/29.
 */
public class DiscoverFragment extends Fragment implements DiscoverContract.View,View.OnClickListener {

    private static final String[] nameList =
            {"朋友圈","扫一扫","摇一摇","附近的人","漂流瓶","游戏"};
    private static final int[] imageList = {
            R.drawable.discover_moments,R.drawable.discover_scan,
            R.drawable.discover_shake,R.drawable.discover_people_nearby,
            R.drawable.discover_message_in_a_bottle, R.drawable.discover_games
    };

    public static final int ITEM_POSITION_MOMENTS = 0;
    public static final int ITEM_POSITION_SCAN = 1;
    public static final int ITEM_POSITION_SHAKE = 2;
    public static final int ITEM_POSITION_PEOPLE_NEARBY = 3;
    public static final int ITEM_POSITION_BOTTLE = 4;
    public static final int ITEM_POSITION_GAMES = 5;

    private Context mContext;
    private DiscoverContract.Presenter mPresenter;
    //    分别表示朋友圈、扫一扫、摇一摇、附近的人、漂流瓶和游戏
    private ItemView[] itemViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_discover, container, false);
        mContext = container.getContext();
        itemViews = new ItemView[nameList.length];
        itemViews[0] = (ItemView) root.findViewById(R.id.view_moments);
        itemViews[1] = (ItemView) root.findViewById(R.id.view_scan);
        itemViews[2] = (ItemView) root.findViewById(R.id.view_shake);
        itemViews[3] = (ItemView) root.findViewById(R.id.view_peopleNearby);
        itemViews[4] = (ItemView) root.findViewById(R.id.view_bottle);
        itemViews[5] = (ItemView) root.findViewById(R.id.view_games);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        for(int i = 0;i<itemViews.length;i++){
            itemViews[i].setTextLeft(nameList[i]);
            itemViews[i].setImageLeft(imageList[i]);
            itemViews[i].setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(DiscoverContract.Presenter presenter) {
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
            case R.id.view_moments:
                startActivity(new Intent(getActivity(), MomentsActivity.class));
                break;
            case R.id.view_shake:
                startActivity(new Intent(getActivity(), ShakeActivity.class));
                break;
            case R.id.view_scan:
            case R.id.view_bottle:
            case R.id.view_peopleNearby:
            case R.id.view_games:
                CommonUtil.showNoImplementText(mContext);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        showItem(v);
    }
}
