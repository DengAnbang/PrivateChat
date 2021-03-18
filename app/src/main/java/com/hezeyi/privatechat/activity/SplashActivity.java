package com.hezeyi.privatechat.activity;

import android.content.Intent;

import com.hezeyi.privatechat.MyApplication;
import com.xhab.utils.base.BaseUtilActivity;

/**
 * 启动页
 *
 * @author Chony
 * @time 2017/2/6 9:00
 */

public class SplashActivity extends BaseUtilActivity {


    @Override
    public void initView() {
        super.initView();
        MyApplication.getInstance().connectSocket();
        //停留1.5S进入主页
        getWindow().getDecorView().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, GuiderActivity.class));
            finish();
        }, 1500);
    }

    @Override
    public int getContentViewRes() {
        return 0;
    }
}
