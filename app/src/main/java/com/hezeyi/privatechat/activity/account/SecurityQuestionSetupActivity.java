package com.hezeyi.privatechat.activity.account;

import android.content.Intent;

import com.hezeyi.privatechat.DataInMemory;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.ToastUtil;

import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/3/12 10:52
 * 安全问题设置
 */
public class SecurityQuestionSetupActivity extends BaseActivity {
    private static final int ResultCode = 0x789;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_security_question_set_up;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("安全问题设置");
    }

    @Override
    public void initEvent() {
        super.initEvent();
        click(R.id.tv_submit, view -> {
            String a1 = getTextViewString(R.id.et_a1);
            String a2 = getTextViewString(R.id.et_a2);
            String q1 = getTwoTextLinearRightText(R.id.ttv_q1);
            String q2 = getTwoTextLinearRightText(R.id.ttv_q1);
            FunUtils.checkIsNullable(q1, "问题1,不能为空!");
            FunUtils.checkIsNullable(a1, "答案1,不能为空!");
            FunUtils.checkIsNullable(q2, "问题2,不能为空!");
            FunUtils.checkIsNullable(a2, "答案2,不能为空!");
            String account = DataInMemory.getInstance().getUserMsgBean().getAccount();
            HttpManager.securityUpdate(account, q1, a1, q2, a2, this, o -> {
                ToastUtil.showToast("设置成功!");
                finish();
            });
        });
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = new Intent(this, SecurityQuestionVerifyActivity.class);
        startActivityForResult(intent, ResultCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCode) {
            if (SecurityQuestionVerifyActivity.RESULT_CODE_NO_SETUP == resultCode) {
                return;
            }
            if (SecurityQuestionVerifyActivity.RESULT_CODE_OK == resultCode) {
                return;
            }
        }
        finish();
    }
}
