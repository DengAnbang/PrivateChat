package com.xhab.utils.net.socket;

import androidx.annotation.NonNull;

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
    public void send(@NonNull String sendMsg) {
        okioSocket.send(sendMsg);
    }

    @Override
    public boolean isConnect() {
        return okioSocket.isConnect();
    }

    @Override
    public void connect() {
        okioSocket.connect();
    }

}
