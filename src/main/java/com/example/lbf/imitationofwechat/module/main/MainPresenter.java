package com.example.lbf.imitationofwechat.module.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.loader.ContactsLoader;
import com.example.lbf.imitationofwechat.module.chats.ChatsFragment;
import com.example.lbf.imitationofwechat.module.chats.ChatsPresenter;
import com.example.lbf.imitationofwechat.module.contacts.ContactsFragment;
import com.example.lbf.imitationofwechat.module.contacts.ContactsPresenter;
import com.example.lbf.imitationofwechat.module.discover.DiscoverFragment;
import com.example.lbf.imitationofwechat.module.discover.DiscoverPresenter;
import com.example.lbf.imitationofwechat.module.me.MeFragment;
import com.example.lbf.imitationofwechat.module.me.MePresenter;
import com.example.lbf.imitationofwechat.util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class MainPresenter implements MainContract.Presenter {
    private Context mContext;
    private MainContract.View mView;
    private ChatsRepository mRepository;

    public MainPresenter(Context context,ChatsRepository repository, MainContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mRepository = CommonUtil.checkNotNull(repository,"repository cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public Fragment getFragmentForPage(int page) {
        switch (page){
            case MainFragment.TAB_PAGE_CHATS:
                ChatsFragment chatsFragment = new ChatsFragment();
                ChatsPresenter chatsPresenter = new ChatsPresenter(
                        mContext,
                        ((AppCompatActivity)mContext).getSupportLoaderManager(),
                        chatsFragment,
                        mRepository
                );
                return chatsFragment;
            case MainFragment.TAB_PAGE_CONTACTS:
                ContactsFragment contactsFragment = new ContactsFragment();
                ContactsLoader contactsLoader = new ContactsLoader(mContext,mRepository);
                ContactsPresenter contactsPresenter = new ContactsPresenter(
                        ((AppCompatActivity)mContext).getSupportLoaderManager(),
                        contactsLoader,
                        contactsFragment,
                        mRepository
                );
                return contactsFragment;
            case MainFragment.TAB_PAGE_DISCOVER:
                DiscoverFragment discoverFragment = new DiscoverFragment();
                DiscoverPresenter discoverPresenter = new DiscoverPresenter(discoverFragment);
                return discoverFragment;
            case MainFragment.TAB_PAGE_ME:
                MeFragment meFragment = new MeFragment();
                MePresenter mePresenter = new MePresenter(meFragment);
                return meFragment;

        }
        return new Fragment();
    }

    @Override
    public void start() {
    }
}
