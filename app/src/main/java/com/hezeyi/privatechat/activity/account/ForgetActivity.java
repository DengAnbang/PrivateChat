package com.hezeyi.privatechat.activity.account;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.ToastUtil;

/**
 * Created by dab on 2021/3/9 15:07
 */
public class ForgetActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_forget;
    }

    private static final int ResultCode = 0x789;
    private static final int ResultChangeCode = 0x788;

    @Override
    public boolean isCanLock() {
        return false;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("找回密码");

    }

    @Override
    public void initEvent() {
        super.initEvent();
        click(R.id.tv_submit, v -> {
            String account = getTextViewString(R.id.et_account);
            if (FunUtils.checkIsNullable(account, "请输入账号!")) {
                return;
            }
            Intent intent = new Intent(this, SecurityQuestionVerifyActivity.class);
            intent.putExtra("account", account);
            MyApplication.getInstance().setLock(false);
            startActivityForResult(intent, ResultCode);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCode) {
            if (SecurityQuestionVerifyActivity.RESULT_CODE_NO_SETUP == resultCode) {
                ToastUtil.showToast("账号为设置密保,请联系客服");
                return;
            }
            if (SecurityQuestionVerifyActivity.RESULT_CODE_OK == resultCode) {
                String account = getTextViewString(R.id.et_account);
                if (FunUtils.checkIsNullable(account, "请输入账号!")) {
                    return;
                }
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra("account", account);
                MyApplication.getInstance().setLock(false);
                startActivityForResult(intent, ResultChangeCode);
                return;
            }
        } else if (requestCode == ResultChangeCode) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }

    }
}
