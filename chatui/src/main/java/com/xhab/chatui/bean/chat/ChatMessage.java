package com.xhab.chatui.bean.chat;

import android.database.Cursor;

import com.google.gson.annotations.Expose;


/**
 * Created by dab on 2021/3/19 17:50
 */


public class ChatMessage {
    @Expose()
    private int id;
    private String uuid;
    @MsgType
    private int msgType;
    @MsgSendStatus
    private int sentStatus;
    private String senderId;
    private String targetId;
    private long sentTime;
    ////////////////////////////////语音
    //语音消息长度,单位：秒。
    private String duration;
    /////////////////////////////////////////文件
    //文件名称,可以和文件名不同，仅用于界面展示
    private String displayName;
    //文件长度(字节)
    private String size;
    //本地文件保存路径
    @Expose()
    private String localPath;
    //文件下载地址
    private String remoteUrl;
    //扩展信息，可以放置任意的数据内容，也可以去掉此属性
    private String extra;//视频消息的时候,是视频完整的源地址
    ///////////////////////////////////////////文件
    private String msg;
    //////////////////////////////////视频


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    @MsgType
    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(@MsgType int msgType) {
        this.msgType = msgType;
    }

    @MsgSendStatus
    public int getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(@MsgSendStatus int sentStatus) {
        this.sentStatus = sentStatus;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public long getSentTime() {
        return sentTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
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

    public static ChatMessage create(Cursor cursor) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(cursor.getInt(cursor.getColumnIndex("id")));
        chatMessage.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
        chatMessage.setSentStatus(cursor.getInt(cursor.getColumnIndex("sentStatus")));
        chatMessage.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));

        chatMessage.setSenderId(cursor.getString(cursor.getColumnIndex("senderId")));
        chatMessage.setTargetId(cursor.getString(cursor.getColumnIndex("targetId")));
        chatMessage.setSentTime(cursor.getLong(cursor.getColumnIndex("sentTime")));
        chatMessage.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
        chatMessage.setDisplayName(cursor.getString(cursor.getColumnIndex("displayName")));
        chatMessage.setSize(cursor.getString(cursor.getColumnIndex("size")));
        chatMessage.setLocalPath(cursor.getString(cursor.getColumnIndex("localPath")));
        chatMessage.setRemoteUrl(cursor.getString(cursor.getColumnIndex("remoteUrl")));
        chatMessage.setMsg(cursor.getString(cursor.getColumnIndex("msg")));

        return chatMessage;
    }

}
