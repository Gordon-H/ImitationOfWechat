package com.example.lbf.imatationofwechat.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.example.lbf.imatationofwechat.beans.CommentBean;
import com.example.lbf.imatationofwechat.beans.MomentBean;
import com.example.lbf.imatationofwechat.contacts.ContactsFragment;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.DataUtil;
import com.example.lbf.imatationofwechat.beans.ChatBean;
import com.example.lbf.imatationofwechat.beans.ContactBean;
import com.example.lbf.imatationofwechat.beans.MessageBean;
import com.example.lbf.imatationofwechat.beans.UserInfoBean;
import com.example.lbf.imatationofwechat.data.source.ChatsDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/30.
 */
public class ChatsLocalDataSource implements ChatsDataSource {

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
    public void saveChatsList(List<ChatBean> chatBeanList, int onTopNumber) {
        mSharedPreferences.edit().putInt(DataUtil.PREF_KEY_ON_TOP_NUMBER,onTopNumber).commit();
        database.execSQL("Delete from "+WeChatDBHelper.TABLE_CHATS);
        ContentValues values = new ContentValues();
        for(int i = 0; i<chatBeanList.size();i++){
            ChatBean bean = chatBeanList.get(i);
            values.put(WeChatDBHelper.COL_CHATS_FOREIGN_KEY,bean.getId());
            values.put(WeChatDBHelper.COL_CHATS_MSG,bean.getContent());
            values.put(WeChatDBHelper.COL_CHATS_TYPE,bean.getType());
            values.put(WeChatDBHelper.COL_CHATS_TIME,bean.getTime());
            database.insert(WeChatDBHelper.TABLE_CHATS,null,values);
        }
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
    public void deleteContact(int contactId) {

    }

    @Override
    public void addContact(ContactBean bean) {

    }

    @Override
    public List<ChatBean> getChatsList() {
        int onTopNumber = mSharedPreferences.getInt(DataUtil.PREF_KEY_ON_TOP_NUMBER,0);
        Cursor cursor = database.query(WeChatDBHelper.TABLE_CHATS,null,null,null,null,null,null);
        List<ChatBean> chatBeanList = new ArrayList<>();
        int i = 0;
        if(cursor.moveToFirst()){
            try{
                do{
                    int type = cursor.getInt(cursor.getColumnIndex(WeChatDBHelper.COL_CHATS_TYPE));
                    String content = cursor.getString(cursor.getColumnIndex(WeChatDBHelper.COL_CHATS_MSG));
                    long time = cursor.getLong(cursor.getColumnIndex(WeChatDBHelper.COL_CHATS_TIME));
                    int key = cursor.getInt(cursor.getColumnIndex(WeChatDBHelper.COL_CHATS_FOREIGN_KEY));
                    ChatBean bean = createBeanFromId(type,key);
//                设为置顶
                    if(i++ <onTopNumber){
                        bean.setOnTop(true);
                    }else{
                        bean.setOnTop(false);
                    }
                    bean.setContent(content);
                    bean.setId(key);
                    bean.setTime(time);
                    chatBeanList.add(bean);
                }while(cursor.moveToNext());
            }finally {
                if(cursor!=null){
                    cursor.close();
                }
            }
        }
        return chatBeanList;
    }
    private ChatBean createBeanFromId(int type, int id) {
        final String SELECTION = WeChatDBHelper.COL_CONTACTS_ID+" = ?";
        final String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor;
        String name = "";
        int imageId = 0;
        if(type == WeChatDBHelper.CHAT_TYPE_CONTACT){
            cursor = database.query(WeChatDBHelper.TABLE_CONTACTS,
                    null,SELECTION,selectionArgs,null,null,null);
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(WeChatDBHelper.COL_CONTACTS_REMARKS));
                imageId = cursor.getInt(cursor.getColumnIndex(WeChatDBHelper.COL_CONTACTS_IMAGE));
            }

        }else{
            cursor = database.query(WeChatDBHelper.TABLE_FOLLOW,
                    null,SELECTION,selectionArgs,null,null,null);
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(WeChatDBHelper.COL_FOLLOW_NAME));
                imageId = cursor.getInt(cursor.getColumnIndex(WeChatDBHelper.COL_FOLLOW_IMAGE));
            }
        }
        ChatBean bean = new ChatBean(name,imageId);
        bean.setType(type);
        return bean;
    }

    @Override
    public List<ContactBean> getContactsList() {
        final String[] PROJECTION = new String[]{
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_ID,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_REMARKS,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IMAGE,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_TYPE,
        };
        //    对应的列索引
        final int ID_INDEX = 0;
        final int REMARKS_INDEX = 1;
        final int IMAGE_INDEX = 2;
        final int KEY_INDEX = 3;
        final int TYPE_INDEX = 4;
        final String ORDER_BY = WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY;
        Cursor cursor = database.query(WeChatDBHelper.TABLE_CONTACTS,PROJECTION,null,null,null,null, ORDER_BY);
        List<ContactBean> chatBeanList = new ArrayList<>();
        ContactBean bean;
        int i = 0;
        if(cursor.moveToFirst()){
            try{
                do{
                    int id = cursor.getInt(ID_INDEX);
                    String name = cursor.getString(REMARKS_INDEX);
                    int image = cursor.getInt(IMAGE_INDEX);
                    int type = cursor.getInt(TYPE_INDEX);
                    String key = cursor.getString(KEY_INDEX);
                    String sortKey = getSortKey(key,type);
                    bean = new ContactBean(id, image, name, sortKey);
                    if(sortKey.equals("*")){
//                        如果是星标联系人就移到最前面
                        chatBeanList.add(i++,bean);
                    }else{
                        chatBeanList.add(bean);
                    }
                }while(cursor.moveToNext());
            }finally {
                cursor.close();
            }

        }
        for(i = 0; i< ContactsFragment.DEFAULT_ITEM_NAME.length; i++){
//            添加默认的几项
            bean = new ContactBean(ContactsFragment.DEFAULT_ITEM_IMAGE[i],
                    ContactsFragment.DEFAULT_ITEM_NAME[i]);
            chatBeanList.add(i,bean);
        }
//        添加最后一项用于显示联系人数目
        chatBeanList.add(new ContactBean(0,""));
        return chatBeanList;
    }

    @Override
    public List<ContactBean> getAccountsList() {
        final String[] PROJECTION = new String[]{
                WeChatDBHelper.COL_FOLLOW_ID,
                WeChatDBHelper.COL_FOLLOW_NAME,
                WeChatDBHelper.COL_FOLLOW_IMAGE,
                WeChatDBHelper.COL_FOLLOW_KEY};
        //    对应的列索引
        final int ID_INDEX = 0;
        final int NAME_INDEX = 1;
        final int IMAGE_INDEX = 2;
        final int KEY_INDEX = 3;
        final String ORDER_BY = WeChatDBHelper.COL_FOLLOW_KEY;
        Cursor cursor = database.query(WeChatPersistenceContract.FollowEntry.TABLE_FOLLOW,PROJECTION,null,null,null,null, ORDER_BY);
        List<ContactBean> beanList = new ArrayList<>();
        ContactBean bean;
        if (cursor.moveToFirst()) {
            try {
                do {
                    int id = cursor.getInt(ID_INDEX);
                    String name = cursor.getString(NAME_INDEX);
                    int image = cursor.getInt(IMAGE_INDEX);
                    String sortKey = getSortKey(cursor.getString(KEY_INDEX), WeChatPersistenceContract.ContactsEntry.CONTACT_TYPE_NORMAL);
                    bean = new ContactBean(id, image, name, sortKey);
                    beanList.add(bean);
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }

        }
        //        添加最后一项用于显示联系人数目
        beanList.add(new ContactBean(0,""));
        return beanList;
    }


    @Override
    public Cursor getContactsCursor() {
        final String[] PROJECTION = new String[]{
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_ID,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_REMARKS,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IMAGE,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY,
                WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_TYPE,
        };
        final String ORDER_BY = WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY;
        Cursor cursor = database.query(WeChatDBHelper.TABLE_CONTACTS,PROJECTION,null,null,null,null, ORDER_BY);
        return cursor;
    }

    @Override
    public Cursor getAccountsCursor() {
        final String[] PROJECTION = new String[]{
                WeChatDBHelper.COL_FOLLOW_ID,
                WeChatDBHelper.COL_FOLLOW_NAME,
                WeChatDBHelper.COL_FOLLOW_IMAGE,
                WeChatDBHelper.COL_FOLLOW_KEY};
        //    对应的列索引
        final String ORDER_BY = WeChatDBHelper.COL_FOLLOW_KEY;
        Cursor cursor = database.query(WeChatPersistenceContract.FollowEntry.TABLE_FOLLOW,
                PROJECTION,null,null,null,null, ORDER_BY);
        return cursor;
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
            int image = c.getInt(c.getColumnIndex(
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
        final String[] PROJECTION = new String[]{
                WeChatPersistenceContract.MsgsEntry.COL_MSG_ID,
                WeChatPersistenceContract.MsgsEntry.COL_MSG_CONTENT,
                WeChatPersistenceContract.MsgsEntry.COL_MSG_TYPE,
                WeChatPersistenceContract.MsgsEntry.COL_MSG_SENDER,
                WeChatPersistenceContract.MsgsEntry.COL_MSG_TIME,
                WeChatPersistenceContract.MsgsEntry.COL_MSG_DISPLAY_TIME};
        //    对应的列索引
        final int ID_INDEX = 0;
        final int CONTENT_INDEX = 1;
        final int TYPE_INDEX = 2;
        final int SENDER_INDEX = 3;
        final int TIME_INDEX = 4;
        final int DISPLAY_TIME_INDEX = 5;
//        每一页的消息数
        final int pageItem = 18;
        final String SELECTION = "(" + WeChatPersistenceContract.MsgsEntry.COL_MSG_SENDER + "=? and " +
                WeChatPersistenceContract.MsgsEntry.COL_MSG_RECEIVER + "=? )or (" +
                WeChatPersistenceContract.MsgsEntry.COL_MSG_SENDER + "=? and " +
                WeChatPersistenceContract.MsgsEntry.COL_MSG_RECEIVER + "=?)";
        final String[] SELECTION_ARGS = {String.valueOf(contactId), "0","0", String.valueOf(contactId)};
        List<MessageBean> beanList = new ArrayList<>();
        Cursor cursor = database.query(WeChatPersistenceContract.MsgsEntry.TABLE_MSG, PROJECTION,
                SELECTION, SELECTION_ARGS, null, null,
                WeChatPersistenceContract.MsgsEntry.COL_MSG_TIME + " DESC",
                pageItem * page + "," + pageItem);
        if (cursor.moveToFirst()) {
            try {
                do {
                    int id = cursor.getInt(ID_INDEX);
                    String content = cursor.getString(CONTENT_INDEX);
                    int type = cursor.getInt(TYPE_INDEX);
                    long time = cursor.getLong(TIME_INDEX);
                    boolean isSend = cursor.getInt(SENDER_INDEX) == 0;
                    boolean displayTime = cursor.getInt(DISPLAY_TIME_INDEX) == 1;
                    MessageBean bean = new MessageBean(id, content, isSend, time, displayTime);
                    bean.setType(type);
                    beanList.add(bean);
                } while (cursor.moveToNext());
            }finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return beanList;
    }

    @Override
    public void saveMessageList(List<MessageBean> beanList, int contactId) {
        for(MessageBean bean:beanList){
            ContentValues values = new ContentValues();
            values.put(WeChatPersistenceContract.MsgsEntry.COL_MSG_CONTENT,bean.getContent());
            values.put(WeChatPersistenceContract.MsgsEntry.COL_MSG_TYPE,bean.getType());
            values.put(WeChatPersistenceContract.MsgsEntry.COL_MSG_TIME,bean.getTime());
            if(bean.isSend()){
                values.put(WeChatPersistenceContract.MsgsEntry.COL_MSG_SENDER,0);
                values.put(WeChatPersistenceContract.MsgsEntry.COL_MSG_RECEIVER,contactId);
            }else{
                values.put(WeChatPersistenceContract.MsgsEntry.COL_MSG_SENDER,contactId);
                values.put(WeChatPersistenceContract.MsgsEntry.COL_MSG_RECEIVER,0);
            }
            values.put(WeChatPersistenceContract.MsgsEntry.COL_MSG_DISPLAY_TIME,bean.isDisPlaytime());
            database.insert(WeChatPersistenceContract.MsgsEntry.TABLE_MSG,null,values);
        }

    }

    @Override
    public List<MomentBean> getMomentList(int page) {
        List<MomentBean> beanList = new ArrayList<>();
//        首项为header
        beanList.add(new MomentBean());
        Cursor cursor = database.query(WeChatDBHelper.TABLE_MOMENTS,null,null,null,null,null,
                WeChatDBHelper.COL_MOMENTS_TIME+ " DESC",page*15+",15");
        if (cursor.moveToFirst()) {
            try{
                do{
                    int id = cursor.getInt(cursor.getColumnIndex(WeChatDBHelper.COL_MOMENTS_ID));
                    int contactId = cursor.getInt(cursor.getColumnIndex(WeChatDBHelper.COL_MOMENTS_CONTACT_ID));
                    int time = cursor.getInt(cursor.getColumnIndex(WeChatDBHelper.COL_MOMENTS_TIME));
                    String content = cursor.getString(cursor.getColumnIndex(WeChatDBHelper.COL_MOMENTS_CONTENT));
                    String imageStr = cursor.getString(cursor.getColumnIndex(WeChatDBHelper.COL_MOMENTS_IMAGES));
                    String link = cursor.getString(cursor.getColumnIndex(WeChatDBHelper.COL_MOMENTS_LINK));
                    ContactBean contactBean = getContactInfo(contactId);
                    ContactBean[] favors = getFavors(id);
                    CommentBean[] comments = getComments(id);
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
        final String SELECTION = WeChatDBHelper.COL_FAVORS_MOMENT_ID+" = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = database.query(WeChatDBHelper.TABLE_FAVORS,null,
                SELECTION, selectionArgs,null,null,null);
        ContactBean[] contactBeans = new ContactBean[c.getCount()];
        int i = 0;
        if(c.moveToFirst()){
            try{
                do{
                    int contactId = c.getInt(c.getColumnIndex(WeChatDBHelper.COL_FAVORS_CONTACT_ID));
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
        final String SELECTION = WeChatDBHelper.COL_COMMENTS_MOMENT_ID+" = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = database.query(WeChatDBHelper.TABLE_COMMENTS,null,
                SELECTION, selectionArgs,null,null,null);
        CommentBean[] commentBeans = new CommentBean[c.getCount()];
        int i = 0;
        if(c.moveToFirst()){
            try{
                do{
                    int contactId = c.getInt(c.getColumnIndex(WeChatDBHelper.COL_COMMENTS_ID));
                    int toId = c.getInt(c.getColumnIndex(WeChatDBHelper.COL_COMMENTS_TO));
                    String content = c.getString(c.getColumnIndex(WeChatDBHelper.COL_COMMENTS_CONTENT));
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
}
