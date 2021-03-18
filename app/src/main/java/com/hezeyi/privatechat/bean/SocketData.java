package com.hezeyi.privatechat.bean;

import com.google.gson.Gson;
import com.xhab.chatui.bean.chat.Message;

/**
 * Created by dab on 2018/1/4 0004 13:35
 */

public class SocketData {


    private String data;
    private String code;
    private String msg;
    private String type;
    private String debugMsg;
    private String targetId;
    private String senderId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDebugMsg() {
        return debugMsg;
    }

    public void setDebugMsg(String debugMsg) {
        this.debugMsg = debugMsg;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public static SocketData create(String code, String type, Message data) {
        SocketData socketData = new SocketData();
        socketData.setCode(code);
        socketData.setData(new Gson().toJson(data));
        socketData.setType(type);
        socketData.setSenderId(data.getSenderId());
        socketData.setTargetId(data.getTargetId());
        return socketData;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
