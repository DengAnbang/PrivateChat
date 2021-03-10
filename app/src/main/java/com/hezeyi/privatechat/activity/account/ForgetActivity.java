package com.hezeyi.privatechat.activity.account;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;

/**
 * Created by dab on 2021/3/9 15:07
 */
public class ForgetActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_forget;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("找回密码");
    }
}
