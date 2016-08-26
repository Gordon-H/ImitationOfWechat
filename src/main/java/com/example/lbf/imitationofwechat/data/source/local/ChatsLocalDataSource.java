package com.example.lbf.imitationofwechat.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.example.lbf.imitationofwechat.Interface.QueryUserListener;
import com.example.lbf.imitationofwechat.Interface.UpdateCacheListener;
import com.example.lbf.imitationofwechat.beans.CommentBean;
import com.example.lbf.imitationofwechat.beans.ContactBean;
import com.example.lbf.imitationofwechat.beans.MessageBean;
import com.example.lbf.imitationofwechat.beans.MomentBean;
import com.example.lbf.imitationofwechat.beans.User;
import com.example.lbf.imitationofwechat.beans.UserInfoBean;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;
import com.example.lbf.imitationofwechat.data.source.ILocalDataSource;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.DataUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by lbf on 2016/7/30.
 */
public class ChatsLocalDataSource implements ILocalDataSource {

    private Context mContext;
    private static ChatsLocalDataSource INSTANCE;
    private SQLiteDatabase database;
    private SharedPreferences mSharedPreferences;
    //    存储联系人ID到联系人bean的映射
    private static SparseArray<ContactBean> contactsMap;


    // Prevent direct instantiation.
    private ChatsLocalDataSource(@NonNull Context context) {
        mContext = CommonUtil.checkNotNull(context);
        SQLiteOpenHelper dbHelper = new WeChatDBHelper(context,WeChatDBHelper.DB_NAME,null,WeChatDBHelper.DB_VERSION);
        database = dbHelper.getWritableDatabase();
        mSharedPreferences = context.getSharedPreferences(DataUtil.PREF_NAME,Context.MODE_PRIVATE);
        contactsMap = new SparseArray<>(100);
    }

    public static ChatsLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ChatsLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void saveConversationList(List<BmobIMConversation> chatBeanList) {
//        SDK会自动保存在本地DB
    }

    @Override
    public void deleteAccount(int accountId) {
        final String WHERE = WeChatPersistenceContract.FollowEntry.COL_FOLLOW_ID+"=?";
        final String[] WHERE_ARGS = {String.valueOf(accountId)};
        database.delete(WeChatPersistenceContract.FollowEntry.TABLE_FOLLOW,WHERE,WHERE_ARGS);
    }

    @Override
    public void addAccount(ContactBean bean) {

    }

    @Override
    public void deleteContact(ContactBean contact) {
        final String SELECTION = WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_USER_ID+" = ?";
        final String[] SELECTION_ARGS = {contact.getUserId()};
        database.delete(WeChatPersistenceContract.ContactsEntry.TABLE_CONTACTS,SELECTION,SELECTION_ARGS);
    }

    @Override
    public void addContact(ContactBean contact) {
        ContentValues values = new ContentValues();
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_NAME,contact.getName());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IMAGE,contact.getImage());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY,contact.getSortKey());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_USER_ID,contact.getUserId());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_ACCOUNT,contact.getAccount());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_REMARKS,contact.getRemarks());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_TAG,contact.getTag());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_LOCATION,contact.getLocation());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_SIGNATURE,contact.getSignature());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_FRIEND_ID,contact.getFriendId());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IS_MALE,contact.isMale()?1:0);
        database.insert(WeChatPersistenceContract.ContactsEntry.TABLE_CONTACTS,null,values);
    }

    @Override
    public void updateContact(ContactBean bean) {
        ContentValues values = new ContentValues();
        String userId = bean.getUserId();
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_TAG,bean.getTag());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_REMARKS,bean.getRemarks());
        values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY,bean.getSortKey());
        database.update(WeChatPersistenceContract.ContactsEntry.TABLE_CONTACTS,
                values,"userId = ?",new String[]{userId});
    }

    @Override
    public List<BmobIMConversation> getConversationList() {
        return BmobIM.getInstance().loadAllConversation();
    }

    @Override
    public List<ContactBean> getContactsList() {
        final String[] PROJECTION = new String[]{
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_ACCOUNT,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_REMARKS,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IMAGE,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_USER_ID,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IS_MALE,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_NAME,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_LOCATION,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_SIGNATURE,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_TAG,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_FRIEND_ID,

        };
        //    对应的列索引
        final int ACCOUNT_INDEX = 0;
        final int REMARKS_INDEX = 1;
        final int IMAGE_INDEX = 2;
        final int KEY_INDEX = 3;
        final int USER_ID_INDEX = 4;
        final int IS_MALE_INDEX = 5;
        final int NAME_INDEX = 6;
        final int LOCATION_INDEX = 7;
        final int SIGNATURE_INDEX = 8;
        final int TAG_INDEX = 9;
        final int FRIEND_ID_INDEX = 10;
        final String SELECTION = WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_OBJECT_ID +"=?";
        final String[] SELECTION_ARGS = new String[]{BmobIM.getInstance().getCurrentUid()};
        final String ORDER_BY = WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY;
        Cursor cursor = database.query(WeChatPersistenceContract.ContactsEntry.TABLE_CONTACTS,
                PROJECTION, SELECTION,SELECTION_ARGS,null,null, ORDER_BY);
        List<ContactBean> contactList = new ArrayList<>();
        ContactBean bean;
        if(cursor.moveToFirst()){
            try{
                do{
                    String account = cursor.getString(ACCOUNT_INDEX);
                    String name = cursor.getString(NAME_INDEX);
                    String remarks = cursor.getString(REMARKS_INDEX);
                    String image = cursor.getString(IMAGE_INDEX);
                    String key = cursor.getString(KEY_INDEX);
                    String userId = cursor.getString(USER_ID_INDEX);
                    boolean isMale = cursor.getInt(IS_MALE_INDEX) != 0;
                    String signature = cursor.getString(SIGNATURE_INDEX);
                    String tag = cursor.getString(TAG_INDEX);
                    String location = cursor.getString(LOCATION_INDEX);
                    String friendId = cursor.getString(FRIEND_ID_INDEX);
                    bean = new ContactBean(account, image, name, key,userId, isMale,signature,location,tag);
                    bean.setRemarks(remarks);
                    bean.setFriendId(friendId);
                    contactList.add(bean);
                }while(cursor.moveToNext());
            }finally {
                cursor.close();
            }
        }
        return contactList;
    }

    @Override
    public void saveContactList(List<ContactBean> contactList) {
        for(ContactBean contact:contactList){
            addContact(contact);
        }
    }

    @Override
    public List<ContactBean> getAccountsList() {
        final String[] PROJECTION = new String[]{
                WeChatPersistenceContract.FollowEntry.COL_FOLLOW_ACCOUNT,
                WeChatPersistenceContract.FollowEntry.COL_FOLLOW_NAME,
                WeChatPersistenceContract.FollowEntry.COL_FOLLOW_IMAGE,
                WeChatPersistenceContract.FollowEntry.COL_FOLLOW_KEY};
        //    对应的列索引
        final int ACCOUNT_INDEX = 0;
        final int NAME_INDEX = 1;
        final int IMAGE_INDEX = 2;
        final int KEY_INDEX = 3;
        final String ORDER_BY = WeChatPersistenceContract.FollowEntry.COL_FOLLOW_KEY;
        Cursor cursor = database.query(WeChatPersistenceContract.FollowEntry.TABLE_FOLLOW,PROJECTION,null,null,null,null, ORDER_BY);
        List<ContactBean> beanList = new ArrayList<>();
        ContactBean bean;
        if (cursor.moveToFirst()) {
            try {
                do {
                    String account = cursor.getString(ACCOUNT_INDEX);
                    String name = cursor.getString(NAME_INDEX);
                    String image = cursor.getString(IMAGE_INDEX);
                    String sortKey = getSortKey(cursor.getString(KEY_INDEX), WeChatPersistenceContract.ContactsEntry.CONTACT_TYPE_NORMAL);
                    bean = new ContactBean(account, image, name, sortKey, "",false,"","","");
                    beanList.add(bean);
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }

        }
        //        添加最后一项用于显示联系人数目
        beanList.add(new ContactBean());
        return beanList;
    }

    private String getSortKey(String key,int type) {
        String keyChar = key.substring(0,1);
        if(type == WeChatPersistenceContract.ContactsEntry.CONTACT_TYPE_STARRED){
            return "*";
        }else if(keyChar.matches("[A-Z]")){
            return keyChar;
        }else{
            return "#";
        }
    }

    @Override
    public ContactBean getContactInfo(int contactId) {
        final String SELECTION = WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_ID+" = ?";
        final String[] SELECTION_ARGS = {String.valueOf(contactId)};

        ContactBean contactBean = contactsMap.get(contactId);
        if(contactBean != null){
            return contactBean;
        }
        Cursor c;
        if(contactId == -1){
//            -1用于表示普通评论，直接返回一个空bean即可
            contactBean = new ContactBean();
            contactBean.setId(-1);
            return contactBean;
        }
        if(contactId == 0){
//            id为0表示用户自身
            c = database.query(WeChatPersistenceContract.UserEntry.TABLE_USER,null,
                    null, null,null,null,null);
        }else{
//            否则为普通联系人
            c = database.query(WeChatPersistenceContract.ContactsEntry.TABLE_CONTACTS,null,
                    SELECTION, SELECTION_ARGS,null,null,null);
        }
        if(c.moveToFirst()){
            String image = c.getString(c.getColumnIndex(
                    WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IMAGE));
            String name = c.getString(c.getColumnIndex(
                    WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_NAME));
            contactBean = new ContactBean(image,name);
            contactBean.setId(contactId);
            contactsMap.put(contactId,contactBean);
        }
        return contactBean;

    }

    @Override
    public List<MessageBean> getMessageList(int contactId, int page) {
        return null;
    }

    @Override
    public void saveMessageList(List<MessageBean> beanList, int contactId) {

    }

    @Override
    public List<MomentBean> getMomentList(int page) {
        List<MomentBean> beanList = new ArrayList<>();
//        首项为header
        beanList.add(new MomentBean());
        Cursor cursor = database.query(WeChatPersistenceContract.MomentsEntry.TABLE_MOMENTS,null,null,null,null,null,
                WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_TIME+ " DESC",page*15+",15");
        if (cursor.moveToFirst()) {
            try{
                do{
                    int id = cursor.getInt(cursor.getColumnIndex(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_ID));
                    int contactId = cursor.getInt(cursor.getColumnIndex(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_CONTACT_ID));
                    int time = cursor.getInt(cursor.getColumnIndex(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_TIME));
                    String content = cursor.getString(cursor.getColumnIndex(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_CONTENT));
                    String imageStr = cursor.getString(cursor.getColumnIndex(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_IMAGES));
                    String link = cursor.getString(cursor.getColumnIndex(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_LINK));
//                    ContactBean contactBean = getContactInfo(contactId);
//                    ContactBean[] favors = getFavors(id);
//                    CommentBean[] comments = getComments(id);
                    int randN1 = (int) (Math.random()*8);
                    int randN2 = (int) (Math.random()*8);
                    int randN3 = (int) (Math.random()*8);
                    ContactBean randContact1 = new ContactBean(WeChatDBHelper.contactImages[randN1],WeChatDBHelper.contactNames[randN1]);
                    ContactBean randContact2 = new ContactBean(WeChatDBHelper.contactImages[randN2],WeChatDBHelper.contactNames[randN2]);
                    ContactBean randContact3 = new ContactBean(WeChatDBHelper.contactImages[randN3],WeChatDBHelper.contactNames[randN3]);
                    ContactBean contactBean = randContact1;
                    ContactBean[] favors = new ContactBean[]{randContact1,randContact2,randContact3};
                    int randN4 = (int) (Math.random()*5);
                    int randN5 = (int) (Math.random()*5);
                    CommentBean randComment1 = new CommentBean(WeChatDBHelper.comments[randN4],randContact2,randContact1);
                    CommentBean randComment2 = new CommentBean(WeChatDBHelper.comments[randN5],randContact3,randContact1);
                    CommentBean[] comments = new CommentBean[]{randComment1,randComment2};
                    String[] images = null;
                    if(imageStr!=null){
                        images= imageStr.split(",");
                    }
                    MomentBean bean = new MomentBean(contactBean, content,link,images,favors,comments,time);
                    beanList.add(bean);
                }while(cursor.moveToNext());
            }finally {
                cursor.close();
            }
        }
        return beanList;
    }

    @Override
    public ContactBean[] getFavors(int id) {
        final String SELECTION = WeChatPersistenceContract.FavorsEntry.COL_FAVORS_MOMENT_ID+" = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = database.query(WeChatPersistenceContract.FavorsEntry.TABLE_FAVORS,null,
                SELECTION, selectionArgs,null,null,null);
        ContactBean[] contactBeans = new ContactBean[c.getCount()];
        int i = 0;
        if(c.moveToFirst()){
            try{
                do{
                    int contactId = c.getInt(c.getColumnIndex(WeChatPersistenceContract.FavorsEntry.COL_FAVORS_CONTACT_ID));
                    contactBeans[i++] = getContactInfo(contactId);
                }while(c.moveToNext());
            }finally {
                c.close();
            }

        }
        return contactBeans;
    }

    @Override
    public CommentBean[] getComments(int id) {
        final String SELECTION = WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_MOMENT_ID+" = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = database.query(WeChatPersistenceContract.CommentsEntry.TABLE_COMMENTS,null,
                SELECTION, selectionArgs,null,null,null);
        CommentBean[] commentBeans = new CommentBean[c.getCount()];
        int i = 0;
        if(c.moveToFirst()){
            try{
                do{
                    int contactId = c.getInt(c.getColumnIndex(WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_ID));
                    int toId = c.getInt(c.getColumnIndex(WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_TO));
                    String content = c.getString(c.getColumnIndex(WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_CONTENT));
                    ContactBean from = getContactInfo(contactId);
                    ContactBean to = getContactInfo(toId);
                    commentBeans[i++] = new CommentBean(content,from,to);
                }while(c.moveToNext());
            }finally {
                c.close();
            }

        }
        return commentBeans;
    }

    @Override
    public UserInfoBean getUserInfo() {
        Cursor cursor = database.query(WeChatPersistenceContract.UserEntry.TABLE_USER,null,null,null,null,null,null);
        UserInfoBean bean = null;
        if(cursor.moveToFirst()){
            try {
                String name = cursor.getString(cursor.getColumnIndex(WeChatPersistenceContract.UserEntry.COL_USER_NAME));
                String account = cursor.getString(cursor.getColumnIndex(WeChatPersistenceContract.UserEntry.COL_USER_ACCOUNT));
                int gender = cursor.getInt(cursor.getColumnIndex(WeChatPersistenceContract.UserEntry.COL_USER_GENDER));
                int image = cursor.getInt(cursor.getColumnIndex(WeChatPersistenceContract.UserEntry.COL_USER_IMAGE));
                int qrCode = cursor.getInt(cursor.getColumnIndex(WeChatPersistenceContract.UserEntry.COL_USER_QR_CODE));
                String region = cursor.getString(cursor.getColumnIndex(WeChatPersistenceContract.UserEntry.COL_USER_REGION));
                String signature = cursor.getString(cursor.getColumnIndex(WeChatPersistenceContract.UserEntry.COL_USER_SIGNATURE));
                bean = new UserInfoBean(account,name,image,qrCode,gender,region,signature);
            }finally {
                if(cursor!=null){
                    cursor.close();
                }
            }
        }
        return bean;
    }

    @Override
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
        final BmobIMConversation conversation=event.getConversation();
        final BmobIMUserInfo info =event.getFromUserInfo();
        final BmobIMMessage msg =event.getMessage();
        String username =info.getName();
        String title =conversation.getConversationTitle();
        LogUtil.i("" + username + "," + title);
        //sdk内部，将新会话的会话标题用objectId表示，因此需要比对用户名和会话标题--单聊，后续会根据会话类型进行判断
        if(!username.equals(title)) {
            ChatsRepository.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(User s, BmobException e) {
                    if(e==null){
                        String name =s.getUsername();
                        String avatar = s.getAvatar();
                        LogUtil.i("query success："+name+","+avatar);
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if(!msg.isTransient()){
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    }else{
                        LogUtil.i(String.valueOf(e));
                    }
                    listener.done(null);
                }
            });
        }else{
            listener.internalDone(null);
        }
    }

    @Override
    public User getCurrentUser() {
        return BmobUser.getCurrentUser(mContext, User.class);
    }
}
