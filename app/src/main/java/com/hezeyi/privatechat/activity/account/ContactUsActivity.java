package com.hezeyi.privatechat.activity.account;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;

/**
 * Created by dab on 2021/4/15 15:39
 */
public class ContactUsActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_contact_us;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        setTitleString("联系我们");
    }
}
