package com.xhab.chatui.dbUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.utils.utils.LogUtils;

/**
 * Created by dab on 2021/3/22 16:56
 */
public class ChatDatabase extends SQLiteOpenHelper {
    static String name = "ChatDatabase.db";
    static int dbVersion = 2;
    private int oldVersion = 1;
    private String user_id;

    public ChatDatabase(Context context, String user_id) {
        super(context, name, null, dbVersion);
        this.user_id = user_id;
        initChatListDatabase("chat_" + user_id);
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * 如果是我发送的消息或者收到的群消息,填targetId,如果是不是群消息填且是别人发我的senderId
     */
    public String getChatTableName(ChatMessage message) {
        String special = message.getAnotherId(getUser_id());
        String s;
        s = "chat_" + user_id + "_to_" + special;  //群表名+我发送时候
        initChatDatabase(s);
        return s;
    }

    public String getChatTableName(String targetId) {
        String s;
        s = "chat_" + user_id + "_to_" + targetId;  //群表名+我发送时候
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
//            stringBuilder.append("(id integer primary key autoincrement");
            stringBuilder.append("(uuid varchar(20)");
            stringBuilder.append(",msgType integer");
            stringBuilder.append(",sentStatus integer");
            stringBuilder.append(",senderId varchar(20)");
            stringBuilder.append(",targetId varchar(20)");
            stringBuilder.append(",sentTime integer");
            stringBuilder.append(",duration varchar(20)");
            stringBuilder.append(",displayName varchar(255)");
            stringBuilder.append(",size varchar(20)");
            stringBuilder.append(",isGroup integer");
            stringBuilder.append(",localPath varchar(255)");
            stringBuilder.append(",remoteUrl varchar(255)");
            stringBuilder.append(",extra text");
            stringBuilder.append(",unread integer");
            stringBuilder.append(",msg text");
            stringBuilder.append(",PRIMARY KEY(uuid) ");
            stringBuilder.append(")");
            //创建数据库sql语句 并 执行
            getWritableDatabase().execSQL(stringBuilder.toString());
            if (oldVersion < 2) {
                addColumnName(getWritableDatabase(), tableName, "unread", "integer");
            }
        }
    }

    private void addColumnName(SQLiteDatabase db, String tableName, String columnName, String type) {
        if (checkColumnExists(db, tableName, columnName)) return;
        String sql = "alter table " + tableName + " add column " + columnName + " " + type;
        db.execSQL(sql);
    }

    /**
     * 方法：检查表中某列是否存在
     *
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    private boolean checkColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    private void initChatListDatabase(String tableName) {
        if (!TextUtils.isEmpty(tableName)) {
            StringBuilder stringBuilder = new StringBuilder("create table if not exists ");
            stringBuilder.append(tableName);
//            stringBuilder.append("(id integer primary key autoincrement");
            stringBuilder.append(" (target_id varchar(20)");
            stringBuilder.append(",another_id varchar(20) ");
            stringBuilder.append(",sender_id varchar(20)");
            stringBuilder.append(",msgType integer");
            stringBuilder.append(",target_name varchar(255)");
            stringBuilder.append(",target_portrait varchar(255)");
            stringBuilder.append(",sentTime integer");
            stringBuilder.append(",unread integer");
            stringBuilder.append(",is_group integer");
            stringBuilder.append(",extra text");
            stringBuilder.append(",msg text");
            stringBuilder.append(",PRIMARY KEY( another_id)); ");
            //创建数据库sql语句 并 执行
            getWritableDatabase().execSQL(stringBuilder.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.oldVersion = oldVersion;
        LogUtils.e("oldVersion*****: " + oldVersion);
        LogUtils.e("newVersion*****: " + newVersion);
//        if (oldVersion<2){
//            db.execSQL("alter table Book add column category_id integer");
//            db.execSQL("alter table Book drop column category_id");
//
//        }


    }
}
