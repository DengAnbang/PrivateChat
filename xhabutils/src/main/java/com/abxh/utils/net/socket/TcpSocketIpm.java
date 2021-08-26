package com.abxh.utils.net.socket;

import androidx.annotation.NonNull;

import com.abxh.utils.utils.LogUtils;

/**
 * Created by dab on 2021/4/7 15:05
 */
public class TcpSocketIpm implements SocketAbstract {
    private OkioSocket okioSocket;

    public TcpSocketIpm(@NonNull String host, int port) {
        okioSocket = new OkioSocket();
        okioSocket.init(host, port);
//        okioSocket.init(Const.Api.SOCKET_SERVER, Const.Api.SOCKET_PORT);

    }


    @Override
    public void setOnMessageChange(OnMessageCome onMessageChange) {
        okioSocket.setOnMessageChange(onMessageChange);
    }

    @Override
    public boolean send(@NonNull String sendMsg) {
        okioSocket.send(sendMsg);
        // TODO: 2021/5/7 未实现,暂时统一返回成功
        return true;
    }

    @Override
    public boolean isConnect() {
        return okioSocket.isConnect();
    }

    @Override
    public void connect() {
        okioSocket.connect();
    }

    @Override
    public void setOnConnectionChange(OnConnectionChange onConnectionChange) {
        LogUtils.e("setOnConnectionChange*****: 没有实现**********************");
    }

}
