package com.xhab.chatui.bean.chat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * 消息类型
 */
@IntDef({MsgType.POINTLESS, MsgType.TEXT, MsgType.AUDIO, MsgType.VIDEO, MsgType.IMAGE, MsgType.FILE, MsgType.SYSTEM, MsgType.MESSAGE})
@Retention(RetentionPolicy.SOURCE)
public @interface MsgType {
    int POINTLESS = 0;//无意义的消息
    int TEXT = 1;//文本消息
    int AUDIO = 2;//语音消息
    int VIDEO = 4;//视频消息
    int IMAGE = 8;//图片消息
    int FILE = 16;//文件消息
    int SYSTEM = 32; //系统消息
    int MESSAGE = TEXT | AUDIO | VIDEO | IMAGE | FILE;//聊天的消息
}