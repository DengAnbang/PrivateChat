package com.hezeyi.privatechat.activity.account;

import android.content.Intent;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.tencent.bugly.beta.Beta;
import com.xhab.utils.activity.WhitelistActivity;

import androidx.annotation.Nullable;

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
        click(R.id.ttv_new_msg_setup, view1 -> {
            Intent intent = new Intent(this, NewMsgSetUpActivity.class);
            startActivity(intent);
        });
        click(R.id.ttv_check_upgrade, view1 -> {
            Beta.checkUpgrade();
        });
        click(R.id.ttv_privacy, view1 -> {

        });
        click(R.id.ttv_permission, view1 -> {
            MyApplication.getInstance().setLock(false);
            startActivityForResult(new Intent(this, WhitelistActivity.class), 0x82);
        });
        click(R.id.ttv_permission, view1 -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            intent.putExtra("account", MyApplication.getInstance().getUserMsgBean().getAccount());
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x82) {
            MyApplication.getInstance().setLock(isCanLock());
        }
    }
}
