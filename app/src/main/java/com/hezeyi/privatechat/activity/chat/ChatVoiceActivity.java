package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.service.VoiceFloatingService;
import com.juphoon.cloud.JCCallItem;
import com.xhab.chatui.service.BaseVoiceFloatingService;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.chatui.voiceCalls.BaseVoiceActivity;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.ToastUtil;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by dab on 2021/3/27 14:30
 */
public class ChatVoiceActivity extends BaseVoiceActivity {

    private JCCallItem mJcCallItem;
    private boolean mIsCall;
    private String mTargetId;

    @Override
    public void showUser(TextView name, ImageView imageView) {
        mJcCallItem = MyApplication.getInstance().getJCCallItem();
        mIsCall = getIntent().getBooleanExtra("isCall", false);
        mTargetId = getIntent().getStringExtra("targetId");
        String targetId;
        if (mIsCall) {
            targetId = mTargetId;
        } else {
            targetId = mJcCallItem.getExtraParam();
        }

        UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(targetId);
        name.setText(userMsgBeanById.getUser_name());
        GlideUtils.loadHeadPortrait(userMsgBeanById.getHead_portrait(), imageView, userMsgBeanById.getPlaceholder());
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJcCallItem = MyApplication.getInstance().getJCCallItem();
        mIsCall = getIntent().getBooleanExtra("isCall", false);
        boolean isCalled = getIntent().getBooleanExtra("isCalled", false);
        if (mIsCall && !isCalled) {
            //打给别人
            mTargetId = getIntent().getStringExtra("targetId");
            String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
            JuphoonUtils.get().call(mTargetId, user_id);
            findViewById(R.id.iv_answer).setVisibility(View.GONE);
        }
        if (isCalled) {
            findViewById(R.id.iv_answer).setVisibility(View.GONE);
        }
        findViewById(R.id.iv_answer).setOnClickListener(v -> {
            JuphoonUtils.get().answer(mJcCallItem);
            findViewById(R.id.iv_answer).setVisibility(View.GONE);
        });
        findViewById(R.id.iv_close).setOnClickListener(v -> {
            dismissFloatingView();

            JuphoonUtils.get().hangup();
            finish();
        });
        JuphoonUtils.get().setCallBackRemove((item, reason, description) -> {
            BaseVoiceFloatingService.StopSelf();
            ToastUtil.showToast("通话结束");
            finish();
        });
    }

    @Override
    public void showFloatingView() {
        if (FunUtils.isServiceRunning(this, VoiceFloatingService.class.getName())) {
            //通知显示悬浮窗
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BaseVoiceFloatingService.ACTION_SHOW_FLOATING));
        } else {
            //启动悬浮窗管理服务
            Intent service = new Intent(this, VoiceFloatingService.class);
            service.putExtra("isCall", mIsCall);
            service.putExtra("targetId", mTargetId);
            startService(service);
        }

    }

    @Override
    public void dismissFloatingView() {
        if (FunUtils.isServiceRunning(this, VoiceFloatingService.class.getName())) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BaseVoiceFloatingService.ACTION_DISMISS_FLOATING));
        }
    }

}
