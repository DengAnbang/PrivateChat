package com.hezeyi.privatechat.activity.account;

import android.view.Gravity;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.ToastUtil;

/**
 * Created by dab on 2021/3/9 20:33
 * user_id
 */
public class UserDetailsActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_user_details;
    }

    private String to_user_id;

    @Override
    public void initView() {
        super.initView();
        setTitleString("个人信息");
        String user_id = getIntent().getStringExtra("user_id");
        HttpManager.userSelectById(user_id, true, this, userMsgBean -> {
            if (userMsgBean != null) {
                to_user_id = userMsgBean.getUser_id();
                setTwoTextLinearRightText(R.id.ttv_account, userMsgBean.getAccount()).getRightTextView().setGravity(Gravity.RIGHT);
                setTwoTextLinearRightText(R.id.ttv_name, userMsgBean.getUser_name()).getRightTextView().setGravity(Gravity.RIGHT);
            } else {
                ToastUtil.showToast("用户不存在!");
                finish();
            }
        });
        UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();
        click(R.id.tv_submit, view -> {
            HttpManager.addFriend(userMsgBean.getUser_id(), to_user_id, "1", this, o -> {
                ToastUtil.showToast("添加成功!");
                finish();
            });
        });
    }


}
