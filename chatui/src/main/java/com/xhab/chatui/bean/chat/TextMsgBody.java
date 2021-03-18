package com.xhab.chatui.bean.chat;


public class TextMsgBody extends Message {
     private String msg;
     private String extra;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
    public void setMessage(Message message) {
        setUuid(message.getUuid());
        setSenderId(message.getSenderId());
        setTargetId(message.getTargetId());
        setSentTime(message.getSentTime());
        setSentStatus(message.getSentStatus());
        setMsgType(message.getMsgType());
    }
}
