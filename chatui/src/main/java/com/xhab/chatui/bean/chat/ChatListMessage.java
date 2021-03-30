package com.xhab.chatui.bean.chat;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.Expose;


/**
 * Created by dab on 2021/3/19 17:50
 */


public class ChatListMessage {
    @Expose()
    private int id;
    private String target_id;
    private String sender_id;
    @MsgType
    private int msgType;
    private String target_name;
    private String target_portrait = "";
    private long sentTime;
    private int unread;
    private int is_group;
    private String extra;
    private String msg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getTarget_name() {
        return target_name == null ? target_id : "";
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    public String getTarget_portrait() {
        return target_portrait;
    }

    public void setTarget_portrait(String target_portrait) {
        this.target_portrait = target_portrait;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getIs_group() {
        return is_group;
    }

    public void setIs_group(int is_group) {
        this.is_group = is_group;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getAnotherId(String userId) {
        if (getTarget_id().equals(userId)) {
            return getSender_id();
        }
        return getTarget_id();
    }

    public static ChatListMessage create(Cursor cursor) {
        ChatListMessage chatMessage = new ChatListMessage();
        chatMessage.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
        chatMessage.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
        chatMessage.setTarget_id(cursor.getString(cursor.getColumnIndex("target_id")));
        chatMessage.setSender_id(cursor.getString(cursor.getColumnIndex("sender_id")));
        chatMessage.setTarget_name(cursor.getString(cursor.getColumnIndex("target_name")));
        chatMessage.setTarget_portrait(cursor.getString(cursor.getColumnIndex("target_portrait")));
        chatMessage.setExtra(cursor.getString(cursor.getColumnIndex("extra")));
        chatMessage.setSentTime(cursor.getLong(cursor.getColumnIndex("sentTime")));
        chatMessage.setUnread(cursor.getInt(cursor.getColumnIndex("unread")));
        chatMessage.setIs_group(cursor.getInt(cursor.getColumnIndex("is_group")));
        return chatMessage;
    }


    public ContentValues getContentValues(String user_id) {
        ContentValues cv = new ContentValues();
        cv.put("target_id", getTarget_id());
        cv.put("sender_id", getSender_id());
        cv.put("sender_id", getSender_id());
        cv.put("another_id", getAnotherId(user_id));
        cv.put("msgType", getMsgType());
        cv.put("sentTime", getSentTime());
        cv.put("msg", getMsg());
        cv.put("extra", getExtra());
        cv.put("is_group", getIs_group());
        cv.put("unread", getUnread());
        cv.put("target_name", getTarget_name());
        cv.put("target_portrait", getTarget_portrait());
        return cv;
    }
}
