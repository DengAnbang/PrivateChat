package com.hezheyi.main.activity;

import android.content.Intent;
import android.os.Bundle;

import com.abxh.core.MmkvHelper;
import com.abxh.jetpack.IViewModel;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hezeyi.common.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * 启动页
 *
 * @author Chony
 * @time 2017/2/6 9:00
 */

public class SplashActivity extends BaseActivity<IViewModel> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 2021/9/25   //上次登录以后,超过了3天,清空密码,重新登录
        Boolean isGuiderActivity = MmkvHelper.getInstance().getBoolean("isGuiderActivity", false);
        if (!isGuiderActivity) {
            startActivity(new Intent(SplashActivity.this, GuiderActivity.class));
        }else {
            ARouter.getInstance().build("/user/login").navigation();
        }
        finish();
    }
}
