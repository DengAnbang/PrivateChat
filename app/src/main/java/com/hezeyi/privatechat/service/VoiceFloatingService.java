package com.hezeyi.privatechat.service;

import android.content.Intent;
import android.view.View;

import com.hezeyi.privatechat.activity.chat.ChatVoiceActivity;
import com.xhab.chatui.service.BaseVoiceFloatingService;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.chatui.voiceCalls.VoiceFloatingView;
import com.xhab.utils.utils.FunUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by dab on 2021/3/31 16:13
 */
public class VoiceFloatingService extends BaseVoiceFloatingService {

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
    public void onCreate() {
        super.onCreate();
        JuphoonUtils.get().setCallBackRemove((item, reason, description) -> {
            if (FunUtils.isServiceRunning(this, VoiceFloatingService.class.getName())) {
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BaseVoiceFloatingService.ACTION_DISMISS_FLOATING));
                stopSelf();
            }
        });
    }

}
