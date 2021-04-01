package com.hezeyi.privatechat.fragment;


import android.content.Intent;
import android.view.View;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.MeDetailsActivity;
import com.hezeyi.privatechat.activity.account.NewMsgSetUpActivity;
import com.hezeyi.privatechat.activity.account.SetupActivity;
import com.hezeyi.privatechat.activity.account.UserDetailsActivity;
import com.hezeyi.privatechat.base.BaseFragment;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.chatui.voiceCalls.BaseVoiceActivity;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.QRCodeUtils;


/**
 * Created by dab on 2021/3/6 14:42
 */
public class MeFragment extends BaseFragment {

    @Override
    public int viewLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    public void onVisibleToUser() {
        super.onVisibleToUser();
        LogUtils.e("onVisibleToUser*****: MeFragment");
        UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();
        if (userMsgBean == null) {
            return;
        }
        upUserMsg(getView(), userMsgBean);
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();
        LogUtils.e("onInvisibleToUser*****: MeFragment");
    }

    @Override
    public void onFirstVisibleToUser(View view) {
        UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();
        if (userMsgBean == null) {
            return;
        }
        upUserMsg(view, userMsgBean);
        click(R.id.rl_details, view1 -> {
            Intent intent = new Intent(getActivity(), MeDetailsActivity.class);
            startActivity(intent);

        });
        click(R.id.ttv_setup, view1 -> {
            Intent intent = new Intent(getActivity(), SetupActivity.class);
            startActivity(intent);

        });
        click(R.id.ttv_new_msg_setup, view1 -> {
            Intent intent = new Intent(getActivity(), NewMsgSetUpActivity.class);
            startActivity(intent);
        });
        click(R.id.ttv_qr, view1 -> {
            QRCodeUtils.scanQrCode(getActivity(), s -> {
                LogUtils.e("onFirstVisibleToUser*****: " + s);
                if (s.equals(userMsgBean.getUser_id())) {
                    Intent intent = new Intent(getActivity(), MeDetailsActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                    intent.putExtra("user_id", s);
                    startActivity(intent);

                }

            });

        });
        click(R.id.ttv_contact_us, v -> {
            Intent intent = new Intent(getActivity(), BaseVoiceActivity.class);
            startActivity(intent);
        });
    }

    private void upUserMsg(View view, UserMsgBean userMsgBean) {
        if (view == null) return;
        GlideUtils.loadHeadPortrait(userMsgBean.getHead_portrait(), view.findViewById(R.id.iv_head_portrait), userMsgBean.getPlaceholder());
        setTextViewString(R.id.tv_name, userMsgBean.getUser_name());
        setTextViewString(R.id.tv_account, "账号:" + userMsgBean.getAccount());
    }
}
