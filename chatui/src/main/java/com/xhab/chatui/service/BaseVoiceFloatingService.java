package com.xhab.chatui.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.View;

import com.xhab.chatui.voiceCalls.VoiceFloatingView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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

    public abstract View.OnLongClickListener setFloatingLongClickListener(VoiceFloatingView voiceFloatingView, String targetId, boolean isCall);

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceVoice = this;
        mVoiceFloatingView = new VoiceFloatingView(this);
        IntentFilter intentFilter = new IntentFilter(ACTION_SHOW_FLOATING);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String targetId = intent.getStringExtra("targetId");
        boolean isCall = intent.getBooleanExtra("isCall", false);
        //显示悬浮窗
        mVoiceFloatingView.show();
        mVoiceFloatingView.setOnLongClickListener(setFloatingLongClickListener(mVoiceFloatingView,targetId,isCall));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mVoiceFloatingView.dismiss();
        mVoiceFloatingView = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        super.onDestroy();

    }
}
