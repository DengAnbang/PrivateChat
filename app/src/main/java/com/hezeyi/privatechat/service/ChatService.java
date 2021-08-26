package com.hezeyi.privatechat.service;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.FriendReplyActivity;
import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.hezeyi.privatechat.activity.chat.ChatActivity;
import com.hezeyi.privatechat.activity.chat.ChatVoiceActivity;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.SocketData;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.net.RetrofitFactory;
import com.hezeyi.privatechat.net.socket.SocketDispense;
import com.xdandroid.hellodaemon.AbsWorkService;
import com.abxh.chatui.bean.chat.ChatMessage;
import com.abxh.chatui.bean.chat.MsgSendStatus;
import com.abxh.chatui.bean.chat.MsgType;
import com.abxh.chatui.dbUtils.ChatDatabaseHelper;
import com.abxh.chatui.utils.NotificationManagerUtils;
import com.abxh.chatui.voiceCalls.JuphoonUtils;
import com.abxh.utils.StackManager;
import com.abxh.utils.net.RequestHelperAgency;
import com.abxh.utils.net.RequestHelperImp;
import com.abxh.utils.net.socket.SocketAbstract;
import com.abxh.utils.net.socket.WebSocketIpm;
import com.abxh.utils.utils.BitmapUtil;
import com.abxh.utils.utils.LogUtils;
import com.abxh.utils.utils.RxBus;
import com.abxh.utils.utils.RxUtils;
import com.abxh.utils.utils.SPUtils;
import com.abxh.utils.utils.ToastUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
            String userId = intent.getStringExtra("userId");
            if (!TextUtils.isEmpty(userId)) {
                mUserId = userId;
                SPUtils.save("user_id", mUserId);
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("stop"))) {
//                stopService()不是public
                //取消对任务的订阅
                stopWork(intent, flags, startId);
                //取消 Job / Alarm / Subscription
                cancelJobAlarmSub();
                return;
            }

        }
        if (!TextUtils.isEmpty(mUserId)) {
            LogUtils.e("startWork*****: loginSocket" + mUserId);
            loginSocket(mUserId);
        }
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        boolean stop = intent.getBooleanExtra("stop", false);
        LogUtils.e("stopWork*****: " + stop);
        loginOut();
        stopSelf();
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
//        boolean b = !TextUtils.isEmpty(mUserId);
//        LogUtils.e("isWorkRunning*****: " + b);
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
        loginOut();
        LogUtils.e("onDestroy*****: " + mUserId);
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        JuphoonUtils.get().hangup();
        LogUtils.e("onServiceKilled*****: " + mUserId);
    }


    private void init() {
        mSocketAbstract = new WebSocketIpm(Const.Api.SOCKET_SERVER, RetrofitFactory.getOkHttpClient());
        mSocketAbstract.setOnConnectionChange(connection -> {
            isConnection = connection;
        });
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
        if (!isConnection) return;//没有连接,就不登录
        if (!Objects.equals(Build.CPU_ABI, "x86")) {
            JuphoonUtils.get().login(userId, "123456");
        }
        ChatMessage data = ChatMessage.getBaseSendMessage(MsgType.POINTLESS, userId, "", false);
        data.setSenderId(userId);
//        String login_out = SocketData.create("0", Const.RxType.TYPE_LOGIN_OUT, data).toJson();
//        okioSocket.send(login_out);
        String login = SocketData.create("0", Const.RxType.TYPE_LOGIN, data).toJson();
        mSocketAbstract.send(login);
    }


    private void initSocket() {
        JuphoonUtils.get().setCallBackAdd(item -> {
//            if (!aBoolean1) {
//                name = getResources().getString(R.string.app_name);
//                msg = "收到一条新消息";
//                portrait = "";
//            }
//            Intent intent = ChatActivity.getStartChatActivity(this, targetId, isGroup);
//            NotificationManagerUtils.showNotification(this, intent,
//                    Const.Api.API_HOST + portrait, name, msg, message.getPlaceholder(), Const.Notification.CHANNEL_MSG_ID);

            Intent intent = new Intent(ChatService.this, VoiceService.class);
            startService(intent);
            if (MyApplication.getInstance().isForeground()) {
                Intent intent1 = new Intent(StackManager.currentActivity(), ChatVoiceActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }

        });
        mSocketAbstract.setOnMessageChange(SocketDispense::parseJson);
        addDisposable(Observable.interval(15 * 60, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            if (isConnection) {
                String s = SocketData.createHeartbeat().toJson();
                mSocketAbstract.send(s);
            }
        }));
        addDisposable(RxBus.get().register(Const.RxType.CONNECTION, Object.class).subscribe(s -> {
            LogUtils.e("CONNECTION*****: loginSocket");
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
            if (message.getMsgType() == MsgType.TEXT || message.getMsgType() == MsgType.SYSTEM || message.getMsgType() == MsgType.VOICE_CALLS) {
                boolean b = sendSendMsgBean(message);
                if (b) {
                    message.setSentStatus(MsgSendStatus.SENT);
                } else {
                    message.setSentStatus(MsgSendStatus.FAILED);
                }
                //消息发送出去了,对方还未收到
                RxBus.get().post(Const.RxType.TYPE_MSG_UPDATE, message);
            } else {
                String localPath = (message).getLocalPath();
                if (message.getMsgType() == MsgType.IMAGE) {
                    localPath = BitmapUtil.compressImage(localPath);
                }
                HttpManager.fileUpload(Const.FilePath.chatFileType, localPath, url -> {
                    message.setRemoteUrl(url);
                    boolean b = sendSendMsgBean(message);
                    if (b) {
                        message.setSentStatus(MsgSendStatus.SENT);
                    } else {
                        message.setSentStatus(MsgSendStatus.FAILED);
                    }
                    //消息发送出去了,对方还未收到
                    RxBus.get().post(Const.RxType.TYPE_MSG_UPDATE, message);
                });
            }
        }, Throwable::printStackTrace));
        addDisposable(RxBus.get().register(Const.RxType.TYPE_OTHER_LOGIN, Object.class).subscribe(o -> {
            ToastUtil.showToast("其他人登录了此账号,请重新登陆!");
            SPUtils.save(Const.Sp.password, "");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            StackManager.finishExcludeActivity(LoginActivity.class);
        }));
        //新好友添加
        addDisposable(RxBus.get().register(Const.RxType.TYPE_FRIEND_ADD, Object.class).subscribe(o -> {
            RxBus.get().post(Const.RxType.TYPE_SHOW_FRIEND_RED_PROMPT, 1);
            showNotification();
        }));
        //好友的状态发生变化
        addDisposable(RxBus.get().register(Const.RxType.TYPE_FRIEND_CHANGE, Object.class).subscribe(o -> {
            HttpManager.userSelectFriend(mUserId, "1", false, this, userMsgBeans -> {
                MyApplication.getInstance().setFriendUserMsgBeans(userMsgBeans);
                RxBus.get().post(Const.RxType.TYPE_FRIEND_CHANGE_SHOW, 1);
            });
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
        LogUtils.e("loginOut*****: ");
        ChatMessage data = ChatMessage.getBaseSendMessage(MsgType.POINTLESS, mUserId, "", false);
        data.setSenderId(mUserId);
        String login_out = SocketData.create("0", Const.RxType.TYPE_LOGIN_OUT, data).toJson();
        mSocketAbstract.send(login_out);
        mUserId = "";
        JuphoonUtils.get().logout();
    }


    public void msgReceive(ChatMessage data) {
        data.setSentStatus(MsgSendStatus.RECEIVE);
        String s = SocketData.create("0", Const.RxType.TYPE_MSG_UPDATE, data).toJson();
        mSocketAbstract.send(s);
    }

    public boolean sendSendMsgBean(ChatMessage sendMsg) {
        String type = Const.RxType.TYPE_MSG_SEND;
        if (sendMsg.isGroup()) {
            type = Const.RxType.TYPE_MSG_GROUP_SEND;
        }
        String s = SocketData.create("0", type, sendMsg).toJson();
        return mSocketAbstract.send(s);
    }


    private void downloadMsgFile(ChatMessage message) {
        showNotification(message);
        if (!message.isNeedDownload()) {
            RxBus.get().post(Const.RxType.TYPE_MSG_ADD, message);
            return;
        }
        String completePath = Const.FilePath.chatFileLocalPath + message.getRemoteUrl();
        addDisposable(HttpManager.downloadFile(Const.Api.API_HOST + message.getRemoteUrl(), completePath, aBoolean -> {
            message.setLocalPath(completePath);
            RxUtils.runOnUiThread(() -> RxBus.get().post(Const.RxType.TYPE_MSG_ADD, message));
        }));
    }


    private void login() {
        String account = SPUtils.getString(Const.Sp.account, "");
        String password = SPUtils.getString(Const.Sp.password, "");
        HttpManager.login(account, password, false, this, userMsgBean -> {
            MyApplication.getInstance().userLogin(userMsgBean, s -> {
                if (TextUtils.isEmpty(s)) {
                    MyApplication.getInstance().setUserMsgBean(userMsgBean.getData());
                    getUserList();
                } else {
                    loginOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    StackManager.finishExcludeActivity(LoginActivity.class);
                }
            });
        });
    }

    private void getUserList() {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, "1", this, userMsgBeans -> {
            MyApplication.getInstance().setFriendUserMsgBeans(userMsgBeans);
            HttpManager.groupSelectList(user_id, false, this, chatGroupBeans -> {
                MyApplication.getInstance().setChatGroupBeans(chatGroupBeans);
            });
        });
    }

    private void showNotification(ChatMessage message) {
        if (!message.isMessage()) return;
        if (message.getMsgType() == MsgType.VOICE_CALLS) {
            //语音通话消息也不推送
            return;
        }
        boolean aBoolean = SPUtils.getBoolean(Const.Sp.isNewMsgCode, true);
        if (!aBoolean) return;
        String anotherId = message.getAnotherId(MyApplication.getInstance().getUserMsgBean().getUser_id());
        if (Objects.equals(MyApplication.getInstance().getAnotherId(), anotherId)) return;
        RxUtils.runOnIoThread(() -> {
            String name;
            String portrait;
            String targetId;
            boolean isGroup = false;
            String msg = message.getMsg();
            if (message.isGroup()) {
                isGroup = true;

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
                    name = userMsgBeanById.getNickname();
                    portrait = userMsgBeanById.getHead_portrait();
                } else {
                    name = message.getSenderId();
                    portrait = "";
                }
                targetId = message.getSenderId();
            }
            boolean aBoolean1 = SPUtils.getBoolean(Const.Sp.isNewMsgDesCode, false);
            if (!aBoolean1) {
                name = getResources().getString(R.string.app_name);
                msg = "收到一条新消息";
                portrait = "";
            }
            Intent intent = ChatActivity.getStartChatActivity(this, targetId, isGroup);
            NotificationManagerUtils.showNotification(this, intent,
                    Const.Api.API_HOST + portrait, name, msg, message.getPlaceholder(), Const.Notification.CHANNEL_MSG_ID);

        });
    }

    private void showNotification() {
        RxUtils.runOnIoThread(() -> {
            Intent intent = new Intent(this, FriendReplyActivity.class);
            NotificationManagerUtils.showNotification(this, intent, "", getResources().getString(R.string.app_name), "有人请求添加你为好友", R.mipmap.logo, Const.Notification.CHANNEL_MSG_ID);
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