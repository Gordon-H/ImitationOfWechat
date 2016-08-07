package com.example.lbf.imatationofwechat;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/**
 * Created by lbf on 2016/6/26.
 */
public class PlusActionProvider extends ActionProvider {
    private Context context;
    private static final String[] PLUS_ACTION_TITLE = {
            "发起群聊","添加朋友","扫一扫","收付款","帮助与反馈"};
    private static final int[] PLUS_ACTION_ICON = {
            R.drawable.actionbar_group_chat_icon,
            R.drawable.actionbar_new_friend_icon,
            R.drawable.actionbar_scan_icon,
            R.drawable.actionbar_group_chat_icon,
            R.drawable.actionbar_feedback_icon};
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public PlusActionProvider(Context context) {
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
        for(int i = 0;i<5;i++){
            subMenu.add(PLUS_ACTION_TITLE[i]).setIcon(PLUS_ACTION_ICON[i])
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
        }
        super.onPrepareSubMenu(subMenu);
    }
}
