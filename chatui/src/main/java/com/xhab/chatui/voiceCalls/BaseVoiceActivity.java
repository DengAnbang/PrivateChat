package com.xhab.chatui.voiceCalls;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhab.chatui.R;
import com.xhab.chatui.service.BaseVoiceFloatingService;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by dab on 2021/3/31 13:26
 */
public abstract class BaseVoiceActivity extends AppCompatActivity {
//    private boolean mIsClose;

    //显示通话对方的名字.头像
    public abstract void showUser(TextView name, ImageView imageView);
    public abstract void showFloatingView();
    public abstract void dismissFloatingView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
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
        showUser(findViewById(R.id.tv_name), findViewById(R.id.iv_head_portrait));
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        dismissFloatingView();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (!mIsClose) {
//            //显示悬浮窗
//            showFloatingView();
//        }
//    }

    @Override
    public void onBackPressed() {

    }


}
