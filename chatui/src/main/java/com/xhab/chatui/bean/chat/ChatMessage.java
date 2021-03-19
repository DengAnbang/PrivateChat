package com.xhab.chatui.bean.chat;

/**
 * Created by dab on 2021/3/19 17:50
 */
public class ChatMessage {
    private String uuid;
    private String msgId;
    @MsgType
    private int msgType;
    @MsgSendStatus
    private int sentStatus;
    private String senderId;
    private String targetId;
    private long sentTime;
    ////////////////////////////////语音
    //语音消息长度,单位：秒。
    private long duration;
    /////////////////////////////////////////文件
    //文件后缀名
    private String ext;
    //文件名称,可以和文件名不同，仅用于界面展示
    private String displayName;
    //文件长度(字节)
    private long size;
    //本地文件保存路径
    private String localPath;
    //文件下载地址
    private String remoteUrl;
    //文件内容的MD5
    private String md5;
    //扩展信息，可以放置任意的数据内容，也可以去掉此属性
    private String extra;
    ///////////////////////////////////////////文件
    private String msg;
    //////////////////////////////////视频
    //高度
    private int height;
    //宽度
    private int width;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @MsgType
    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(@MsgType int msgType) {
        this.msgType = msgType;
    }

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

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
