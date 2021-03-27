package com.hezeyi.privatechat.activity.chat;

import com.hezeyi.privatechat.DataInMemory;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.juphoon.cloud.JCCallItem;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.utils.ToastUtil;

/**
 * Created by dab on 2021/3/27 14:30
 */
public class ChatVoiceActivity extends BaseActivity {

    private JCCallItem mJcCallItem;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_chat_voice;
    }

    @Override
    public void initData() {
        super.initData();
        mJcCallItem = DataInMemory.getInstance().getJCCallItem();
        boolean isCall = getIntent().getBooleanExtra("isCall", false);
        if (isCall) {
            String target_name = getIntent().getStringExtra("target_name");
            setTitleString(target_name);
            String targetId = getIntent().getStringExtra("targetId");
            JuphoonUtils.get().call(targetId, DataInMemory.getInstance().getUserMsgBean().getUser_name());
            visibility(R.id.tv_next, false);
        } else {
            String extraParam = mJcCallItem.getExtraParam();
            setTitleString(extraParam);
        }


    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void initEvent() {
        super.initEvent();
        click(R.id.tv_next, v -> {
            JuphoonUtils.get().answer(mJcCallItem);
            visibility(R.id.tv_next, false);
        });
        click(R.id.tv_finish, v -> {
            JuphoonUtils.get().hangup();
            finish();
        });
        JuphoonUtils.get().setCallBackRemove((item, reason, description) -> {
            ToastUtil.showToast("通话结束");
            finish();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        JuphoonUtils.get().hangup();
    }
}
