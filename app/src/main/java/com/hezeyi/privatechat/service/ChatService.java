package com.hezeyi.privatechat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.hezeyi.privatechat.activity.chat.ChatVoiceActivity;
import com.hezeyi.privatechat.bean.SocketData;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.net.socket.SocketDispense;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgSendStatus;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.chatui.dbUtils.ChatDatabaseHelper;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.StackManager;
import com.xhab.utils.net.socket.OkioSocket;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.RxUtils;
import com.xhab.utils.utils.ToastUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ChatService extends Service {

    private String mUserId;
    private boolean isConnection;

    public ChatService() {
        init();
        okioSocket = new OkioSocket();
        initSocket();
        connectSocket();
    }

    private void init() {

    }

    private OkioSocket okioSocket;


    private void initSocket() {
        JuphoonUtils.get().setCallBackAdd(item -> {
            MyApplication.getInstance().setJCCallItem(item);
            Intent intent = new Intent(ChatService.this, ChatVoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        Disposable subscribe4 = Observable.interval(20, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            if (isConnection) {
                String s = SocketData.createHeartbeat().toJson();
                okioSocket.send(s);
            }
        });

        Disposable subscribe5 = RxBus.get().register(Const.RxType.CONNECTION, Object.class).subscribe(s -> {
            isConnection = true;
            if (mUserId != null) {
                loginSocket(mUserId);
            }
        });

        okioSocket.setOnMessageChange(SocketDispense::parseJson);
        //接收消息
        Disposable subscribe = RxBus.get().register(Const.RxType.TYPE_MSG_RECEIVE, String.class).subscribe(s -> {
            ChatMessage message = new Gson().fromJson(s, ChatMessage.class);
            downloadMsgFile(message);
            if (!message.isGroup()) {
                msgReceive(message);
            }

        });
        //发送消息
        Disposable subscribe2 = RxBus.get().register(Const.RxType.TYPE_MSG_SEND, ChatMessage.class).subscribe(message -> {
            if (message.getMsgType() == MsgType.TEXT || message.getMsgType() == MsgType.SYSTEM) {
                sendSendMsgBean(message);
                message.setSentStatus(MsgSendStatus.SENT);
                //消息发送出去了,对方还未收到
                RxBus.get().post(Const.RxType.TYPE_MSG_UPDATE, message);
            } else {
                String localPath = (message).getLocalPath();
                HttpManager.fileUpload(Const.FilePath.chatFileType, localPath, url -> {
                    message.setRemoteUrl(url);
                    sendSendMsgBean(message);
                    //消息发送出去了,对方还未收到
                    message.setSentStatus(MsgSendStatus.SENT);
                    RxBus.get().post(Const.RxType.TYPE_MSG_UPDATE, message);
                });
            }
        });
        Disposable subscribe3 = RxBus.get().register(Const.RxType.TYPE_OTHER_LOGIN, Object.class).subscribe(o -> {
            ToastUtil.showToast("其他人登录了此账号,请重新登陆!");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            StackManager.finishExcludeActivity(LoginActivity.class);
        });
        //更新数据库的消息状态
        Disposable subscribe1 = RxBus.get().register(Const.RxType.TYPE_MSG_UPDATE, ChatMessage.class).subscribe(chatMessage -> {
            ChatDatabaseHelper.get(this, mUserId).updateMsg(chatMessage);
        });
        //添加到数据库的消息
        Disposable subscribe6 = RxBus.get().register(Const.RxType.TYPE_MSG_ADD, ChatMessage.class).subscribe(chatMessage -> {
            ChatDatabaseHelper.get(this, mUserId).chatDbInsert(chatMessage);
        });
    }

    public void connectSocket() {
        if (!okioSocket.isConnect()) {
            okioSocket.connect(Const.Api.SOCKET_SERVER, Const.Api.SOCKET_PORT);
        }
    }

    public void loginSocket(String userId) {
//        JuphoonUtils.get().login(userId, "123456");
        ChatMessage data = ChatMessage.getBaseSendMessage(MsgType.POINTLESS, userId, "", false);
        data.setSenderId(userId);
        String s = SocketData.create("0", Const.RxType.TYPE_LOGIN, data).toJson();
        okioSocket.send(s);
    }

    public void msgReceive(ChatMessage data) {
        data.setSentStatus(MsgSendStatus.RECEIVE);
        String s = SocketData.create("0", Const.RxType.TYPE_MSG_UPDATE, data).toJson();
        okioSocket.send(s);
    }

    public void sendSendMsgBean(ChatMessage sendMsg) {
        String type = Const.RxType.TYPE_MSG_SEND;
        if (sendMsg.isGroup()) {
            type = Const.RxType.TYPE_MSG_GROUP_SEND;
        }
        String s = SocketData.create("0", type, sendMsg).toJson();
        okioSocket.send(s);
    }

    @Override
    public IBinder onBind(Intent tent) {
        LogUtils.e("onBind*****: ");
        // TODO: Return the communication channel to the service.
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mUserId = intent.getStringExtra("userId");
        }

        if (mUserId != null) {
            loginSocket(mUserId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadMsgFile(ChatMessage message) {
        if (message.getMsgType() == MsgType.TEXT
                || message.getMsgType() == MsgType.SYSTEM
                || message.getMsgType() == MsgType.FILE
        ) {
            RxBus.get().post(Const.RxType.TYPE_MSG_ADD, message);
            return;
        }
        String completePath = Const.FilePath.chatFileLocalPath + message.getRemoteUrl();
        Disposable disposable = HttpManager.downloadFileNew(Const.Api.API_HOST + message.getRemoteUrl(), completePath, aBoolean -> {
            message.setLocalPath(completePath);
            RxUtils.runOnUiThread(() -> RxBus.get().post(Const.RxType.TYPE_MSG_ADD, message));
        });

    }
}
