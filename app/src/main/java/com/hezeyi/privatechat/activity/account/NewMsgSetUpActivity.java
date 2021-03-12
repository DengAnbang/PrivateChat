package com.hezeyi.privatechat.activity.account;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;

/**
 * Created by dab on 2021/3/12 10:52
 */
public class NewMsgSetUpActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_new_msg_set_up;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("新消息通知");
    }
}
