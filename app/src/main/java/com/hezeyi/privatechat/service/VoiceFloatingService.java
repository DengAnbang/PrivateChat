package com.hezeyi.privatechat.service;

import android.content.Intent;
import android.view.View;

import com.hezeyi.privatechat.activity.chat.ChatVoiceActivity;
import com.juphoon.cloud.JCCallItem;
import com.xhab.chatui.service.BaseVoiceFloatingService;
import com.xhab.chatui.voiceCalls.VoiceFloatingView;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.RxBus;


import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/31 16:13
 */
public class VoiceFloatingService extends BaseVoiceFloatingService {

    private Disposable mDisposable;

    @Override
    public View.OnLongClickListener setFloatingLongClickListener(VoiceFloatingView voiceFloatingView) {
        return v -> {
            Intent intent = new Intent(VoiceFloatingService.this, ChatVoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            voiceFloatingView.dismiss();
            return true;
        };
    }

    @Override
    public View.OnClickListener setFloatingClickListener(VoiceFloatingView voiceFloatingView) {
        return v -> {
            Intent intent = new Intent(VoiceFloatingService.this, ChatVoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            voiceFloatingView.dismiss();
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDisposable = RxBus.get().register("onCallItemRemove", JCCallItem.class).subscribe(jcCallItem -> {
            if (FunUtils.isServiceRunning(this, VoiceFloatingService.class.getName())) {
                sendBroadcast(new Intent(BaseVoiceFloatingService.ACTION_DISMISS_FLOATING));
                stopSelf();
            }
        });

    }

}
