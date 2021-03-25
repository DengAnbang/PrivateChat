package com.hezeyi.privatechat.activity;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.xhab.utils.utils.SPUtils;
import com.xhab.utils.utils.ToastUtil;
import com.xhab.utils.view.VerificationCodeView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dab on 2021/3/25 11:34
 */
public class LockActivity extends BaseActivity {

    private boolean mIsSetUp;

    @Override
    public int getContentViewRes() {
        return R.layout.layout_lock;
    }

    @Override
    public void initView() {
        super.initView();
        mIsSetUp = getIntent().getBooleanExtra("isSetUp", false);
        setTitleString(mIsSetUp ? "设置安全码" : "请输入安全码");
        MyApplication.getInstance().setLock(false);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        VerificationCodeView verificationCodeView = findViewById(R.id.verification_code);
        verificationCodeView.setOnInputListener(new VerificationCodeView.OnInputListener() {
            @Override
            public void onSucess(String code) {
                if (mIsSetUp) {
                    setUp(code);
                } else {
                    verification(code);
                }

            }

            @Override
            public void onInput() {

            }
        });
        new Timer().schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) verificationCodeView.getEtCode().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(verificationCodeView.getEtCode(), 0);
            }

        }, 500);
    }

    private void setUp(String code) {
        SPUtils.save(Const.Sp.SecurityCode, code);
        ToastUtil.showToast("安全码设置成功");
        MyApplication.getInstance().setLock(true);
        finish();
    }

    private void verification(String code) {
        String string = SPUtils.getString(Const.Sp.SecurityCode, "");
        if (string.equals(code)) {
            MyApplication.getInstance().setLock(true);
            finish();
        } else {
            ToastUtil.showToast("密码错误,如果忘记,请重新登录后设置新的安全码!");
        }
    }

    @Override
    public void onBackPressed() {


    }
}
