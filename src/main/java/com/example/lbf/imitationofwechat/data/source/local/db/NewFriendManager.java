package com.example.lbf.imitationofwechat.data.source.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.data.source.local.db.dao.DaoMaster;
import com.example.lbf.imitationofwechat.data.source.local.db.dao.DaoSession;
import com.example.lbf.imitationofwechat.data.source.local.db.dao.NewFriendDao;
import com.example.lbf.imitationofwechat.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by Administrator on 2016/4/27.
 */
public class NewFriendManager {

    private DaoMaster.DevOpenHelper openHelper;
    Context mContecxt;
    String uid=null;
    private static HashMap<String, NewFriendManager> daoMap = new HashMap<>();

    /**获取DB实例
     * @param context
     * @return
     */
    public static NewFriendManager getInstance(Context context) {
        User user = BmobUser.getCurrentUser(context, User.class);
        String loginId=user.getObjectId();
        if(TextUtils.isEmpty(loginId)){
            throw new RuntimeException("you must login.");
        }
        NewFriendManager dao = daoMap.get(loginId);
        if (dao == null) {
            dao = new NewFriendManager(context,loginId);
            daoMap.put(loginId, dao);
        }
        return dao;
    }

    private NewFriendManager(Context context, String uId){
        clear();
        this.mContecxt =context.getApplicationContext();
        this.uid=uId;
        String DBName = uId+".demodb";
        this.openHelper = new DaoMaster.DevOpenHelper(mContecxt, DBName, null);
    }

    /**
     * 清空资源
     */
    public void clear() {
        if(openHelper !=null) {
            openHelper.close();
            openHelper = null;
            mContecxt=null;
            uid =null;
        }
    }

    private DaoSession openReadableDb() {
        checkInit();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private DaoSession openWritableDb(){
        checkInit();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private void checkInit(){
        if(openHelper ==null){
            throw new RuntimeException("请初始化db");
        }
    }

    //-------------------------------------------------------------

    /**获取本地所有的邀请信息
     * @return
     */
    public List<NewFriend> getAllNewFriend(){
        NewFriendDao dao =openReadableDb().getNewFriendDao();
        return dao.queryBuilder().orderDesc(NewFriendDao.Properties.Time).list();
    }

    /**创建或更新新朋友信息
     * @param info
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateNewFriend(NewFriend info){
        NewFriendDao dao = openWritableDb().getNewFriendDao();
        NewFriend local = getNewFriend(info.getUid(), info.getTime());
        if(local==null){
            return dao.insertOrReplace(info);
        }else{
            return -1;
        }
    }

    /**
     * 获取本地的好友请求
     * @param uid
     * @param time
     * @return
     */
    private NewFriend getNewFriend(String uid,Long time){
        NewFriendDao dao =  openReadableDb().getNewFriendDao();
        return dao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(uid))
                .where(NewFriendDao.Properties.Time.eq(time)).build().unique();
    }

    /**
     * 是否有新的好友邀请
     * @return
     */
    public boolean hasNewFriendInvitation(){
        List<NewFriend> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取未读的好友邀请
     * @return
     */
    public int getNewInvitationCount(){
        List<NewFriend> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            return infos.size();
        }else{
            return 0;
        }
    }
    /**
     * 获取所有未读未验证的好友请求
     * @return
     */
    private List<NewFriend> getNoVerifyNewFriend(){
        NewFriendDao dao =  openReadableDb().getNewFriendDao();
        return dao.queryBuilder().where(NewFriendDao.Properties.Status.eq(Config.STATUS_VERIFY_NONE))
                .build().list();
    }

    /**
     * 批量更新未读未验证的状态为已读
     */
    public void updateBatchStatus(){
        List<NewFriend> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            int size =infos.size();
            List<NewFriend> all =new ArrayList<>();
            for (int i = 0; i < size; i++) {
                NewFriend msg =infos.get(i);
                msg.setStatus(Config.STATUS_VERIFY_READED);
                all.add(msg);
            }
            insertBatchMessages(infos);
        }
    }

    /**批量插入消息
     * @param msgs
     */
    public  void insertBatchMessages(List<NewFriend> msgs){
        NewFriendDao dao =openWritableDb().getNewFriendDao();
        dao.insertOrReplaceInTx(msgs);
    }

    /**
     * 修改指定好友请求的状态
     * @param friend
     * @param status
     * @return
     */
    public long updateNewFriend(NewFriend friend,int status){
        NewFriendDao dao = openWritableDb().getNewFriendDao();
        friend.setStatus(status);
        return dao.insertOrReplace(friend);
    }

    /**
     * 删除指定的添加请求
     * @param friend
     */
    public void deleteNewFriend(NewFriend friend){
        NewFriendDao dao =openWritableDb().getNewFriendDao();
        dao.delete(friend);
    }

}
