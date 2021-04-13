package com.hezeyi.privatechat.activity.account;

import android.content.Intent;
import android.view.Gravity;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.TimeUtils;
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
        String myUser_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        if (user_id.equals(myUser_id)) {
            Intent intent = new Intent(this, MeDetailsActivity.class);
            startActivity(intent);
            finish();
        }
        UserMsgBean userMsgBeanById = MyApplication.getInstance().getFriendUserMsgBeanById(user_id);
        visibility(R.id.tv_send, userMsgBeanById != null);
        visibility(R.id.tv_submit, userMsgBeanById == null);
        if (userMsgBeanById != null) {
            setRightTitleString("删除好友", v -> {
                FunUtils.affirm(this, "确认删除好友?", "删除", aBoolean -> {
                    if (aBoolean) {
                        HttpManager.friendDelete(myUser_id, userMsgBeanById.getUser_id(), this, o -> {
                            MyApplication.getInstance().removeFriendUserMsgBeanById(userMsgBeanById.getUser_id());
                            ToastUtil.showToast("删除成功!");
                            finish();
                        });
                    }
                });
            });
        }

        HttpManager.userSelectById(user_id, true, this, userMsgBean -> {
            if (userMsgBean != null) {
                to_user_id = userMsgBean.getUser_id();
                setTwoTextLinearRightText(R.id.ttv_account, userMsgBean.getAccount()).getRightTextView().setGravity(Gravity.RIGHT);
                setTwoTextLinearRightText(R.id.ttv_name, userMsgBean.getUser_name()).getRightTextView().setGravity(Gravity.RIGHT);
                setTwoTextLinearRightText(R.id.ttv_vip_time, TimeUtils.toTimeByString(userMsgBean.getVip_time())).getRightTextView().setGravity(Gravity.RIGHT);
                visibility(R.id.ttv_vip_time, MyApplication.getInstance().getUserMsgBean().isAdmin());
                GlideUtils.loadHeadPortrait(userMsgBean.getHead_portrait(), findViewById(R.id.iv_head_portrait), userMsgBean.getPlaceholder());
            } else {
                ToastUtil.showToast("用户不存在!");
                finish();
            }
        });

        UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();
        click(R.id.tv_submit, view -> {
            HttpManager.friendAdd(userMsgBean.getUser_id(), to_user_id, "2", this, o -> {
                ToastUtil.showToast("提交成功!");
                finish();
            });
        });
    }


}
