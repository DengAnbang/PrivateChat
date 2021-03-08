package com.hezeyi.privatechat.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.hezeyi.privatechat.MainActivity;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.base.BaseActivity;

/**
 * Created by dab on 2021/3/8 20:36
 */
public class LoginActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        super.initView();
        click(R.id.tv_submit, view -> checkLogin());
        click(R.id.tv_forget, view -> {

        });
        click(R.id.tv_register, view -> {

        });
    }

    private void checkLogin() {
        String account = getTextViewString(R.id.et_account);
        String password = getTextViewString(R.id.et_password);
        if (checkIsNullable(account, "请输入账号!")) return;
        if (checkIsNullable(password, "请输入密码!")) return;
        login(account, password);
    }

    private void login(String account, String password) {
        HttpManager.login(account, password, this, userMsgBean -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean checkIsNullable(String s, String hint) {
        if (TextUtils.isEmpty(s)) {
            showToast(hint);
            return true;
        }
        return false;
    }

}
