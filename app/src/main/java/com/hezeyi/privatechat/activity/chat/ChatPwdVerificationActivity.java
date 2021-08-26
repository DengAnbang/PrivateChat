package com.hezeyi.privatechat.activity.chat;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.abxh.utils.utils.LogUtils;
import com.abxh.utils.utils.SPUtils;
import com.abxh.utils.utils.ToastUtil;
import com.abxh.utils.view.PayPsdInputView;

/**
 * Created by dab on 2021/4/20 11:36
 */
public class ChatPwdVerificationActivity extends BaseActivity {

    @Override
    public int getContentViewRes() {
        return R.layout.activity_chat_pwd_verification;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        setTitleString("聊天码");
    }

    @Override
    public void initView() {
        super.initView();
        String chat_pwd = getIntent().getStringExtra("chat_pwd");
        String sp_key = getIntent().getStringExtra("sp_key");
        PayPsdInputView payPsdInputView = findViewById(R.id.ppv_chat_pwd);
        payPsdInputView.setComparePassword(chat_pwd, new PayPsdInputView.onPasswordListener() {
            @Override
            public void onDifference(String oldPsd, String newPsd) {
                ToastUtil.showToast("聊天码错误,请联系对方查看!");
            }

            @Override
            public void onEqual(String psd) {
                LogUtils.e("onEqual*****: " + psd);
                SPUtils.save(sp_key, psd);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void inputFinished(String inputPsd) {
                LogUtils.e("inputFinished*****: " + inputPsd);
            }
        });
    }

}
