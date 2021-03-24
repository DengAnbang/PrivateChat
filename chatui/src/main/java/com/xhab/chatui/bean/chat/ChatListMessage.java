package com.xhab.chatui.bean.chat;

import android.database.Cursor;

import com.google.gson.annotations.Expose;


/**
 * Created by dab on 2021/3/19 17:50
 */


public class ChatListMessage {
    @Expose()
    private int id;
    private String target_id;
    @MsgType
    private int msgType;
    private String target_name;
    private String target_portrait;
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
        return target_name;
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

    public static ChatListMessage create(Cursor cursor) {
        ChatListMessage chatMessage = new ChatListMessage();
        chatMessage.setId(cursor.getInt(cursor.getColumnIndex("id")));
        chatMessage.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
        chatMessage.setMsg(cursor.getString(cursor.getColumnIndex("msg")));

        return chatMessage;
    }

}
