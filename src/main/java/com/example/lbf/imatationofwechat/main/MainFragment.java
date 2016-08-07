package com.example.lbf.imatationofwechat.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.search.SearchActivity;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.views.TabItemView;

/**
 * Created by lbf on 2016/7/29.
 */
public class MainFragment extends Fragment implements View.OnClickListener,MainContract.View {
    public static final int TAB_COUNT = 4;
    public static final int TAB_PAGE_CHATS = 0;
    public static final int TAB_PAGE_CONTACTS = 1;
    public static final int TAB_PAGE_DISCOVER = 2;
    public static final int TAB_PAGE_ME = 3;

    private Context mContext;
    private MainContract.Presenter mPresenter;
    private ViewPager mViewPager;
    private TabItemView[] tabViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main,container,false);
        mContext = container.getContext();
        mViewPager = (ViewPager) root.findViewById(R.id.vp_main);
        tabViews = new TabItemView[4];
        tabViews[0] = ((TabItemView) root.findViewById(R.id.tab_item_chats));
        tabViews[1] = (TabItemView) root.findViewById(R.id.tab_item_contacts);
        tabViews[2] = (TabItemView) root.findViewById(R.id.tab_item_discover);
        tabViews[3] = (TabItemView) root.findViewById(R.id.tab_item_me);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager.setAdapter(new MainFragmentAdapter(getActivity().getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                调整后的偏移量
                int leftOffset = (int) (-256+512*(1-positionOffset));
                int rightOffset = (int) (-256+512*positionOffset);
                if(leftOffset>255){
                    leftOffset = 255;
                }else if(leftOffset<0){
                    leftOffset = 0;
                }
                if(rightOffset>255){
                    rightOffset= 255;
                }else if(rightOffset<0){
                    rightOffset = 0;
                }
                tabViews[position].setViewAlpha(leftOffset);
                if(position<3){
                    tabViews[position+1].setViewAlpha(rightOffset);
                }
            }
        });
        for(TabItemView tabView:tabViews){
            tabView.setOnClickListener(this);
        }
        tabViews[0].setViewAlpha(255);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = CommonUtil.checkNotNull(presenter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String errorMsg) {
        LogUtil.i("error! "+errorMsg);
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void changePage(int page) {
        if(mViewPager.getCurrentItem() == page){
            return;
        }
//        清空选中状态
        for(TabItemView itemView:tabViews){
            itemView.setViewAlpha(0);
        }
        tabViews[page].setViewAlpha(255);
        mViewPager.setCurrentItem(page,false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_item_chats:
                changePage(0);
                break;
            case R.id.tab_item_contacts:
                changePage(1);
                break;
            case R.id.tab_item_discover:
                changePage(2);
                break;
            case R.id.tab_item_me:
                changePage(3);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                startActivity(new Intent(mContext, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public class MainFragmentAdapter extends FragmentPagerAdapter {
        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mPresenter.getFragmentForPage(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
