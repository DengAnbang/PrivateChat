package com.xhab.utils.net.socket;

import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxUtils;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by dab on 2021/4/7 14:01
 */
public class WebSocketIpm implements SocketAbstract {
    private String mWbSocketUrl;
    private WebSocket mWebSocket;
    public static long reConnectedTime = 4000;//重连间隔
    private boolean isConnect;//是否连接成功

    public WebSocketIpm(String wbSocketUrl) {
        mWbSocketUrl = wbSocketUrl;
    }

    private OnMessageCome mOnMessageCome;

    private WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            mWebSocket = webSocket;
            isConnect = response.code() == 101;
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            if (mOnMessageCome != null) {
                RxUtils.runOnUiThread(() -> {
                    mOnMessageCome.onMessageCome(text);
                });

            }
            if (!text.contains("PONG")) {
                LogUtils.e("WebSocketCall" + text);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            if (mOnMessageCome != null) {
                mOnMessageCome.onMessageCome(bytes.base64());
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            LogUtils.e("onClosed:");
            isConnect = false;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            isConnect = false;
            if (response != null) {
                LogUtils.e("WebSocket 连接失败：" + response.message());
            }
            LogUtils.e("WebSocket 连接失败异常原因：" + t.getMessage());
            isConnect = false;
            try {
                Thread.sleep(reConnectedTime);
                if (!isConnect) {
                    connect();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            t.printStackTrace();
        }
    };

    @Override
    public void setOnMessageChange(OnMessageCome onMessageChange) {
        mOnMessageCome = onMessageChange;
    }

    @Override
    public void send(@NonNull String sendMsg) {
        mWebSocket.send(sendMsg);
    }

    @Override
    public boolean isConnect() {
        return isConnect;
    }

    @Override
    public void connect() {
        OkHttpClient client = new OkHttpClient.Builder()
//                .pingInterval(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(mWbSocketUrl)
                .build();

        mWebSocket = client.newWebSocket(request, webSocketListener);
        mWebSocket.request();
    }


}