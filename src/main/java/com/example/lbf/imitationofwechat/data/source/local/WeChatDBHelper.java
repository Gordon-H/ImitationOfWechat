package com.example.lbf.imitationofwechat.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.util.DataUtil;
import com.example.lbf.imitationofwechat.util.PinYinUtil;
/**
 * Created by lbf on 2016/6/28.
 */
public class WeChatDBHelper  extends SQLiteOpenHelper{


    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "WeChat.db";

//    建表SQL语句

    private static final String CREATE_TABLE_CONTACTS = "create table "+WeChatPersistenceContract.ContactsEntry.TABLE_CONTACTS+"("
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_ID +" integer primary key autoincrement ,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_ACCOUNT+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_NAME+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_REMARKS+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY+" text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IMAGE+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_OBJECT_ID+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_USER_ID+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_FRIEND_ID+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_LOCATION+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_SIGNATURE+ " text,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IS_MALE+ " integer,"
        +WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_TAG + " text)";
    private static final String CREATE_TABLE_MOMENTS = "create table "+WeChatPersistenceContract.MomentsEntry.TABLE_MOMENTS+"("
        +WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_ID +" integer primary key autoincrement ,"
        +WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_CONTACT_ID+ " integer,"
        +WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_CONTENT+ " text,"
        +WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_IMAGES+ " text,"
        +WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_LINK+" text,"
        +WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_TIME+ " integer)";
     private static final String CREATE_TABLE_COMMENTS = "create table "+WeChatPersistenceContract.CommentsEntry.TABLE_COMMENTS+"("
        +WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_ID +" integer primary key autoincrement ,"
        +WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_CONTACT_ID+ " integer,"
        +WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_MOMENT_ID+ " integer,"
        +WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_CONTENT+ " text,"
        +WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_TO+" integer)";
     private static final String CREATE_TABLE_FAVORS = "create table "+WeChatPersistenceContract.FavorsEntry.TABLE_FAVORS+"("
        +WeChatPersistenceContract.FavorsEntry.COL_FAVORS_ID +" integer primary key autoincrement ,"
        +WeChatPersistenceContract.FavorsEntry.COL_FAVORS_CONTACT_ID+ " integer,"
        +WeChatPersistenceContract.FavorsEntry.COL_FAVORS_MOMENT_ID+ " integer)";

    private static final String CREATE_TABLE_FOLLOW = "create table "+WeChatPersistenceContract.FollowEntry.TABLE_FOLLOW+"("
        +WeChatPersistenceContract.FollowEntry.COL_FOLLOW_ID +" integer primary key autoincrement ,"
        +WeChatPersistenceContract.FollowEntry.COL_FOLLOW_ACCOUNT+ " text,"
        +WeChatPersistenceContract.FollowEntry.COL_FOLLOW_NAME+ " text,"
        +WeChatPersistenceContract.FollowEntry.COL_FOLLOW_KEY+" text,"
        +WeChatPersistenceContract.FollowEntry.COL_FOLLOW_IMAGE+ " text)";


//    一些初始的数据
    public static String[] contactNames = {"Tom","Peter","John","小丽","小薇","Rose","皇冠","扇子？"};
    private static String[] followNames = {"腾讯新闻","Google","订阅号", "腾讯爱心","腾讯浏览器"};
    public static String[] contactImages = {"drawable://"+R.drawable.avatar_person1,
            "drawable://"+R.drawable.avatar_person2,
            "drawable://"+R.drawable.avatar_person3,"drawable://"+ R.drawable.avatar_person4,
            "drawable://"+R.drawable.avatar_person5, "drawable://"+R.drawable.avatar_person6,
            "drawable://"+R.drawable.avatar_person7, "drawable://"+R.drawable.avatar_person8};
    public static int[] followImages = {R.drawable.avatar_tencent_news,R.drawable.avatar_google,
            R.drawable.avatar_subscription,R.drawable.avatar_tencent_love,
            R.drawable.avatar_tentcent_browser};
    public static String[] moments = new String[]{"昨天微信上收到一条打招呼的，打开一看，是个MM。\n" +
            "　　MM：“好无聊啊！”\n" +
            "　　我：“我也好无聊啊！”（期待她叫我出去玩。）\n" +
            "　　MM：“无聊的话你放个屁追着玩啊！”\n" +
            "　　我二话不说就把她给删了。",
            "今天天气真好",
            "钓鱼岛是中国的！！！",
            "最爱你的人是我，你怎么舍得我难过~~",
            "这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈"};
    public static String[] comments = {"今天天气真好啊！！！！",
            "顶！",
            "不要迷恋哥，哥只是个传说！",
            "论装逼，我不是针对谁！",
            "这是一条很长很长的评论这是一条很长很长的评论这是一条很长很长的评论这是一条很长很长的评论"};
    public static String link = R.drawable.avatar_google+"-快点来使用Google+吧！-"+"http://www.baidu.com";
    private Context context;
    public WeChatDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_FOLLOW);
        db.execSQL(CREATE_TABLE_MOMENTS);
        db.execSQL(CREATE_TABLE_COMMENTS);
        db.execSQL(CREATE_TABLE_FAVORS);
        initDataBase(db);
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                DataUtil.PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DataUtil.PREF_KEY_ON_TOP_NUMBER,3).commit();
    }

    private void initDataBase(SQLiteDatabase db) {
//        插入一些初始数据到数据库中
        ContentValues values = new ContentValues();
        ContentValues values4 = new ContentValues();
        ContentValues values5 = new ContentValues();
        ContentValues values6 = new ContentValues();
        ContentValues values7 = new ContentValues();
        ContentValues values8 = new ContentValues();
        for(int i = 0;i<contactNames.length;i++){
//            插入初始联系人数据
            values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_NAME,contactNames[i]);
            values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_REMARKS,contactNames[i]);
            values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IMAGE,contactImages[i]);
            values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY, "*"+ PinYinUtil.toPinYin(contactNames[i]));
//            db.insert(WeChatPersistenceContract.ContactsEntry.TABLE_CONTACTS,null,values);
//            插入初始消息数据

        }
        for(int i = 0;i<followImages.length;i++){
            values4.put(WeChatPersistenceContract.FollowEntry.COL_FOLLOW_NAME,followNames[i]);
            values4.put(WeChatPersistenceContract.FollowEntry.COL_FOLLOW_ACCOUNT,followNames[i]);
            values4.put(WeChatPersistenceContract.FollowEntry.COL_FOLLOW_IMAGE,followImages[i]);
            values4.put(WeChatPersistenceContract.FollowEntry.COL_FOLLOW_KEY, PinYinUtil.toPinYin(followNames[i]));
            db.insert(WeChatPersistenceContract.FollowEntry.TABLE_FOLLOW,null,values4);

        }
        StringBuilder builder = new StringBuilder();
//        插入30个名字随机的联系人
        for(int i = 0;i<30;i++){
            int n1 = (int) (Math.random()*26);
            int n2 = (int) (Math.random()*26);
            int n3 = (int) (Math.random()*26);
            builder.delete(0,builder.length());
            builder.append((char)(n1+'A'));
            builder.append((char)(n2+'A'));
            builder.append((char)(n3+'A'));
            String name = builder.toString();

            values4.put(WeChatPersistenceContract.FollowEntry.COL_FOLLOW_NAME,name);
            values4.put(WeChatPersistenceContract.FollowEntry.COL_FOLLOW_ACCOUNT,name);
            values4.put(WeChatPersistenceContract.FollowEntry.COL_FOLLOW_IMAGE,R.drawable.display_picture_default);
            values4.put(WeChatPersistenceContract.FollowEntry.COL_FOLLOW_KEY, PinYinUtil.toPinYin(name));
            db.insert(WeChatPersistenceContract.FollowEntry.TABLE_FOLLOW,null,values4);

            values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_NAME,name);
            values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_REMARKS,name);
            values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_IMAGE,"drawable://"+R.drawable.display_picture_default);
            values.put(WeChatPersistenceContract.ContactsEntry.COL_CONTACTS_KEY, PinYinUtil.toPinYin(name));
//            db.insert(WeChatPersistenceContract.ContactsEntry.TABLE_CONTACTS,null,values);
        }
        values5.put(WeChatPersistenceContract.UserEntry.COL_USER_ACCOUNT,"136xxxxxxxx");
        values5.put(WeChatPersistenceContract.UserEntry.COL_USER_GENDER,"男");
        values5.put(WeChatPersistenceContract.UserEntry.COL_USER_IMAGE,R.drawable.me_avatar);
        values5.put(WeChatPersistenceContract.UserEntry.COL_USER_NAME,"Gordon");
        values5.put(WeChatPersistenceContract.UserEntry.COL_USER_QR_CODE,R.drawable.me_qr_code);
        values5.put(WeChatPersistenceContract.UserEntry.COL_USER_SIGNATURE,"好好学习，天天向上");
        values5.put(WeChatPersistenceContract.UserEntry.COL_USER_REGION,"中国");
        db.insert(WeChatPersistenceContract.UserEntry.TABLE_USER,null,values5);
        String[] momentContents = new String[25];
        for(int i = 0;i<5;i++){
            momentContents[i] = moments[i];
        }
        for(int i = 0;i<20;i++){
            momentContents[5+i] = "朋友圈"+i;
        }
        for(int i = 0;i<momentContents.length;i++){
            values6.put(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_CONTACT_ID,i+1);
            values6.put(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_TIME,System.currentTimeMillis()-1000*1000*i);
            if(i %5== 0){
                values6.put(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_LINK,link);
            } else{
                values6.put(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_LINK,"");
            }
            values6.put(WeChatPersistenceContract.MomentsEntry.COL_MOMENTS_CONTENT,momentContents[i]);
            db.insert(WeChatPersistenceContract.MomentsEntry.TABLE_MOMENTS,null,values6);
            for(int j = 0;j<4;j++){
                int rand1 = (int)(Math.random()*20+1);
                int rand2 = (int)(Math.random()*4+1);
                values7.put(WeChatPersistenceContract.FavorsEntry.COL_FAVORS_MOMENT_ID,i+1);
                values7.put(WeChatPersistenceContract.FavorsEntry.COL_FAVORS_CONTACT_ID,rand1);
                values8.put(WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_MOMENT_ID,i+1);
                values8.put(WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_CONTENT,comments[rand2]);
                values8.put(WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_CONTACT_ID,rand1);
                values8.put(WeChatPersistenceContract.CommentsEntry.COL_COMMENTS_TO,-1);
                db.insert(WeChatPersistenceContract.FavorsEntry.TABLE_FAVORS,null,values7);
                db.insert(WeChatPersistenceContract.CommentsEntry.TABLE_COMMENTS,null,values8);
            }

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
