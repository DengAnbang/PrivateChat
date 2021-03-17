package com.hezeyi.privatechat.activity.account;

import android.content.Intent;
import android.view.Gravity;

import com.hezeyi.privatechat.DataInMemory;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;

/**
 * Created by dab on 2021/3/9 20:33
 */
public class MeDetailsActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_me_details;
    }


    @Override
    public void initView() {
        super.initView();
        setTitleString("个人信息");
        UserMsgBean userMsgBean = DataInMemory.getInstance().getUserMsgBean();
        if (userMsgBean != null) {
            setTwoTextLinearRightText(R.id.ttv_account, userMsgBean.getAccount()).getRightTextView().setGravity(Gravity.RIGHT);
            setTwoTextLinearRightText(R.id.ttv_name, userMsgBean.getUser_name()).getRightTextView().setGravity(Gravity.RIGHT);
        }
        click(R.id.ttv_qr, view -> {
            Intent intent = new Intent(this, MeQrCodeActivity.class);
            startActivity(intent);
        });
        click(R.id.ttv_name, view -> {
//            HttpManager.userUpdate(userMsgBean.getAccount(), "", "修改名字测试1", "", this, userMsgBean1 -> {
//                showSnackBar("修改完成");
//            });
        });
    }


}
