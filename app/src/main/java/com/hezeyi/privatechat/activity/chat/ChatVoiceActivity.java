package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.MainActivity;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.service.VoiceFloatingService;
import com.hezeyi.privatechat.service.VoiceService;
import com.juphoon.cloud.JCCall;
import com.juphoon.cloud.JCCallItem;
import com.juphoon.cloud.JCMediaDevice;
import com.xhab.chatui.service.BaseVoiceFloatingService;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.chatui.voiceCalls.BaseVoiceActivity;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.RxUtils;
import com.xhab.utils.utils.TimeUtils;

import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/3/27 14:30
 */
public class ChatVoiceActivity extends BaseVoiceActivity {
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
        if (checkJcCallItem()) {
            showUser(findViewById(com.xhab.chatui.R.id.tv_name), findViewById(com.xhab.chatui.R.id.iv_head_portrait));
        }
    }

    public boolean checkJcCallItem() {
        if (mJcCallItem == null) {
            stopService(new Intent(this, VoiceService.class));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();

        if (checkJcCallItem()) {
            if (mJcCallItem.getDirection() == JCCall.DIRECTION_IN && mJcCallItem.getState() == JCCall.STATE_PENDING) {
                findViewById(R.id.iv_answer).setVisibility(View.VISIBLE);
                findViewById(R.id.iv_answer).setOnClickListener(v -> {
                    JuphoonUtils.get().answer(mJcCallItem);
                    findViewById(R.id.iv_answer).setVisibility(View.GONE);
                });
            }
        }
        addDisposable(RxBus.get().register("onCallItemUpdate", Integer.class).subscribe(integer -> {
            switch (mJcCallItem.getState()) {
                case JCCall.STATE_PENDING://响铃
                    LogUtils.e("initEvent*****:响铃");
                    if (mJcCallItem.getDirection() == JCCall.DIRECTION_IN) {
                        findViewById(R.id.iv_answer).setVisibility(View.VISIBLE);
                        findViewById(R.id.iv_answer).setOnClickListener(v -> {
                            JuphoonUtils.get().answer(mJcCallItem);
                            findViewById(R.id.iv_answer).setVisibility(View.GONE);
                        });
                    }
                    break;
                case JCCall.STATE_CONNECTING://连接中

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
                JCMediaDevice mediaDevice = JuphoonUtils.get().getMediaDevice();
                mediaDevice.enableSpeaker(false);
                LogUtils.e("initEvent*****: " + mediaDevice.isSpeakerOn());
                //如果是通话中,显示通话时间
                mCallTime = (System.currentTimeMillis() / 1000) - mJcCallItem.getTalkingBeginTime();
                setTextViewString(R.id.tv_time, "通话时间:" + TimeUtils.getHMS(mCallTime));
            } else {
                LogUtils.e("initEvent*****: " + mJcCallItem.getState());
            }
        }));
        addDisposable(RxBus.get().register("onCallItemUpdate", Integer.class).subscribe(second -> {


        }));

    }


    private void showUser(TextView name, ImageView imageView) {
        checkJcCallItem();
        UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(mJcCallItem.getUserId());
        name.setText(userMsgBeanById.getNickname());
        GlideUtils.loadHeadPortrait(userMsgBeanById.getHead_portrait(), imageView, userMsgBeanById.getPlaceholder());

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.iv_close).setOnClickListener(v -> {
            quit();
        });
        addDisposable(RxBus.get().register("onCallItemRemove", JCCallItem.class).subscribe(jcCallItem -> {
            finish();
        }));

    }


    public void quit() {
        dismissFloatingView();
        JuphoonUtils.get().hangup();
        finish();
    }

    @Override
    public void showFloatingView() {
        if (FunUtils.isServiceRunning(this, VoiceFloatingService.class.getName())) {
            //通知显示悬浮窗
           sendBroadcast(new Intent(BaseVoiceFloatingService.ACTION_SHOW_FLOATING));
        } else {
            //启动悬浮窗管理服务
            Intent service = new Intent(this, VoiceFloatingService.class);
            startService(service);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void dismissFloatingView() {
        if (FunUtils.isServiceRunning(this, VoiceFloatingService.class.getName())) {
           sendBroadcast(new Intent(BaseVoiceFloatingService.ACTION_DISMISS_FLOATING));
        }
    }

}
