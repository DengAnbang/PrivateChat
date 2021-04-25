package com.hezeyi.privatechat.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.chat.ChatVoiceActivity;
import com.juphoon.cloud.JCCall;
import com.juphoon.cloud.JCCallItem;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.chatui.dbUtils.ChatDatabaseHelper;
import com.xhab.chatui.service.BaseVoiceFloatingService;
import com.xhab.chatui.utils.NotificationManagerUtils;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.RxUtils;
import com.xhab.utils.utils.TimeUtils;
import com.xhab.utils.utils.ToastUtil;

import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/4/22 15:46
 */
public class VoiceService extends Service implements RequestHelperImp {

    private JCCallItem mJcCallItem;
    private MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("onStartCommand*****: ");
        stopForeground(true);
        Intent intent1 = new Intent(this, ChatVoiceActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 获取服务通知
        Notification notification = NotificationManagerUtils.getNotification(this, intent1, false, "语音通话", Const.Notification.CHANNEL_MSG_ID);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,notification);
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(1, notification);

        init();
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {

        mJcCallItem = JuphoonUtils.get().getJCCallItem();
        if (mJcCallItem == null) {
            stopSelf();
            return;
        }
        //呼入的时候,不会回调onCallItemUpdate 这个方法,所有单独触发一次
        addDisposable(RxBus.get().register("onCallItemUpdate", Integer.class).subscribe(integer -> {
            switch (mJcCallItem.getState()) {
                case JCCall.STATE_PENDING://响铃
                    LogUtils.e("initEvent*****:响铃");
                    ringBell();
                    break;
                case JCCall.STATE_CONNECTING://连接中
                    stopMediaPlayer();
                    break;
                case JCCall.STATE_TALKING://通话中
                    stopMediaPlayer();
                    break;
                case JCCall.STATE_CANCEL://未接通挂断

                    break;
                case JCCall.STATE_CANCELED://未接通被挂断

                    break;
                case JCCall.STATE_MISSED://未接

                    break;

            }
        }));
        addDisposable(RxUtils.interval(5, () -> {
            if (mJcCallItem != null) {
                LogUtils.e("55555555*****: mJcCallItem:" + mJcCallItem.getState());
                if (mJcCallItem.getState() == JCCall.STATE_TALKING) {
                    if (JuphoonUtils.get().getPing() >= 3) {//表示三次没有收到ping了,对方可能已经掉线了
                        if (mJcCallItem.getDirection() == JCCall.DIRECTION_IN) {//如果是来电,则发送一次通话异常的消息
                            long mCallTime = (System.currentTimeMillis() / 1000) - mJcCallItem.getTalkingBeginTime();
                            time(mJcCallItem.getUserId(), "通话异常结束:" + TimeUtils.getHMS(mCallTime));
                            JuphoonUtils.get().setPing(0);
                        }
                        JuphoonUtils.get().hangup();
                        stopSelf();
                        return;
                    }
                    JuphoonUtils.get().getCall().sendMessage(mJcCallItem, "1", "Ping");
                    JuphoonUtils.get().setPing(JuphoonUtils.get().getPing() + 1);

                } else {
                    LogUtils.e("initEvent*****: " + mJcCallItem.getState());
                }

            } else {
                LogUtils.e("55555555*****: mJcCallItem==null");

            }


        }));
        addDisposable(RxBus.get().register("onCallItemRemove", JCCallItem.class).subscribe(jcCallItem -> {
            stopMediaPlayer();
            BaseVoiceFloatingService.StopSelf();
            long mCallTime = (System.currentTimeMillis() / 1000) - mJcCallItem.getTalkingBeginTime();
            if (mCallTime <= 0 || mCallTime > 10_0000_0000) {
                ToastUtil.showToast("通话结束");
                if (jcCallItem.getDirection() == JCCall.DIRECTION_OUT) {
                    time(jcCallItem.getUserId(), "通话结束");
                }
            } else {
                ToastUtil.showToast("通话结束,通话时长:" + TimeUtils.getHMS(mCallTime));
                if (jcCallItem.getDirection() == JCCall.DIRECTION_OUT) {
                    time(jcCallItem.getUserId(), "通话时长:" + TimeUtils.getHMS(mCallTime));
                }
            }
            stopSelf();
        }));


        //如果是来电,则获取一次通话状态
        if (mJcCallItem.getDirection() == JCCall.DIRECTION_IN) {
            RxBus.get().post("onCallItemUpdate", mJcCallItem.getState());
        }
    }

    private void ringBell() {
        //来电
        if (mJcCallItem.getDirection() == JCCall.DIRECTION_IN) {
            boolean b = mMediaPlayer != null && mMediaPlayer.isPlaying();
            LogUtils.e("ringBell*****: " + b);
            if (b) {
                return;
            }
            mMediaPlayer = MediaPlayer.create(this, R.raw.laidian);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        } else {
            boolean b = mMediaPlayer != null && mMediaPlayer.isPlaying();
            LogUtils.e("ringBell*****: " + b);
            if (b) {
                return;
            }
            //拨打出去
            mMediaPlayer = MediaPlayer.create(this, R.raw.start);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            Disposable mDisposable = RxUtils.interval(15, () -> {
                //如果15s没有接听,则挂断!
                if (mJcCallItem.getState() == JCCall.STATE_PENDING) {
                    ToastUtil.showToast("对方未接听");
                    JuphoonUtils.get().hangup();
                    stopMediaPlayer();
                    mMediaPlayer = MediaPlayer.create(this, R.raw.end);
                    mMediaPlayer.start();

                }
            });
            addDisposable(mDisposable);
        }
    }

    private void stopMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(false);
            mMediaPlayer.stop();
        }
    }

    public void time(String targetId, String msg) {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        ChatMessage chatMessage = ChatMessage.getBaseSendMessage(MsgType.VOICE_CALLS,
                user_id, targetId, false);
        chatMessage.setMsg("[语音消息]");
        chatMessage.setExtra(msg);
        RxBus.get().post(Const.RxType.TYPE_MSG_SEND, chatMessage);
        RxBus.get().post(Const.RxType.TYPE_MSG_ADD, chatMessage);
        ChatDatabaseHelper.get(this, user_id).chatDbInsert(chatMessage);
    }

    @Override
    public void onDestroy() {

        // 移除通知
        stopForeground(true);
        if (mRequestHelperAgency != null) {
            mRequestHelperAgency.destroy();
        }
        JuphoonUtils.get().hangup();
        super.onDestroy();
    }

    private RequestHelperAgency mRequestHelperAgency;


    @Override
    public RequestHelperAgency initRequestHelper() {
        if (mRequestHelperAgency == null) {
            mRequestHelperAgency = new RequestHelperAgency(this);
        }
        return mRequestHelperAgency;
    }

}
