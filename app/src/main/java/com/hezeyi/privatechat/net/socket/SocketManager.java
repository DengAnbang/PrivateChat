package com.hezeyi.privatechat.net.socket;

import com.easysocket.EasySocket;
import com.easysocket.config.DefaultMessageProtocol;
import com.easysocket.config.EasySocketOptions;
import com.easysocket.entity.OriginReadData;
import com.easysocket.entity.SocketAddress;
import com.easysocket.interfaces.conn.ISocketActionListener;
import com.easysocket.interfaces.conn.SocketActionListener;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;


/**
 * Created by dab on 2021/3/23 20:13
 */
public class SocketManager {
    private boolean isConnected;

    /**
     * 初始化EasySocket
     */
    private void initEasySocket() {
        // socket配置
        EasySocketOptions options = new EasySocketOptions.Builder()
                // 主机地址，请填写自己的IP地址，以getString的方式是为了隐藏作者自己的IP地址
                .setSocketAddress(new SocketAddress(Const.Api.SOCKET_SERVER, Const.Api.SOCKET_PORT))
                // 定义消息协议，方便解决 socket黏包、分包的问题
                .setReaderProtocol(new DefaultMessageProtocol())
                .build();

        // 初始化
        EasySocket.getInstance().createConnection(options, MyApplication.getInstance());// 创建一个socket连接
        //监听socket相关行为
        EasySocket.getInstance().subscribeSocketAction(socketActionListener);
        //连接Socket
        EasySocket.getInstance().connect();
    }

    /**
     * 发送一个的消息，
     */
    private void sendTestMessage(byte[] message) {
        // 发送
        EasySocket.getInstance().upMessage(message);
    }

    /**
     * socket行为监听
     */
    private ISocketActionListener socketActionListener = new SocketActionListener() {
        /**
         * socket连接成功
         * @param socketAddress
         */
        @Override
        public void onSocketConnSuccess(SocketAddress socketAddress) {

            isConnected = true;
        }

        /**
         * socket连接失败
         * @param socketAddress
         * @param isNeedReconnect 是否需要重连
         */
        @Override
        public void onSocketConnFail(SocketAddress socketAddress, boolean isNeedReconnect) {
            isConnected = false;
        }

        /**
         * socket断开连接
         * @param socketAddress
         * @param isNeedReconnect 是否需要重连
         */
        @Override
        public void onSocketDisconnect(SocketAddress socketAddress, boolean isNeedReconnect) {
            isConnected = false;
        }

        /**
         * socket接收的数据
         * @param socketAddress
         * @param readData
         */
        @Override
        public void onSocketResponse(SocketAddress socketAddress, String readData) {

        }

        @Override
        public void onSocketResponse(SocketAddress socketAddress, OriginReadData originReadData) {
            super.onSocketResponse(socketAddress, originReadData);
//            LogUtil.d("SocketActionListener收到数据-->" + originReadData.getBodyString());
        }
    };

}