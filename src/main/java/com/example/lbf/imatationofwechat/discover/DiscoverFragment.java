package com.example.lbf.imatationofwechat.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.moments.MomentsActivity;
import com.example.lbf.imatationofwechat.shake.ShakeActivity;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.views.ItemView;

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
    private View viewMoments;
    private ItemView viewScan;
    private ItemView viewShake;
    private ItemView viewPeopleNearby;
    private ItemView viewBottle;
    private View viewGames;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_discover, container, false);
        mContext = container.getContext();
        viewMoments = root.findViewById(R.id.view_discover_moments);
        viewScan = (ItemView) root.findViewById(R.id.view_discover_scan);
        viewShake = (ItemView) root.findViewById(R.id.view_discover_shake);
        viewPeopleNearby = (ItemView) root.findViewById(R.id.view_discover_peopleNearby);
        viewBottle = (ItemView) root.findViewById(R.id.view_discover_bottle);
        viewGames = root.findViewById(R.id.view_discover_games);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        ((ImageView)viewMoments.findViewById(R.id.iv_discover_momentsItemIcon)).setImageResource(imageList[0]);
        ((TextView)viewMoments.findViewById(R.id.tv_discover_momentsItemTitle)).setText(nameList[0]);
        viewScan.setImage(imageList[1]);
        viewScan.setText(nameList[1]);
        viewShake.setImage(imageList[2]);
        viewShake.setText(nameList[2]);
        viewPeopleNearby.setImage(imageList[3]);
        viewPeopleNearby.setText(nameList[3]);
        viewBottle.setImage(imageList[4]);
        viewBottle.setText(nameList[4]);
        ((ImageView)viewGames.findViewById(R.id.iv_discover_gamesItemIcon)).setImageResource(imageList[5]);
        ((TextView)viewGames.findViewById(R.id.tv_discover_gamesItemTitle)).setText(nameList[5]);
        viewMoments.setOnClickListener(this);
        viewScan.setOnClickListener(this);
        viewShake.setOnClickListener(this);
        viewPeopleNearby.setOnClickListener(this);
        viewBottle.setOnClickListener(this);
        viewGames.setOnClickListener(this);
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
            case R.id.view_discover_moments:
                startActivity(new Intent(getActivity(), MomentsActivity.class));
                break;
            case R.id.view_discover_shake:
                startActivity(new Intent(getActivity(), ShakeActivity.class));
                break;
            case R.id.view_discover_scan:
            case R.id.view_discover_bottle:
            case R.id.view_discover_peopleNearby:
            case R.id.view_discover_games:
                CommonUtil.showNoImplementText(mContext);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        showItem(v);
    }
}
