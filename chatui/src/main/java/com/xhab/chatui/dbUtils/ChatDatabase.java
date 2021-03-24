package com.xhab.chatui.dbUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by dab on 2021/3/22 16:56
 */
public class ChatDatabase extends SQLiteOpenHelper {
    static String name = "ChatDatabase.db";
    static int dbVersion = 1;
    private String user_id;

    public ChatDatabase(Context context, String user_id) {
        super(context, name, null, dbVersion);
        this.user_id = user_id;
        initChatListDatabase("chat_" + user_id);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public String getChatTableName(String senderId, String targetId) {
        String s;
        if (user_id.equals(senderId)) {
            s = "chat_" + senderId + "_to_" + targetId;
        } else {
            s = "chat_" + targetId + "_to_" + senderId;
        }

        initChatDatabase(s);
        return s;
    }

    public String getChatListTableName() {
        String s = "chat_" + user_id;
        initChatListDatabase(s);
        return s;
    }

    private void initChatDatabase(String tableName) {
        if (!TextUtils.isEmpty(tableName)) {
            StringBuilder stringBuilder = new StringBuilder("create table if not exists ");
            stringBuilder.append(tableName);
            stringBuilder.append("(id integer primary key autoincrement");
            stringBuilder.append(",uuid varchar(20)");
            stringBuilder.append(",msgType integer");
            stringBuilder.append(",sentStatus integer");
            stringBuilder.append(",senderId varchar(20)");
            stringBuilder.append(",targetId varchar(20)");
            stringBuilder.append(",sentTime integer");
            stringBuilder.append(",duration varchar(20)");
            stringBuilder.append(",displayName varchar(255)");
            stringBuilder.append(",size varchar(20)");
            stringBuilder.append(",localPath varchar(255)");
            stringBuilder.append(",remoteUrl varchar(255)");
            stringBuilder.append(",extra text");
            stringBuilder.append(",msg text");
            stringBuilder.append(")");
            //创建数据库sql语句 并 执行
            getWritableDatabase().execSQL(stringBuilder.toString());
        }
    }

    private void initChatListDatabase(String tableName) {
        if (!TextUtils.isEmpty(tableName)) {
            StringBuilder stringBuilder = new StringBuilder("create table if not exists ");
            stringBuilder.append(tableName);
            stringBuilder.append("(id integer primary key autoincrement");
            stringBuilder.append(",target_id varchar(20)");
            stringBuilder.append(",msgType integer");
            stringBuilder.append(",target_name varchar(255)");
            stringBuilder.append(",target_portrait varchar(255)");
            stringBuilder.append(",sentTime integer");
            stringBuilder.append(",unread integer");
            stringBuilder.append(",is_group integer");
            stringBuilder.append(",extra text");
            stringBuilder.append(",msg text");
            stringBuilder.append(")");
            //创建数据库sql语句 并 执行
            getWritableDatabase().execSQL(stringBuilder.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
