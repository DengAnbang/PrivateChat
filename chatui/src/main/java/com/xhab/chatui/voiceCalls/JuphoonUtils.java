package com.xhab.chatui.voiceCalls;

import android.content.Context;

import com.juphoon.cloud.JCCall;
import com.juphoon.cloud.JCCallCallback;
import com.juphoon.cloud.JCCallItem;
import com.juphoon.cloud.JCClient;
import com.juphoon.cloud.JCClientCallback;
import com.juphoon.cloud.JCMediaDevice;
import com.juphoon.cloud.JCMediaDeviceCallback;
import com.juphoon.cloud.JCMediaDeviceVideoCanvas;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.TimeUtils;

import androidx.annotation.NonNull;

import static com.juphoon.cloud.JCCall.STATE_OK;

/**
 * Created by dab on 2021/3/26 17:27
 */
public class JuphoonUtils {

    private boolean mInit;
    private JCClient mClient;
    private JCCall mCall;
    private static JuphoonUtils instance;
    private JCMediaDevice mMediaDevice;

    public JCCall getCall() {
        return mCall;
    }

    private JCCallItem mJCCallItem;

    public JCCallItem getJCCallItem() {
        return mJCCallItem;
    }


    public static JuphoonUtils get() {
        if (instance == null) {
            synchronized (JuphoonUtils.class) {
                if (instance == null) {
                    instance = new JuphoonUtils();
                }
            }
        }
        return instance;
    }

    private JuphoonUtils() {

    }


    // 初始化函数
    public boolean initialize(Context context, String appKey) {
        // 登录类
        mClient = JCClient.create(context, appKey, new JCClientCallback() {
            @Override
            public void onLogin(boolean result, int reason) {
                if (result) {// 登录成功
//                    LogUtils.e("onLogin*****: 账号登录成功");

                }
                if (reason == com.juphoon.cloud.JCClient.REASON_AUTH) {// 账号密码错误
                    LogUtils.e("onLogin*****: 账号密码错误");
                }
            }

            @Override
            public void onLogout(int reason) {
                if (reason == com.juphoon.cloud.JCClient.REASON_SERVER_LOGOUT) {// 强制登出
                    LogUtils.e("onLogout*****: 强制登出");
                }
            }

            @Override
            public void onClientStateChange(int state, int oldState) {
                if (state == JCClient.STATE_IDLE) { // 未登录
                    LogUtils.e("onClientStateChange*****: 语音状态:未登录");
                } else if (state == JCClient.STATE_LOGINING) { // 正在登录
                    LogUtils.e("onClientStateChange*****: 正在登录");
                } else if (state == JCClient.STATE_LOGINED) { // 登录成功
                    LogUtils.e("onClientStateChange*****: 语音状态:登录成功");
                } else if (state == JCClient.STATE_LOGOUTING) { // 登出中
                    LogUtils.e("onClientStateChange*****: 语音状态:登出中");
                }
            }
        }, null);
        // 获取初始化状态（用来判断初始化状态）
        mInit = mClient.getState() == JCClient.STATE_IDLE;
        initializeCall();
        return mInit;
    }


    // 初始化函数
    private void initializeCall() {
        //1. 媒体类
        mMediaDevice = JCMediaDevice.create(mClient, new JCMediaDeviceCallback() {
            @Override
            public void onCameraUpdate() {

            }

            @Override
            public void onAudioOutputTypeChange(int audioRouteType) {

            }

            @Override
            public void onRenderReceived(JCMediaDeviceVideoCanvas canvas) {

            }

            @Override
            public void onRenderStart(JCMediaDeviceVideoCanvas canvas) {

            }

            @Override
            public void onVideoError(JCMediaDeviceVideoCanvas canvas) {

            }
        });


        //2. 通话类
        mCall = JCCall.create(mClient, mMediaDevice, new JCCallCallback() {
            @Override
            public void onCallItemAdd(JCCallItem jcCallItem) {
                mJCCallItem = jcCallItem;
                // 业务逻辑
                if (jcCallItem.getDirection() == JCCall.DIRECTION_IN) {

                    // 如果是被叫
                    LogUtils.e("被叫" + jcCallItem.getUserId());
                } else {
                    // 如果是主叫
                    LogUtils.e("主叫" + jcCallItem.getUserId());
                }
                if (jcCallItem.getDirection() == JCCall.DIRECTION_IN && !jcCallItem.getVideo()) {
                    if (mCallBackAdd != null) {
                        mCallBackAdd.onCallItemAdd(jcCallItem);
                    }

                }

            }

            @Override
            public void onCallItemRemove(JCCallItem jcCallItem, int i, String s) {
                if (mCallBackRemove != null) {
                    mCallBackRemove.onCallItemRemove(jcCallItem, i, s);
                }

                LogUtils.e("onCallItemRemove*****: " + jcCallItem.getState());
                LogUtils.e("通话结束" + TimeUtils.toTimeByString(jcCallItem.getTalkingBeginTime()));
            }

            @Override
            public void onCallItemUpdate(JCCallItem jcCallItem, JCCallItem.ChangeParam changeParam) {
                LogUtils.e("onCallItemUpdate*****: 通话状态" + jcCallItem.getState());
                RxBus.get().post("onCallItemUpdate", jcCallItem.getState());

            }

            @Override
            public void onMessageReceive(String s, String s1, JCCallItem jcCallItem) {

                LogUtils.e("onMessageReceive*****: " + s + "*********" + s1);
            }

            @Override
            public void onMissedCallItem(JCCallItem jcCallItem) {
                if (jcCallItem.getState() == STATE_OK) {
                    LogUtils.e("onMissedCallItem");
                }


            }

            @Override
            public void onDtmfReceived(JCCallItem item, int value) {
                LogUtils.e("onDtmfReceived");
            }
        });

    }


    public void login(@NonNull String userId, @NonNull String password) {
        JCClient.LoginParam loginParam = new JCClient.LoginParam();
        //发起登录
        mClient.login(userId, password, null, loginParam);
    }

    public void logout() {
        mClient.logout();
    }

    public void call(@NonNull String userId, String extraParam) {
        mCall.call(userId, false, new JCCall.CallParam(extraParam, "ticket"));
    }

    public void answer(@NonNull JCCallItem item) {
        mCall.answer(item, false);
    }

    public void hangup() {
        // 1. 获取当前活跃通话
        JCCallItem item = mCall.getActiveCallItem();
        // 2. 挂断当前活跃通话
        mCall.term(item, JCCall.REASON_NONE, null);
    }

    private CallBackAdd mCallBackAdd;

    public void setCallBackAdd(CallBackAdd callBackAdd) {
        mCallBackAdd = callBackAdd;
    }

    private CallBackRemove mCallBackRemove;

    public void setCallBackRemove(CallBackRemove callBackRemove) {
        mCallBackRemove = callBackRemove;
    }


    public interface CallBackAdd {
        void onCallItemAdd(JCCallItem item);
    }

    public interface CallBackRemove {
        void onCallItemRemove(JCCallItem item, @JCCall.CallReason int reason, String description);
    }

}
