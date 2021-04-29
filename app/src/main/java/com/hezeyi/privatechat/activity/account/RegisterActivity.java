package com.hezeyi.privatechat.activity.account;

import android.text.TextUtils;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.FunUtils;

/**
 * Created by dab on 2021/3/9 15:04
 */
public class RegisterActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_register;
    }

    @Override
    public boolean isCanLock() {
        return false;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("注册账号");

    }

    @Override
    public void initEvent() {
        super.initEvent();

        click(R.id.tv_submit, view -> {
            String account = getTextViewString(R.id.et_account);
            String password = getTextViewString(R.id.et_password);
            String passwordAgain = getTextViewString(R.id.et_password_again);
            if (FunUtils.checkIsNullable(account, "请输入账号!")) return;
            if (FunUtils.checkIsNullable(password, "请输入密码!")) return;
            if (FunUtils.checkIsNullable(passwordAgain, "请确认密码!")) return;
            if (!TextUtils.equals(password, passwordAgain)) {
                showSnackBar("两次密码不一致!");
                return;
            }
//            FunUtils.getChineseName()
            HttpManager.register(account, password, account, "", this, userMsgBean -> {
                showSnackBar("注册成功!");
                finish();
            });
        });

    }
}
