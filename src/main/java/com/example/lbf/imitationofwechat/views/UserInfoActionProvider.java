package com.example.lbf.imitationofwechat.views;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.SubMenu;
import android.view.View;

import com.example.lbf.imatationofwechat.R;

/**
 * Created by lbf on 2016/6/26.
 */
public class UserInfoActionProvider extends ActionProvider {
    private Context context;
    private final int SUBMENU_SIZE =2;
    private static final String[] ACTION_TITLES = {
            "标为星标朋友","删除"};
    private static final int[] ACTION_ICONS = {
            R.drawable.actionbar_star,
            R.drawable.actionbar_delete};
    private static final int[] PLUS_ACTION_ID = {
            R.id.menu_item_star,
            R.id.menu_item_delete};
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public UserInfoActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        for(int i = 0;i<SUBMENU_SIZE;i++){
            subMenu.add(1,PLUS_ACTION_ID[i],i, ACTION_TITLES[i]).setIcon(ACTION_ICONS[i]);
        }
        super.onPrepareSubMenu(subMenu);
    }
}
