package com.example.lbf.imatationofwechat.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.DataUtil;
import com.example.lbf.imatationofwechat.util.PinYinUtil;

/**
 * Created by lbf on 2016/6/28.
 */
public class WeChatDBHelper  extends SQLiteOpenHelper{
//    会话类型：联系人和公众号
    public static final int CHAT_TYPE_CONTACT = 1;
    public static final int CHAT_TYPE_OFFICIAL_ACCOUNT = 2;
//    联系人类型：普通联系人和星标联系人
    public static final int CONTACT_TYPE_NORMAL = 1;
    public static final int CONTACT_TYPE_STARRED = 2;
//    消息类型：文本和图片
    public static final int MSG_TYPE_TEXT = 0;
    public static final int MSG_TYPE_IMAGE = 1;

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "WeChat.db";
    public static final String TABLE_CONTACTS = "Contacts";
    public static final String TABLE_MSG = "Msg";
    public static final String TABLE_FOLLOW = "Follow";
    public static final String TABLE_USER = "User";
    public static final String TABLE_CHATS = "Chats";
    public static final String TABLE_MOMENTS = "Moments";
    public static final String TABLE_COMMENTS = "Comments";
    public static final String TABLE_FAVORS = "Favors";

//    Contacts表的列名,分别为ID，账号，昵称，备注,图片，标签和键
    public static final String COL_CONTACTS_ID = "_id";
    public static final String COL_CONTACTS_ACCOUNT = "account";
    public static final String COL_CONTACTS_NAME = "name";
    public static final String COL_CONTACTS_REMARKS = "remarks";
    public static final String COL_CONTACTS_IMAGE = "image";
    public static final String COL_CONTACTS_TYPE = "type";
    public static final String COL_CONTACTS_TAG = "tag";
    public static final String COL_CONTACTS_KEY = "key";
    //    Chats表的列名,分别为ID，外键ID，类型，名称，消息和时间
    public static final String COL_CHATS_ID = "_id";
    public static final String COL_CHATS_FOREIGN_KEY = "key";
    public static final String COL_CHATS_TYPE = "type";
    public static final String COL_CHATS_MSG = "msg";
    public static final String COL_CHATS_TIME = "time";

    public static final String COL_MSG_ID = "_id";
    public static final String COL_MSG_SENDER = "sender";
    public static final String COL_MSG_RECEIVER = "receiver";
    public static final String COL_MSG_CONTENT = "content";
    public static final String COL_MSG_TYPE = "type";
    public static final String COL_MSG_TIME = "time";
    public static final String COL_MSG_DISPLAY_TIME = "display_time";
//    follow表的列名,分别为ID，账号，名称,图片和键
    public static final String COL_FOLLOW_ID = "_id";
    public static final String COL_FOLLOW_ACCOUNT = "account";
    public static final String COL_FOLLOW_NAME = "name";
    public static final String COL_FOLLOW_IMAGE = "image";
    public static final String COL_FOLLOW_KEY = "key";
//    user表的列名,分别为ID，账号，名称,头像，二维码图片，性别，地区和个性签名
    public static final String COL_USER_ID = "_id";
    public static final String COL_USER_ACCOUNT = "account";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_IMAGE = "image";
    public static final String COL_USER_QR_CODE = "qr_code";
    public static final String COL_USER_GENDER = "gender";
    public static final String COL_USER_REGION = "region";
    public static final String COL_USER_SIGNATURE = "signature";

    public static final String COL_MOMENTS_ID = "_id";
    public static final String COL_MOMENTS_CONTACT_ID = "contact_id";
    public static final String COL_MOMENTS_CONTENT = "content";
    public static final String COL_MOMENTS_IMAGES = "images";
    public static final String COL_MOMENTS_LINK = "link";
    public static final String COL_MOMENTS_TIME = "time";

    public static final String COL_COMMENTS_ID = "_id";
    public static final String COL_COMMENTS_CONTACT_ID = "contact_id";
    public static final String COL_COMMENTS_MOMENT_ID = "moment_id";
    public static final String COL_COMMENTS_CONTENT = "content";
    public static final String COL_COMMENTS_TO = "to_id";

    public static final String COL_FAVORS_ID = "_id";
    public static final String COL_FAVORS_CONTACT_ID = "contact_id";
    public static final String COL_FAVORS_MOMENT_ID = "moment_id";


//    建表SQL语句
    private static final String CREATE_TABLE_USER = "create table "+TABLE_USER+"("
        +COL_USER_ID +" integer primary key autoincrement ,"
        +COL_USER_ACCOUNT+ " text,"
        +COL_USER_NAME+ " text,"
        +COL_USER_IMAGE+ " integer,"
        +COL_USER_GENDER+ " integer,"
        +COL_USER_REGION+ " text,"
        +COL_USER_SIGNATURE+ " text,"
        +COL_USER_QR_CODE + " integer)";
    private static final String CREATE_TABLE_CONTACTS = "create table "+TABLE_CONTACTS+"("
        +COL_CONTACTS_ID +" integer primary key autoincrement ,"
        +COL_CONTACTS_ACCOUNT+ " text,"
        +COL_CONTACTS_NAME+ " text,"
        +COL_CONTACTS_REMARKS+ " text,"
        +COL_CONTACTS_KEY+" text,"
        +COL_CONTACTS_IMAGE+ " integer,"
        +COL_CONTACTS_TYPE+ " integer,"
        + COL_CONTACTS_TAG + " text)";
    private static final String CREATE_TABLE_MOMENTS = "create table "+TABLE_MOMENTS+"("
        +COL_MOMENTS_ID +" integer primary key autoincrement ,"
        +COL_MOMENTS_CONTACT_ID+ " integer,"
        +COL_MOMENTS_CONTENT+ " text,"
        +COL_MOMENTS_IMAGES+ " text,"
        +COL_MOMENTS_LINK+" text,"
        +COL_MOMENTS_TIME+ " integer)";
     private static final String CREATE_TABLE_COMMENTS = "create table "+TABLE_COMMENTS+"("
        +COL_COMMENTS_ID +" integer primary key autoincrement ,"
        +COL_COMMENTS_CONTACT_ID+ " integer,"
        +COL_COMMENTS_MOMENT_ID+ " integer,"
        +COL_COMMENTS_CONTENT+ " text,"
        +COL_COMMENTS_TO+" integer)";
     private static final String CREATE_TABLE_FAVORS = "create table "+TABLE_FAVORS+"("
        +COL_FAVORS_ID +" integer primary key autoincrement ,"
        +COL_FAVORS_CONTACT_ID+ " integer,"
        +COL_FAVORS_MOMENT_ID+ " integer)";
    private static final String CREATE_TABLE_CHATS = "create table "+TABLE_CHATS+"("
        +COL_CHATS_ID +" integer primary key autoincrement ,"
        +COL_CHATS_TYPE+ " integer,"
        +COL_CHATS_FOREIGN_KEY +" text,"
        +COL_CHATS_MSG+ " text,"
        +COL_CHATS_TIME + " integer)";
    private static final String CREATE_TABLE_MSG = "create table "+TABLE_MSG+"("
        +COL_MSG_ID +" integer primary key autoincrement ,"
        +COL_MSG_SENDER+ " integer,"
        +COL_MSG_RECEIVER+ " integer,"
        +COL_MSG_CONTENT+ " text,"
        +COL_MSG_TYPE+ " integer,"
        +COL_MSG_TIME+" integer,"
        +COL_MSG_DISPLAY_TIME+" integer)";
    private static final String CREATE_TABLE_FOLLOW = "create table "+TABLE_FOLLOW+"("
        +COL_FOLLOW_ID +" integer primary key autoincrement ,"
        +COL_FOLLOW_ACCOUNT+ " text,"
        +COL_FOLLOW_NAME+ " text,"
        +COL_FOLLOW_KEY+" text,"
        +COL_FOLLOW_IMAGE+ " integer)";


//    一些初始的数据
    private String[] contactNames = {"Tom","Peter","John","小丽","小薇","Rose","皇冠","扇子？"};
    private String[] followNames = {"腾讯新闻","Google","订阅号", "腾讯爱心","腾讯浏览器"};
    private int[] contactImages = {R.drawable.avatar_person1,R.drawable.avatar_person2,
            R.drawable.avatar_person3, R.drawable.avatar_person4,
            R.drawable.avatar_person5, R.drawable.avatar_person6,
            R.drawable.avatar_person7, R.drawable.avatar_person8};
    private int[] followImages = {R.drawable.avatar_tencent_news,R.drawable.avatar_google,
            R.drawable.avatar_subscription,R.drawable.avatar_tencent_love,
            R.drawable.avatar_tentcent_browser};
    private String[] moments = new String[]{"昨天微信上收到一条打招呼的，打开一看，是个MM。\n" +
            "　　MM：“好无聊啊！”\n" +
            "　　我：“我也好无聊啊！”（期待她叫我出去玩。）\n" +
            "　　MM：“无聊的话你放个屁追着玩啊！”\n" +
            "　　我二话不说就把她给删了。",
            "今天天气真好",
            "钓鱼岛是中国的！！！",
            "最爱你的人是我，你怎么舍得我难过~~",
            "这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈这是一条很长很长很长很长的朋友圈"};
    private String[] comments = {"今天天气真好啊！！！！",
            "顶！",
            "不要迷恋哥，哥只是个传说！",
            "论装逼，我不是针对谁！",
            "这是一条很长很长的评论这是一条很长很长的评论这是一条很长很长的评论这是一条很长很长的评论"};
    private String link = R.drawable.avatar_google+"-快点来使用Google+吧！-"+"http://www.baidu.com";
    private Context context;
    public WeChatDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_CHATS);
        db.execSQL(CREATE_TABLE_FOLLOW);
        db.execSQL(CREATE_TABLE_MSG);
        db.execSQL(CREATE_TABLE_USER);
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
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();
        ContentValues values4 = new ContentValues();
        ContentValues values5 = new ContentValues();
        ContentValues values6 = new ContentValues();
        ContentValues values7 = new ContentValues();
        ContentValues values8 = new ContentValues();
        for(int i = 0;i<contactNames.length;i++){
//            插入初始联系人数据
            values.put(COL_CONTACTS_NAME,contactNames[i]);
            values.put(COL_CONTACTS_REMARKS,contactNames[i]);
            values.put(COL_CONTACTS_IMAGE,contactImages[i]);
            values.put(COL_CONTACTS_KEY, "*"+PinYinUtil.toPinYin(contactNames[i]));
            values.put(COL_CONTACTS_TYPE,CONTACT_TYPE_STARRED);
            db.insert(TABLE_CONTACTS,null,values);
//            插入初始聊天页数据
            values2.put(COL_CHATS_MSG, "Hello!!");
            values2.put(COL_CHATS_TYPE, CHAT_TYPE_CONTACT);
            values2.put(COL_CHATS_FOREIGN_KEY, i+1);
            values2.put(COL_CHATS_TIME, System.currentTimeMillis()-1000*1000*i);
            db.insert(TABLE_CHATS,null,values2);
//            插入初始消息数据
            values3.put(COL_MSG_DISPLAY_TIME,1);
            for(int j = 0;j < 50;j++){
                values3.put(COL_MSG_RECEIVER, j%2==0?0:i+1);
                values3.put(COL_MSG_SENDER, j%2==0?i+1:0);
                values3.put(COL_MSG_CONTENT,"Hello!!"+j);
                values3.put(COL_MSG_TIME,System.currentTimeMillis()-(1000*9000*(50-j)));
                db.insert(TABLE_MSG,null,values3);
            }

        }
        for(int i = 0;i<followImages.length;i++){
            values4.put(COL_FOLLOW_NAME,followNames[i]);
            values4.put(COL_FOLLOW_ACCOUNT,followNames[i]);
            values4.put(COL_FOLLOW_IMAGE,followImages[i]);
            values4.put(COL_FOLLOW_KEY, PinYinUtil.toPinYin(followNames[i]));
            db.insert(TABLE_FOLLOW,null,values4);

            values2.put(COL_CHATS_MSG, "送50元现金券！暑期电影福利");
            values2.put(COL_CHATS_TYPE, CHAT_TYPE_OFFICIAL_ACCOUNT);
            values2.put(COL_CHATS_FOREIGN_KEY, i+1);
            values2.put(COL_CHATS_TIME,System.currentTimeMillis()-1000*1000*i);
            db.insert(TABLE_CHATS,null,values2);
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

            values4.put(COL_FOLLOW_NAME,name);
            values4.put(COL_FOLLOW_ACCOUNT,name);
            values4.put(COL_FOLLOW_IMAGE,R.drawable.display_picture_default);
            values4.put(COL_FOLLOW_KEY, PinYinUtil.toPinYin(name));
            db.insert(TABLE_FOLLOW,null,values4);

            values.put(COL_CONTACTS_NAME,name);
            values.put(COL_CONTACTS_REMARKS,name);
            values.put(COL_CONTACTS_IMAGE,R.drawable.display_picture_default);
            values.put(COL_CONTACTS_TYPE,CONTACT_TYPE_NORMAL);
            values.put(COL_CONTACTS_KEY, PinYinUtil.toPinYin(name));
            db.insert(TABLE_CONTACTS,null,values);
        }
        values5.put(COL_USER_ACCOUNT,"136xxxxxxxx");
        values5.put(COL_USER_GENDER,"男");
        values5.put(COL_USER_IMAGE,R.drawable.me_avatar);
        values5.put(COL_USER_NAME,"Gordon");
        values5.put(COL_USER_QR_CODE,R.drawable.me_qr_code);
        values5.put(COL_USER_SIGNATURE,"好好学习，天天向上");
        values5.put(COL_USER_REGION,"中国");
        db.insert(TABLE_USER,null,values5);
        String[] momentContents = new String[25];
        for(int i = 0;i<5;i++){
            momentContents[i] = moments[i];
        }
        for(int i = 0;i<20;i++){
            momentContents[5+i] = "朋友圈"+i;
        }
        for(int i = 0;i<momentContents.length;i++){
            values6.put(COL_MOMENTS_CONTACT_ID,i+1);
            values6.put(COL_MOMENTS_TIME,System.currentTimeMillis()-1000*1000*i);
            if(i %5== 0){
                values6.put(COL_MOMENTS_LINK,link);
            } else{
                values6.put(COL_MOMENTS_LINK,"");
            }
            values6.put(COL_MOMENTS_CONTENT,momentContents[i]);
            db.insert(TABLE_MOMENTS,null,values6);
            for(int j = 0;j<4;j++){
                int rand1 = (int)(Math.random()*20+1);
                int rand2 = (int)(Math.random()*4+1);
                values7.put(COL_FAVORS_MOMENT_ID,i+1);
                values7.put(COL_FAVORS_CONTACT_ID,rand1);
                values8.put(COL_COMMENTS_MOMENT_ID,i+1);
                values8.put(COL_COMMENTS_CONTENT,comments[rand2]);
                values8.put(COL_COMMENTS_CONTACT_ID,rand1);
                values8.put(COL_COMMENTS_TO,-1);
                db.insert(TABLE_FAVORS,null,values7);
                db.insert(TABLE_COMMENTS,null,values8);
            }

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
