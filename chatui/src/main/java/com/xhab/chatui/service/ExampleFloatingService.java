package com.xhab.chatui.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.LayoutInflater;

import com.xhab.chatui.utils.FloatingWindowHelper;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by dab on 2021/3/31 14:06
 */
public class ExampleFloatingService extends Service {
    private static final String ACTION_CLICK = "action_click";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CLICK.equals(intent.getAction())) {
                onClick();
            }
        }
    };
    private FloatingWindowHelper mFloatingWindowHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        isStart = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter(ACTION_CLICK));
        mFloatingWindowHelper = new FloatingWindowHelper(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        mExampleViewA = layoutInflater.inflate(R.layout.widget_test_view, null, false)
//        mExampleViewB = layoutInflater.inflate(R.layout.widget_test_view_b, null, false)
        onClick();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(mLocalBroadcastReceiver);
        mFloatingWindowHelper.destroy();
        isStart = false;
        super.onDestroy();

    }

    private boolean isStart;

    private void onClick() {
//        if (!mFloatingWindowHelper.contains(mExampleViewA)) {
//            mFloatingWindowHelper.addView(mExampleViewA);
//        } else if (!mFloatingWindowHelper.contains(mExampleViewB)) {
//            mFloatingWindowHelper.addView(mExampleViewB, 100, 100, true);
//        } else {
//            mFloatingWindowHelper.clear();
//        }
    }
}
