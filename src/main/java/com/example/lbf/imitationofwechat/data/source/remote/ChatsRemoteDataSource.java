package com.example.lbf.imitationofwechat.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.beans.CommentBean;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.beans.Friend;
import com.example.lbf.imitationofwechat.beans.MomentBean;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.beans.UserInfoBean;
import com.example.lbf.imitationofwechat.data.source.IRemoteDataSource;
import com.example.lbf.imitationofwechat.Interface.QueryUserListener;
import com.example.lbf.imitationofwechat.data.source.local.ChatsLocalDataSource;
import com.example.lbf.imitationofwechat.util.LogUtil;
import com.example.lbf.imitationofwechat.util.PinYinUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by lbf on 2016/8/11.
 */
public class ChatsRemoteDataSource implements IRemoteDataSource {

    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    private static final String DEFAULT_AVATAR = "drawable://"+ R.drawable.display_picture_default;

    private Context mContext;
    private static ChatsRemoteDataSource INSTANCE;
    private ChatsRemoteDataSource(Context mContext) {
        this.mContext = mContext;
    }
    public static ChatsRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ChatsRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void deleteAccount(int accountId) {

    }

    @Override
    public void addAccount(ContactBean bean) {

    }

    @Override
    public void deleteContact(String userId) {
        Friend friend =new Friend();
        friend.delete(mContext, userId, new DeleteListener() {
            @Override
            public void onSuccess() {
                LogUtil.i("onSuccess");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("onFailure"  +s);
            }
        });
    }

    @Override
    public void addContact(ContactBean bean) {

    }

    @Override
    public void updateContact(ContactBean bean) {
        Friend friend = new Friend();
        friend.setRemarks(bean.getRemarks());
        friend.setTag(bean.getTag());
        friend.update(mContext, bean.getFriendId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                LogUtil.i("update successfully!");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("update failed!");

            }
        });
    }

    @Override
    public List<ContactBean> getContactsList() {
        final List<ContactBean> contactList = new ArrayList<>();
        BmobQuery<Friend> query = new BmobQuery<>();
        final Object o = new Object();
        User user = BmobUser.getCurrentUser(mContext, User.class);
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
        query.order("sortKey");
        query.findObjects(mContext, new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                if (list != null && list.size() > 0) {
                    for(Friend friend:list){
                        User friendUser = friend.getFriendUser();
                        ContactBean bean = new ContactBean(friendUser);
                        bean.setRemarks(friend.getRemarks());
                        bean.setSortKey(friend.getSortKey());
                        bean.setFriendId(friend.getObjectId());
                        bean.setObjectId(BmobIM.getInstance().getCurrentUid());
                        contactList.add(bean);
                    }
                } else {
                    LogUtil.i("暂无联系人");
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.i(s);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                synchronized (o){
                    o.notify();
                }
            }
        });
        try {
            synchronized (o){
                o.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return contactList;
    }


    @Override
    public List<ContactBean> getAccountsList() {
        return null;
    }


    @Override
    public ContactBean getContactInfo(int contactId) {
        return null;
    }

    @Override
    public List<MomentBean> getMomentList(int page) {
        return null;
    }

    @Override
    public ContactBean[] getFavors(int id) {
        return new ContactBean[0];
    }

    @Override
    public CommentBean[] getComments(int id) {
        return new CommentBean[0];
    }

    @Override
    public UserInfoBean getUserInfo() {
        return null;
    }

    @Override
    public void login(String username, String password, final LogInListener listener) {
        if(TextUtils.isEmpty(username)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if(TextUtils.isEmpty(password)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        final User user =new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(BmobUser.getCurrentUser(mContext, User.class), null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(user, new BmobException(i, s));
            }
        });
    }

    @Override
    public void logout() {
        BmobUser.logOut(mContext);
    }

    @Override
    public void register(String username,String name, String password, String pwdagain, final LogInListener listener) {
        if(TextUtils.isEmpty(username)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if(TextUtils.isEmpty(name)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写昵称"));
            return;
        }

        if(TextUtils.isEmpty(password)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        if(TextUtils.isEmpty(pwdagain)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写确认密码"));
            return;
        }
        if(!password.equals(pwdagain)){
            listener.internalDone(new BmobException(CODE_NOT_EQUAL, "两次输入的密码不一致，请重新输入"));
            return;
        }
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAvatar(DEFAULT_AVATAR);
        user.setName(name);
        user.signUp(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(null, null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(null, new BmobException(i, s));
            }
        });
    }

    @Override
    public void queryUsers(String username, int limit, final FindListener<User> listener) {
        BmobQuery<User> query = new BmobQuery<>();
        //去掉当前用户
        try {
            BmobUser user = BmobUser.getCurrentUser(mContext);
            List<ContactBean> contactList = ChatsLocalDataSource.getInstance(mContext).getContactsList();
            List<String> usernameList = new ArrayList<>();
            usernameList.add(user.getUsername());
            for(ContactBean contact:contactList){
                usernameList.add(contact.getAccount());
            }

//            query.addWhereNotEqualTo("username",user.getUsername());
            query.addWhereNotContainedIn("username",usernameList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(mContext, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(CODE_NULL, "找不到该用户");
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }

    @Override
    public void queryUserInfo(String objectId, final QueryUserListener listener) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(mContext, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list!=null && list.size()>0){
                    listener.internalDone(list.get(0), null);
                }else{
                    listener.internalDone(new BmobException(000, "查无此人"));
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.internalDone(new BmobException(i, s));
            }
        });
    }

    @Override
    public void addContact(Friend f, ContactBean friend, SaveListener listener) {
        User user =BmobUser.getCurrentUser(mContext, User.class);
        f.setUser(user);
        f.setFriendUser(new User(friend));
        String sortKey = PinYinUtil.toPinYin(friend.getName());
        if(sortKey.charAt(0) < 'A'||sortKey.charAt(0)>'Z'){
            sortKey = "3"+sortKey;
        }else{
            sortKey = "2"+sortKey;
        }
        f.setSortKey(sortKey);
        f.setRemarks(friend.getName());
        f.save(mContext, listener);
    }

    @Override
    public void deleteContact(ContactBean contact,DeleteListener listener) {
        Friend friend =new Friend();
        friend.delete(mContext,contact.getFriendId(),listener);
    }
}
