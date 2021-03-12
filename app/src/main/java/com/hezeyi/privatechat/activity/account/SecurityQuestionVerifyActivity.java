package com.hezeyi.privatechat.activity.account;

import com.hezeyi.privatechat.DataInMemory;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.SecurityBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;

/**
 * Created by dab on 2021/3/12 10:52
 * 安全问题验证
 */
public class SecurityQuestionVerifyActivity extends BaseActivity {
    public static final int RESULT_CODE_OK = RESULT_OK;//验证通过
    public static final int RESULT_CODE_NO_SETUP = 0x10;//没有设置问题

    @Override
    public int getContentViewRes() {
        return R.layout.activity_security_question_set_up;
    }

    private SecurityBean mSecurityBean;

    @Override
    public void initView() {
        super.initView();
        setTitleString("安全问题验证");
    }

    @Override
    public void initEvent() {
        super.initEvent();
        click(R.id.tv_submit, view -> {
            String a1 = getTextViewString(R.id.et_a1);
            String a2 = getTextViewString(R.id.et_a2);
            if (a1.equals(mSecurityBean.getAnswer1()) && a2.equals(mSecurityBean.getAnswer2())) {
                setResult(RESULT_CODE_OK);
                finish();
            } else {
                showSnackBar("问题答案错误!");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        UserMsgBean userMsgBean = DataInMemory.getInstance().getUserMsgBean();
        HttpManager.securitySelect(userMsgBean.getAccount(), this, securityBean -> {
            if (securityBean.getQuestion1().equals("") || securityBean.getQuestion2().equals("")) {
                setResult(RESULT_CODE_NO_SETUP);
                finish();
            } else {
                mSecurityBean = securityBean;
                setTwoTextLinearRightText(R.id.ttv_q1, securityBean.getQuestion1());
                setTwoTextLinearRightText(R.id.ttv_q2, securityBean.getQuestion2());
            }
        });
    }


}
