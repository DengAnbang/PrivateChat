package com.abxh.utils.net.socket;

import androidx.annotation.NonNull;

/**
 * Created by dab on 2021/4/7 13:52
 */
public interface SocketAbstract {


    /**
     * 收到消息的回调
     *
     * @param onMessageChange
     */
    public void setOnMessageChange(OnMessageCome onMessageChange);

    /**
     * 发送消息
     *
     * @param sendMsg
     */
    public boolean send(@NonNull String sendMsg);

    /**
     * 是否处于连接状态
     *
     * @return
     */
    public boolean isConnect();

    public void connect();


    public interface OnMessageCome {
        void onMessageCome(String msg);
    }
    public interface OnConnectionChange {
        void onChange(boolean connection);
    }

    public void setOnConnectionChange(OnConnectionChange onConnectionChange);
}
