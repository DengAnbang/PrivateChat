package com.hezeyi.privatechat.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.hezeyi.privatechat.DataInMemory;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.MeDetailsActivity;
import com.hezeyi.privatechat.base.BaseFragment;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.QRCodeUtils;
import com.xhab.utils.utils.RxUtils;


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
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();
        LogUtils.e("onInvisibleToUser*****: MeFragment");
    }

    @Override
    public void onFirstVisibleToUser(View view) {
        setTitleString("我的");
        UserMsgBean userMsgBean = DataInMemory.getInstance().getUserMsgBean();
        if (userMsgBean != null) {
            setTextViewString(R.id.tv_name, userMsgBean.getUser_name());
            setTextViewString(R.id.tv_account, "账号:" + userMsgBean.getAccount());

        }
        click(R.id.rl_details, view1 -> {
            Intent intent = new Intent(getActivity(), MeDetailsActivity.class);
            startActivity(intent);

        });
        click(R.id.ttv_qr, view1 -> {
            QRCodeUtils.scanQrCode(getActivity(), s -> {
                LogUtils.e("onFirstVisibleToUser*****: " + s);
//                showToast(s);
                RxUtils.runOnUiThread(() -> {
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                });

            });

        });

    }
}
