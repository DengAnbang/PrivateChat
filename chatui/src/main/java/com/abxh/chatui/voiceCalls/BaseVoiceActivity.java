package com.abxh.chatui.voiceCalls;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.abxh.chatui.R;
import com.abxh.chatui.service.BaseVoiceFloatingService;
import com.abxh.utils.base.BaseUtilActivity;

/**
 * Created by dab on 2021/3/31 13:26
 */
public abstract class BaseVoiceActivity extends BaseUtilActivity {


    public abstract void showFloatingView();

    public abstract void dismissFloatingView();

    @Override
    public void initEvent() {
        super.initEvent();
        //Android 6.0 以下无需获取权限，可直接展示悬浮窗
        findViewById(R.id.iv_dismiss).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //判断是否拥有悬浮窗权限，无则跳转悬浮窗权限授权页面
                if (Settings.canDrawOverlays(this)) {
                    showFloatingView();
                    finish();
                } else {
                    //跳转悬浮窗权限授权页面
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            } else {
                showFloatingView();
                finish();
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(v -> {
            BaseVoiceFloatingService.StopSelf();
            finish();
        });
    }

    @Override
    public void onBackPressed() {

    }


}
