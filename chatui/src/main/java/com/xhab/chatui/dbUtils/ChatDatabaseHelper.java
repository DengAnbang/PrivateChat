package com.xhab.chatui.dbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xhab.chatui.bean.chat.ChatListMessage;
import com.xhab.chatui.bean.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

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
        if ((!message.isMessage())) return;
        chatListDbInsert(message);
        String tableName = mChatDatabase.getChatTableName(message);
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ChatMessage.setContentValues(cv, message);
        long insert = writableDatabase.replace(tableName, null, cv);
    }


    public void updateMsg(ChatMessage message) {
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sentStatus", message.getSentStatus());
        String tableName = mChatDatabase.getChatTableName(message);
        long insert = writableDatabase.update(tableName, cv, "uuid = ?", new String[]{message.getUuid()});
    }

    public List<ChatMessage> chatMsgSelect(List<ChatMessage> oldChatMessages, String targetId) {
        long lastSendTime = System.currentTimeMillis();
        if (oldChatMessages.size() > 0) {
            lastSendTime = oldChatMessages.get(0).getSentTime();
        }
        List<ChatMessage> chatMessages = new ArrayList<>();
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        String tableName = mChatDatabase.getChatTableName(targetId);
        Cursor cursor = writableDatabase.rawQuery("select * from " + tableName + " WHERE  sentTime < ? ", new String[]{lastSendTime + ""});
        while (cursor.moveToNext()) {
            ChatMessage chatMessage1 = ChatMessage.create(cursor);
            chatMessages.add(chatMessage1);
        }
        cursor.close();
        return chatMessages;
    }


    public void chatListDbInsert(ChatMessage message) {
        String chatListTableName = mChatDatabase.getChatListTableName();
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        ChatListMessage chatListMessage = ChatMessage.createChatListMessage(message);
        ContentValues contentValues = chatListMessage.getContentValues(mChatDatabase.getUser_id());
        long insert = writableDatabase.replace(chatListTableName, null, contentValues);
    }

    public void chatListDelete(String another_id) {
        String chatListTableName = mChatDatabase.getChatListTableName();
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        long insert = writableDatabase.delete(chatListTableName, "another_id=?", new String[]{another_id});
    }

    public void chatDbDelete(ChatMessage message, @Nullable ChatMessage lastMessage) {
        if (lastMessage != null) {
            chatListDbInsert(lastMessage);
        }
        String tableName = mChatDatabase.getChatTableName(message);
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        long insert = writableDatabase.delete(tableName, "uuid=?", new String[]{message.getUuid()});
    }

    public List<ChatListMessage> chatListDbSelect() {
        String chatListTableName = mChatDatabase.getChatListTableName();
        SQLiteDatabase writableDatabase = mChatDatabase.getWritableDatabase();
        List<ChatListMessage> chatListMessages = new ArrayList<>();

//        Cursor cursor = writableDatabase.rawQuery("select * from " + tableName + " WHERE senderId= ? AND sentTime < ? limit ? offset ?  order by sentTime desc", new String[]{mChatDatabase.getSenderId(), lastSendTime + "", oldChatMessages.size() + "", "5"});
        Cursor cursor = writableDatabase.rawQuery("select * from " + chatListTableName + " ORDER BY sentTime desc", null);
        while (cursor.moveToNext()) {
            ChatListMessage chatListMessage = ChatListMessage.create(cursor);
            chatListMessages.add(chatListMessage);
        }
        cursor.close();
        return chatListMessages;
    }

}
