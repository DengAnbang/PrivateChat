package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.service.VoiceFloatingService;
import com.juphoon.cloud.JCCall;
import com.juphoon.cloud.JCCallItem;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.chatui.dbUtils.ChatDatabaseHelper;
import com.xhab.chatui.service.BaseVoiceFloatingService;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.chatui.voiceCalls.BaseVoiceActivity;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.RxUtils;
import com.xhab.utils.utils.TimeUtils;
import com.xhab.utils.utils.ToastUtil;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/27 14:30
 */
public class ChatVoiceActivity extends BaseVoiceActivity {
    private MediaPlayer mMediaPlayer;
    private Disposable mDisposable;
    private JCCallItem mJcCallItem;
    private long mCallTime;

    @Override
    public int getContentViewRes() {
        return com.xhab.chatui.R.layout.activity_voice;
    }

    @Override
    public void initView() {
        super.initView();
        mJcCallItem = JuphoonUtils.get().getJCCallItem();
        showUser(findViewById(com.xhab.chatui.R.id.tv_name), findViewById(com.xhab.chatui.R.id.iv_head_portrait));
    }

    @Override
    public void initEvent() {
        super.initEvent();

        addDisposable(RxBus.get().register("onCallItemUpdate", Integer.class).subscribe(integer -> {
            switch (mJcCallItem.getState()) {
                case JCCall.STATE_PENDING://响铃
                    LogUtils.e("initEvent*****:响铃");
                    ringBell();
                    break;
                case JCCall.STATE_CONNECTING://连接中
                    stopMediaPlayer();
                    if (mDisposable != null) {
                        mDisposable.dispose();
                    }
                    break;
                case JCCall.STATE_TALKING://通话中

                    break;
                case JCCall.STATE_CANCEL://未接通挂断

                    break;
                case JCCall.STATE_CANCELED://未接通被挂断

                    break;
                case JCCall.STATE_MISSED://未接

                    break;

            }

        }));
        addDisposable(RxUtils.interval(1, () -> {
            if (mJcCallItem.getState() == JCCall.STATE_TALKING) {
                //如果是通话中,显示通话时间
                mCallTime = (System.currentTimeMillis() / 1000) - mJcCallItem.getTalkingBeginTime();
                setTextViewString(R.id.tv_time, "通话时间:" + TimeUtils.getHMS(mCallTime));
            } else {
                LogUtils.e("initEvent*****: " + mJcCallItem.getState());
            }
        }));
        addDisposable(RxBus.get().register("onCallItemUpdate", Integer.class).subscribe(second -> {


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
            findViewById(R.id.iv_answer).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_answer).setOnClickListener(v -> {
                stopMediaPlayer();
                JuphoonUtils.get().answer(mJcCallItem);
                findViewById(R.id.iv_answer).setVisibility(View.GONE);
            });
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
            mDisposable = RxUtils.interval(15, () -> {
                //如果15s没有接听,则挂断!
                if (mJcCallItem.getState() == JCCall.STATE_PENDING) {
                    ToastUtil.showToast("对方未接听");
                    JuphoonUtils.get().hangup();
                    stopMediaPlayer();
                    mMediaPlayer = MediaPlayer.create(this, R.raw.end);
                    mMediaPlayer.start();
                    finish();
                }
            });
            addDisposable(mDisposable);
        }
    }


    private void showUser(TextView name, ImageView imageView) {
        UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(mJcCallItem.getUserId());
        name.setText(userMsgBeanById.getNickname());
        GlideUtils.loadHeadPortrait(userMsgBeanById.getHead_portrait(), imageView, userMsgBeanById.getPlaceholder());

    }

    private void stopMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(false);
            mMediaPlayer.stop();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.iv_close).setOnClickListener(v -> {
            quit();
        });
        JuphoonUtils.get().setCallBackRemove((item, reason, description) -> {
            stopMediaPlayer();
            dismissFloatingView();
            BaseVoiceFloatingService.StopSelf();
            if (mCallTime <= 0 || mCallTime > 10_0000_0000) {
                ToastUtil.showToast("通话结束" + description);
                if (item.getDirection()== JCCall.DIRECTION_OUT){
                    time(item.getUserId(), "通话结束");
                }
            } else {
                ToastUtil.showToast("通话结束,通话时长:" + TimeUtils.getHMS(mCallTime));
                if (item.getDirection()== JCCall.DIRECTION_OUT){
                    time(item.getUserId(), "通话时长:" + TimeUtils.getHMS(mCallTime));
                }
            }
            finish();
        });
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

    private void dispose() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }

    }

    public void quit() {
        dispose();
        stopMediaPlayer();
        dismissFloatingView();
        JuphoonUtils.get().hangup();
        finish();
    }

    @Override
    public void showFloatingView() {
        if (FunUtils.isServiceRunning(this, VoiceFloatingService.class.getName())) {
            //通知显示悬浮窗
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BaseVoiceFloatingService.ACTION_SHOW_FLOATING));
        } else {
            //启动悬浮窗管理服务
            Intent service = new Intent(this, VoiceFloatingService.class);
            startService(service);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    @Override
    public void dismissFloatingView() {
        if (FunUtils.isServiceRunning(this, VoiceFloatingService.class.getName())) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BaseVoiceFloatingService.ACTION_DISMISS_FLOATING));
        }
    }

}
