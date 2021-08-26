package com.hezeyi.privatechat.activity.account;

import android.content.Intent;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.LockActivity;
import com.hezeyi.privatechat.base.BaseActivity;
import com.abxh.utils.utils.SPUtils;
import com.abxh.utils.view.TwoTextLinearView;

/**
 * Created by dab on 2021/3/12 10:52
 */
public class SecurityCodeSetupActivity extends BaseActivity {

    private boolean isOpen;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_security_code_set_up;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("安全码");

        isOpen = SPUtils.getBoolean(Const.Sp.isOpenSecurityCode, true);
        TwoTextLinearView isOpenTwoTextLinearView = findViewById(R.id.ttv_is_open);
        isOpenTwoTextLinearView.setRightDrawable(this.isOpen ? R.mipmap.c25_icon1 : R.mipmap.c25_icon2);
        click(R.id.ttv_is_open, v -> {
            isOpen = !isOpen;
            isOpenTwoTextLinearView.setRightDrawable(this.isOpen ? R.mipmap.c25_icon1 : R.mipmap.c25_icon2);
            SPUtils.save(Const.Sp.isOpenSecurityCode, isOpen);
        });
        TwoTextLinearView twoTextLinearView = findViewById(R.id.ttv_set_security_code);
        twoTextLinearView.setOnClickListener(v -> {
            Intent intent = new Intent(this, LockActivity.class);
            intent.putExtra("isSetUp", true);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TwoTextLinearView twoTextLinearView = findViewById(R.id.ttv_set_security_code);
        String string = SPUtils.getString(Const.Sp.SecurityCode+ MyApplication.getInstance().getUserMsgBean().getUser_id(), "");
        if (string.equals("")) {
            twoTextLinearView.setLeftText("\u3000设置安全码\u3000");
        } else {
            twoTextLinearView.setLeftText("\u3000修改安全码\u3000");
        }
    }
}
