package com.xhab.chatui.bean.chat;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.xhab.chatui.R;

import java.util.UUID;


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
    private boolean isGroup;//是群消息
    private int unread;//未读消息0表示已读,1表示未读
    //////////////////////////////////视频


    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getPlaceholder() {
        return isGroup ? R.mipmap.group_icon : R.mipmap.logo;
    }

    private ChatMessage() {
    }

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

    /**
     * 是否是有效消息(系统消息等等除外)
     *
     * @return
     */
    public boolean isMessage() {
        return (getMsgType() & MsgType.MESSAGE) != 0;
    }

    /**
     * 需要立即下载的消息
     *
     * @return
     */
    public boolean isNeedDownload() {
        return (getMsgType() & MsgType.DOWNLOAD) != 0;
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

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getAnotherId(String userId) {
        if (getTargetId().equals(userId)) {
            return getSenderId();
        }
        return getTargetId();
    }

    public static ChatMessage create(Cursor cursor) {
        ChatMessage chatMessage = new ChatMessage();
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
        chatMessage.setUnread(cursor.getInt(cursor.getColumnIndex("unread")));
        chatMessage.setGroup(cursor.getInt(cursor.getColumnIndex("isGroup")) == 1);

        return chatMessage;
    }

    public static ChatListMessage createChatListMessage(ChatMessage message) {
        ChatListMessage chatListMessage = new ChatListMessage();
        chatListMessage.setTarget_id(message.getTargetId());
        chatListMessage.setSender_id(message.getSenderId());
        chatListMessage.setMsgType(message.getMsgType());
        chatListMessage.setSentTime(message.getSentTime());
        chatListMessage.setMsg(message.getMsg());
        chatListMessage.setExtra(message.getExtra());
        chatListMessage.setIs_group(message.isGroup() ? 1 : 0);
        chatListMessage.setUnread(message.getUnread());
        return chatListMessage;
    }

    public static ContentValues setContentValues(ContentValues contentValues, ChatMessage message) {
        contentValues.put("uuid", message.getUuid());
        contentValues.put("msgType", message.getMsgType());
        contentValues.put("sentStatus", message.getSentStatus());
        contentValues.put("senderId", message.getSenderId());
        contentValues.put("targetId", message.getTargetId());
        contentValues.put("sentTime", message.getSentTime());
        contentValues.put("duration", message.getDuration());
        contentValues.put("displayName", message.getDisplayName());
        contentValues.put("size", message.getSize());
        contentValues.put("localPath", message.getLocalPath());
        contentValues.put("remoteUrl", message.getRemoteUrl());
        contentValues.put("msg", message.getMsg());
        contentValues.put("unread", message.getUnread());
        contentValues.put("isGroup", message.isGroup() ? 1 : 0);
        return contentValues;
    }

    public static ChatMessage getBaseSendMessage(@MsgType int msgType, String senderId, String targetId, boolean isGroup) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUuid(UUID.randomUUID() + "");
        chatMessage.setSenderId(senderId);
        chatMessage.setTargetId(targetId);
        chatMessage.setSentTime(System.currentTimeMillis());
        chatMessage.setSentStatus(MsgSendStatus.SENDING);
        chatMessage.setMsgType(msgType);
        chatMessage.setGroup(isGroup);
        switch (msgType) {
            case MsgType.AUDIO:
                chatMessage.setMsg("[语音]");
                break;
            case MsgType.FILE:
                chatMessage.setMsg("[文件]");
                break;
            case MsgType.IMAGE:
                chatMessage.setMsg("[图片]");
                break;
            case MsgType.VIDEO:
                chatMessage.setMsg("[视频]");
                break;
        }
        return chatMessage;
    }
}
