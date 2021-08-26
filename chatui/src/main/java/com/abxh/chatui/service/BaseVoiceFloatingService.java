package com.abxh.chatui.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.View;

import com.abxh.chatui.voiceCalls.VoiceFloatingView;



/**
 * Created by dab on 2021/3/31 14:20
 */
public abstract class BaseVoiceFloatingService extends Service {
    public static final String ACTION_SHOW_FLOATING = "action_show_floating";
    public static final String ACTION_DISMISS_FLOATING = "action_dismiss_floating";
    private static BaseVoiceFloatingService mServiceVoice;
    private VoiceFloatingView mVoiceFloatingView;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_SHOW_FLOATING.equals(intent.getAction())) {
                mVoiceFloatingView.show();
            } else if (ACTION_DISMISS_FLOATING.equals(intent.getAction())) {
                mVoiceFloatingView.dismiss();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void StopSelf() {
        if (mServiceVoice == null) return;
        mServiceVoice.stopSelf();
    }

    public abstract View.OnLongClickListener setFloatingLongClickListener(VoiceFloatingView voiceFloatingView);

    public abstract View.OnClickListener setFloatingClickListener(VoiceFloatingView voiceFloatingView);

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceVoice = this;
        mVoiceFloatingView = new VoiceFloatingView(this);
        IntentFilter intentFilter = new IntentFilter(ACTION_SHOW_FLOATING);
       registerReceiver(mLocalBroadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //显示悬浮窗
        mVoiceFloatingView.show();
        mVoiceFloatingView.setOnClickListener(setFloatingClickListener(mVoiceFloatingView));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mVoiceFloatingView.dismiss();
        mVoiceFloatingView = null;
       unregisterReceiver(mLocalBroadcastReceiver);
        super.onDestroy();

    }
}
