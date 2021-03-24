package com.xhab.chatui.dbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xhab.chatui.bean.chat.ChatListMessage;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dab on 2021/3/22 18:01
 */
public class ChatDatabaseHelper {
    private static ChatDatabaseHelper instance;
    private ChatDatabase mChatDatabase;


    private ChatDatabaseHelper(Context context, String user_id) {
        mChatDatabase = new ChatDatabase(context, user_id);
    }

    public static ChatDatabaseHelper get(Context context, String user_id) {
        if (instance == null) {
            synchronized (ChatDatabaseHelper.class) {
                if (instance == null) {
                    instance = new ChatDatabaseHelper(context, user_id);
                }
            }
        }
        return instance;
    }


    public void chatDbInsert(ChatMessage message) {
        if (message.getMsgType() == MsgType.SYSTEM) return;
        String tableName = mChatDatabase.getChatTableName(message.getSenderId(), message.getTargetId());
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uuid", message.getUuid());
        cv.put("msgType", message.getMsgType());
        cv.put("sentStatus", message.getSentStatus());
        cv.put("senderId", message.getSenderId());
        cv.put("targetId", message.getTargetId());
        cv.put("sentTime", message.getSentTime());
        cv.put("duration", message.getDuration());
        cv.put("displayName", message.getDisplayName());
        cv.put("size", message.getSize());
        cv.put("localPath", message.getLocalPath());
        cv.put("remoteUrl", message.getRemoteUrl());
        cv.put("msg", message.getMsg());
        long insert = writableDatabase.insert(tableName, null, cv);
        Log.e("555555555555", "chatDbInsert: " + insert);
    }

    public void updateMsg(String uuid, int sentStatus, String senderId, String targetId) {
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sentStatus", sentStatus);
        String tableName = mChatDatabase.getChatTableName(senderId, targetId);
        long insert = writableDatabase.update(tableName, cv, "uuid = ?", new String[]{uuid});
    }

    public List<ChatMessage> chatMsgSelect(List<ChatMessage> oldChatMessages, String senderId, String targetId) {
        long lastSendTime = System.currentTimeMillis();
        if (oldChatMessages.size() > 0) {
            lastSendTime = oldChatMessages.get(0).getSentTime();
        }
        List<ChatMessage> chatMessages = new ArrayList<>();
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        String tableName = mChatDatabase.getChatTableName(senderId, targetId);
//        Cursor cursor = writableDatabase.rawQuery("select * from " + tableName + " WHERE senderId= ? AND sentTime < ? limit ? offset ?  order by sentTime desc", new String[]{mChatDatabase.getSenderId(), lastSendTime + "", oldChatMessages.size() + "", "5"});
        Cursor cursor = writableDatabase.rawQuery("select * from " + tableName + " WHERE  sentTime < ? ", new String[]{lastSendTime + ""});
        while (cursor.moveToNext()) {
            ChatMessage chatMessage1 = ChatMessage.create(cursor);
            chatMessages.add(chatMessage1);
        }
        cursor.close();
        return chatMessages;
    }


    public void chatListDbInsert(ChatListMessage message) {
        String chatListTableName = mChatDatabase.getChatListTableName();
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("target_id", message.getTarget_id());
        cv.put("msgType", message.getMsgType());
        cv.put("target_name", message.getTarget_name());
        cv.put("target_portrait", message.getTarget_portrait());
        cv.put("sentTime", message.getSentTime());
        cv.put("unread", message.getUnread());
        cv.put("is_group", message.getIs_group());
        cv.put("extra", message.getExtra());
        cv.put("msg", message.getMsg());
        long insert = writableDatabase.insert(chatListTableName, null, cv);
        Log.e("555555555555", "chatListDbInsert: " + insert);
    }
}
