package com.xhab.chatui.bean.chat;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by dab on 2021/3/19 18:01
 */

@IntDef({MsgSendStatus.DEFAULT, MsgSendStatus.SENDING, MsgSendStatus.FAILED, MsgSendStatus.SENT, MsgSendStatus.RECEIVE})
@Retention(RetentionPolicy.SOURCE)
public @interface MsgSendStatus {
    int DEFAULT = 0;//默认
    int SENDING = 1;//发送中
    int FAILED = 2;//发送失败
    int SENT = 3;//已发送
    int RECEIVE = 4;//已送达
}
