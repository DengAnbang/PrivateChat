package com.hezeyi.privatechat.activity.account;

import android.content.Intent;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.xhab.utils.StackManager;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.SPUtils;

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
        click(R.id.ttv_login_out, view -> {
            RxBus.get().post(Const.RxType.TYPE_LOGIN_OUT, Object.class);
            SPUtils.save(Const.Sp.password, "");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            StackManager.finishExcludeActivity(LoginActivity.class);
        });
    }
}
