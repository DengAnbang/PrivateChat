package com.hezeyi.privatechat.activity.account;

import android.text.TextUtils;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.ToastUtil;

/**
 * Created by dab on 2021/4/24 18:28
 * account
 */

public class ChangePasswordActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        setTitleString("修改密码");
        click(R.id.tv_submit, view -> {
            String password = getTextViewString(R.id.et_password);
            String passwordAgain = getTextViewString(R.id.et_password_again);
            if (FunUtils.checkIsNullable(password, "请输入密码!")) return;
            if (FunUtils.checkIsNullable(passwordAgain, "请确认密码!")) return;
            if (!TextUtils.equals(password, passwordAgain)) {
                showSnackBar("两次密码不一致!");
                return;
            }
            String account = getIntent().getStringExtra("account");
            HttpManager.userUpdate(account, password, "", "", "", this, userMsgBean -> {
                ToastUtil.showToast("修改成功!");
                setResult(RESULT_OK);
                finish();
            });
        });

    }
}
