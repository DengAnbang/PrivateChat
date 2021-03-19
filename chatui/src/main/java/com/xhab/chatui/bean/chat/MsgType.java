package com.xhab.chatui.bean.chat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * 消息类型
 */
@IntDef({MsgType.TEXT, MsgType.AUDIO, MsgType.VIDEO, MsgType.IMAGE, MsgType.FILE, MsgType.SYSTEM})
@Retention(RetentionPolicy.SOURCE)
public @interface MsgType {
    int TEXT = 0;//文本消息
    int AUDIO = 1;//语音消息
    int VIDEO = 2;//视频消息
    int IMAGE = 3;//图片消息
    int FILE = 4;//文件消息
    int SYSTEM = 5; //系统消息

}