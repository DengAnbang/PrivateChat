package com.hezeyi.privatechat.activity.account;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.tencent.bugly.beta.Beta;
import com.xhab.utils.activity.WhitelistActivity;

/**
 * Created by dab on 2021/3/12 10:52
 */
public class SetupActivity extends BaseActivity {
    private static final int KEY_WHITELIST_ACTIVITY = 82;
    private static final int KEY_CHECK_IS_ANDROID_O = 83;

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
//            HttpManager.updatesCheck(true, this);
        });
        click(R.id.ttv_privacy, view1 -> {

        });
        click(R.id.ttv_permission, view1 -> {
            MyApplication.getInstance().setLock(false);
            startActivityForResult(new Intent(this, WhitelistActivity.class), KEY_WHITELIST_ACTIVITY);
        });
//        click(R.id.ttv_permission, view1 -> {
//            Intent intent = new Intent(this, ChangePasswordActivity.class);
//            intent.putExtra("account", MyApplication.getInstance().getUserMsgBean().getAccount());
//            startActivity(intent);
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication.getInstance().setLock(isCanLock());

    }
}
