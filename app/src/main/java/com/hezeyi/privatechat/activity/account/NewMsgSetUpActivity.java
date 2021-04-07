package com.hezeyi.privatechat.activity.account;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.xhab.utils.utils.SPUtils;
import com.xhab.utils.view.TwoTextLinearView;

/**
 * Created by dab on 2021/3/12 10:52
 */
public class NewMsgSetUpActivity extends BaseActivity {

    private boolean mIsNewMsg;
    private boolean mIsNewMsgDes;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_new_msg_set_up;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("新消息通知");
        mIsNewMsg = SPUtils.getBoolean(Const.Sp.isNewMsgCode, true);
        TwoTextLinearView isOpenTwoTextLinearView = findViewById(R.id.ttv_new_msg);
        isOpenTwoTextLinearView.setRightDrawable(mIsNewMsg ? R.mipmap.c25_icon1 : R.mipmap.c25_icon2);
        click(R.id.ttv_new_msg, v -> {
            mIsNewMsg = !mIsNewMsg;
            isOpenTwoTextLinearView.setRightDrawable(mIsNewMsg ? R.mipmap.c25_icon1 : R.mipmap.c25_icon2);
            SPUtils.save(Const.Sp.isNewMsgCode, mIsNewMsg);
        });
        mIsNewMsgDes = SPUtils.getBoolean(Const.Sp.isNewMsgDesCode, false);
        TwoTextLinearView twoTextLinearView = findViewById(R.id.ttv_new_msg_des);
        twoTextLinearView.setRightDrawable(mIsNewMsgDes ? R.mipmap.c25_icon1 : R.mipmap.c25_icon2);
        click(R.id.ttv_new_msg_des, v -> {
            mIsNewMsgDes = !mIsNewMsgDes;
            twoTextLinearView.setRightDrawable(mIsNewMsgDes ? R.mipmap.c25_icon1 : R.mipmap.c25_icon2);
            SPUtils.save(Const.Sp.isNewMsgDesCode, mIsNewMsgDes);
        });
    }
}
