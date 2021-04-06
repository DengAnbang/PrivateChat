package com.hezeyi.privatechat.activity.account;

import android.content.Intent;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;

/**
 * Created by dab on 2021/3/12 10:52
 */
public class SetupActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_setup;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("设置");
    }

    @Override
    public void initEvent() {
        super.initEvent();
        click(R.id.ttv_security_code, view -> {
            Intent intent = new Intent(this, SecurityCodeSetupActivity.class);
            startActivity(intent);
        });
        click(R.id.ttv_security_question, view -> {
            Intent intent = new Intent(this, SecurityQuestionSetupActivity.class);
            startActivity(intent);
        });
    }
}
