package com.hezeyi.privatechat.service;

import android.content.Intent;
import android.view.View;

import com.hezeyi.privatechat.activity.chat.ChatVoiceActivity;
import com.xhab.chatui.service.BaseVoiceFloatingService;
import com.xhab.chatui.voiceCalls.VoiceFloatingView;

/**
 * Created by dab on 2021/3/31 16:13
 */
public class VoiceFloatingService extends BaseVoiceFloatingService {


    @Override
    public View.OnLongClickListener setFloatingLongClickListener(VoiceFloatingView voiceFloatingView, String targetId, boolean isCall) {
        return v -> {
            Intent intent = new Intent(VoiceFloatingService.this, ChatVoiceActivity.class);
            intent.putExtra("targetId", targetId);
            intent.putExtra("isCall", isCall);
            intent.putExtra("isCalled", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            voiceFloatingView.dismiss();
            return true;
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
