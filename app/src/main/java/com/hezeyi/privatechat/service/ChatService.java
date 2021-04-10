package com.hezeyi.privatechat.service;

import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

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
import com.xdandroid.hellodaemon.AbsWorkService;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgSendStatus;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.chatui.dbUtils.ChatDatabaseHelper;
import com.xhab.chatui.utils.NotificationManagerUtils;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.StackManager;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;
import com.xhab.utils.net.socket.SocketAbstract;
import com.xhab.utils.net.socket.WebSocketIpm;
import com.xhab.utils.utils.BitmapUtil;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.RxUtils;
import com.xhab.utils.utils.SPUtils;
import com.xhab.utils.utils.ToastUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 监听系统通知，需要用户手动开启权限，那么该进程可以不死
 */


public class ChatService extends AbsWorkService implements RequestHelperImp {

    private String mUserId;
    private boolean isConnection;
    private SocketAbstract mSocketAbstract;

    public ChatService() {

    }


    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return false;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        if (intent != null) {
            mUserId = intent.getStringExtra("userId");
            if (!TextUtils.isEmpty(mUserId)) {
                SPUtils.save("user_id", mUserId);
            }

        }
        if (mUserId != null) {
            loginSocket(mUserId);
        }
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        loginOut();
        mRequestHelperAgency.destroy();
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        LogUtils.e("isWorkRunning*****: ");
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy*****: " + mUserId);
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {

        LogUtils.e("onServiceKilled*****: " + mUserId);
    }


    private void init() {
        mSocketAbstract = new WebSocketIpm("ws://" + Const.Api.SOCKET_SERVER + ":9090/" + "websocket");
//        mSocketAbstract = new TcpSocketIpm(Const.Api.SOCKET_SERVER, Const.Api.SOCKET_PORT);
        initSocket();
        connectSocket();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void loginSocket(String userId) {
        JuphoonUtils.get().login(userId, "123456");
        ChatMessage data = ChatMessage.getBaseSendMessage(MsgType.POINTLESS, userId, "", false);
        data.setSenderId(userId);
//        String login_out = SocketData.create("0", Const.RxType.TYPE_LOGIN_OUT, data).toJson();
//        okioSocket.send(login_out);
        String login = SocketData.create("0", Const.RxType.TYPE_LOGIN, data).toJson();
        mSocketAbstract.send(login);
    }


    private void initSocket() {
        JuphoonUtils.get().setCallBackAdd(item -> {
            MyApplication.getInstance().setJCCallItem(item);
            Intent intent = new Intent(ChatService.this, ChatVoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        mSocketAbstract.setOnMessageChange(SocketDispense::parseJson);
        addDisposable(Observable.interval(40, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            if (isConnection) {
                String s = SocketData.createHeartbeat().toJson();
                mSocketAbstract.send(s);
            }
        }));
        addDisposable(RxBus.get().register(Const.RxType.CONNECTION, Object.class).subscribe(s -> {
            mUserId = SPUtils.getString("user_id", "");
            if (!TextUtils.isEmpty(mUserId)) {
                login();
            }
            isConnection = true;
            if (mUserId != null) {
                loginSocket(mUserId);
            }
        }));

        //接收消息
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_RECEIVE, String.class).subscribe(s -> {
            ChatMessage message = new Gson().fromJson(s, ChatMessage.class);
            if (message.isMessage()) {
                message.setUnread(1);
            }
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
                if (message.getMsgType() == MsgType.IMAGE) {
                    localPath = BitmapUtil.compressImage(localPath);
                }
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
            RxBus.get().post(Const.RxType.TYPE_SHOW_LIST, "");
        }));
        //退出登录
        addDisposable(RxBus.get().register(Const.RxType.TYPE_LOGIN_OUT, Object.class).subscribe(chatMessage -> {
            loginOut();
        }));
    }

    public void connectSocket() {
        if (!mSocketAbstract.isConnect()) {
            mSocketAbstract.connect();
        }
    }


    private void loginOut() {
        ChatMessage data = ChatMessage.getBaseSendMessage(MsgType.POINTLESS, mUserId, "", false);
        data.setSenderId(mUserId);
        String login_out = SocketData.create("0", Const.RxType.TYPE_LOGIN_OUT, data).toJson();
        mSocketAbstract.send(login_out);
    }


    public void msgReceive(ChatMessage data) {
        data.setSentStatus(MsgSendStatus.RECEIVE);
        String s = SocketData.create("0", Const.RxType.TYPE_MSG_UPDATE, data).toJson();
        mSocketAbstract.send(s);
    }

    public void sendSendMsgBean(ChatMessage sendMsg) {
        String type = Const.RxType.TYPE_MSG_SEND;
        if (sendMsg.isGroup()) {
            type = Const.RxType.TYPE_MSG_GROUP_SEND;
        }
        String s = SocketData.create("0", type, sendMsg).toJson();
        mSocketAbstract.send(s);
    }


    private void downloadMsgFile(ChatMessage message) {
        showNotification(message);
        if (!message.isNeedDownload()) {
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
        if (!message.isMessage()) return;
        boolean aBoolean = SPUtils.getBoolean(Const.Sp.isNewMsgCode, true);
        if (!aBoolean) return;
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
            boolean aBoolean1 = SPUtils.getBoolean(Const.Sp.isNewMsgDesCode, false);
            if (!aBoolean1) {
                name = "S.O.M";
                msg = "收到一条新消息";
                portrait = "";
            }
            intent.putExtra("targetId", targetId);
            NotificationManagerUtils.showNotification(this, intent,
                    true,
                    Const.Api.API_HOST + portrait, name, msg, message.getPlaceholder());

        });
    }

    private void login() {
        String account = SPUtils.getString(Const.Sp.account, "");
        String password = SPUtils.getString(Const.Sp.password, "");
        HttpManager.login(account, password, this, userMsgBean -> {
            MyApplication.getInstance().setUserMsgBean(userMsgBean);
            getUserList();
        });
    }

    private void getUserList() {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, "1", this, userMsgBeans -> {
            MyApplication.getInstance().setUserMsgBeans(userMsgBeans);
            HttpManager.groupSelectList(user_id, this, chatGroupBeans -> {
                MyApplication.getInstance().setChatGroupBeans(chatGroupBeans);
                MyApplication.getInstance().setLock(true);
            });
        });
    }

    private RequestHelperAgency mRequestHelperAgency;

    @Override
    public boolean onUnbind(Intent intent) {
        loginOut();
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
