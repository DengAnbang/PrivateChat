package com.hezheyi.main.activity;

import android.content.Intent;

import com.abxh.core.MmkvHelper;
import com.abxh.jetpack.IViewModel;
import com.hezeyi.common.base.BaseActivity;

/**
 * 启动页
 *
 * @author Chony
 * @time 2017/2/6 9:00
 */

public class SplashActivity extends BaseActivity<IViewModel> {
    @Override
    public void initView() {
        super.initView();
        // TODO: 2021/9/25   //上次登录以后,超过了3天,清空密码,重新登录
        Boolean isGuiderActivity = MmkvHelper.getInstance().getBoolean("isGuiderActivity", false);
        if (!isGuiderActivity) {
            startActivity(new Intent(SplashActivity.this, GuiderActivity.class));
            finish();
        }else {

        }

    }


    @Override
    public int getContentViewRes() {
        return 0;
    }
}
