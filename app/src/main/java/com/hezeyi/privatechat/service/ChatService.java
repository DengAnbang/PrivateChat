package com.hezeyi.privatechat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.hezeyi.privatechat.activity.chat.ChatActivity;
import com.hezeyi.privatechat.activity.chat.ChatVoiceActivity;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.SocketData;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.net.socket.SocketDispense;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgSendStatus;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.chatui.dbUtils.ChatDatabaseHelper;
import com.xhab.chatui.utils.NotificationManagerUtils;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.StackManager;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;
import com.xhab.utils.net.socket.OkioSocket;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.RxUtils;
import com.xhab.utils.utils.SPUtils;
import com.xhab.utils.utils.ToastUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ChatService extends Service implements RequestHelperImp {

    private String mUserId;
    private boolean isConnection;

    public ChatService() {

    }

    private void init() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        okioSocket = new OkioSocket();
        initSocket();
        connectSocket();
    }

    private OkioSocket okioSocket;


    private void initSocket() {
        JuphoonUtils.get().setCallBackAdd(item -> {
            MyApplication.getInstance().setJCCallItem(item);
            Intent intent = new Intent(ChatService.this, ChatVoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        okioSocket.setOnMessageChange(SocketDispense::parseJson);

        addDisposable(Observable.interval(20, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            if (isConnection) {
                String s = SocketData.createHeartbeat().toJson();
                okioSocket.send(s);
            }
        }));
        addDisposable(RxBus.get().register(Const.RxType.CONNECTION, Object.class).subscribe(s -> {
            isConnection = true;
            if (mUserId != null) {
                loginSocket(mUserId);
            }
        }));

        //接收消息
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_RECEIVE, String.class).subscribe(s -> {
            ChatMessage message = new Gson().fromJson(s, ChatMessage.class);
            downloadMsgFile(message);
            if (!message.isGroup()) {
                msgReceive(message);
            }

        }));
        //发送消息
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_SEND, ChatMessage.class).subscribe(message -> {
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
        }));
        addDisposable(RxBus.get().register(Const.RxType.TYPE_OTHER_LOGIN, Object.class).subscribe(o -> {
            ToastUtil.showToast("其他人登录了此账号,请重新登陆!");
            SPUtils.save(Const.Sp.password, "");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            StackManager.finishExcludeActivity(LoginActivity.class);
        }));
        //更新数据库的消息状态
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_UPDATE, ChatMessage.class).subscribe(chatMessage -> {
            ChatDatabaseHelper.get(this, mUserId).updateMsg(chatMessage);
        }));
        //添加到数据库的消息
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_ADD, ChatMessage.class).subscribe(chatMessage -> {
            ChatDatabaseHelper.get(this, mUserId).chatDbInsert(chatMessage);
        }));
        //退出登录
        addDisposable(RxBus.get().register(Const.RxType.TYPE_LOGIN_OUT, Object.class).subscribe(chatMessage -> {
            loginOut();
        }));
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
//        String login_out = SocketData.create("0", Const.RxType.TYPE_LOGIN_OUT, data).toJson();
//        okioSocket.send(login_out);
        String login = SocketData.create("0", Const.RxType.TYPE_LOGIN, data).toJson();
        okioSocket.send(login);
    }

    private void loginOut() {
        ChatMessage data = ChatMessage.getBaseSendMessage(MsgType.POINTLESS, mUserId, "", false);
        data.setSenderId(mUserId);
        String login_out = SocketData.create("0", Const.RxType.TYPE_LOGIN_OUT, data).toJson();
        okioSocket.send(login_out);
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
        showNotification(message);
        if (message.getMsgType() == MsgType.TEXT
                || message.getMsgType() == MsgType.SYSTEM
                || message.getMsgType() == MsgType.FILE
        ) {
            RxBus.get().post(Const.RxType.TYPE_MSG_ADD, message);
            return;
        }
        String completePath = Const.FilePath.chatFileLocalPath + message.getRemoteUrl();
        addDisposable(HttpManager.downloadFileNew(Const.Api.API_HOST + message.getRemoteUrl(), completePath, aBoolean -> {
            message.setLocalPath(completePath);
            RxUtils.runOnUiThread(() -> RxBus.get().post(Const.RxType.TYPE_MSG_ADD, message));
        }));
    }

    private void showNotification(ChatMessage message) {
        if (message.getMsgType() == MsgType.SYSTEM) return;
        String anotherId = message.getAnotherId(MyApplication.getInstance().getUserMsgBean().getUser_id());
        if (Objects.equals(MyApplication.getInstance().getAnotherId(), anotherId)) return;
        RxUtils.runOnIoThread(() -> {
            Intent intent = new Intent(this, ChatActivity.class);
            String name;
            String portrait;
            String targetId;
            String msg = message.getMsg();
            if (message.isGroup()) {
                intent.putExtra("isGroup", true);
                ChatGroupBean chatGroupBean = MyApplication.getInstance().getChatGroupBeanById(message.getTargetId());
                if (chatGroupBean != null) {
                    name = chatGroupBean.getGroup_name();
                    portrait = chatGroupBean.getGroup_portrait();
                } else {
                    name = message.getTargetId();
                    portrait = "";
                }
                targetId = message.getTargetId();
            } else {
                UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(message.getSenderId());
                if (userMsgBeanById != null) {
                    name = userMsgBeanById.getUser_name();
                    portrait = userMsgBeanById.getHead_portrait();
                } else {
                    name = message.getSenderId();
                    portrait = "";
                }
                targetId = message.getSenderId();
            }
            intent.putExtra("targetId", targetId);
            NotificationManagerUtils.showNotification(this, intent,
                    true, 1,
                    Const.Api.API_HOST + portrait, name, msg);

        });
    }


    private RequestHelperAgency mRequestHelperAgency;

    @Override
    public boolean onUnbind(Intent intent) {
        mRequestHelperAgency.destroy();
        return super.onUnbind(intent);
    }

    @Override
    public RequestHelperAgency initRequestHelper() {
        if (mRequestHelperAgency == null) {
            mRequestHelperAgency = new RequestHelperAgency(this);
        }
        return mRequestHelperAgency;
    }
}
