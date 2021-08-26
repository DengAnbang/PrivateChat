package com.hezeyi.privatechat.fragment;


import android.content.Intent;
import android.view.View;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.AccountListActivity;
import com.hezeyi.privatechat.activity.recharge.RechargeRecordActivity;
import com.hezeyi.privatechat.activity.recharge.RechargeSetUpActivity;
import com.hezeyi.privatechat.base.BaseFragment;
import com.abxh.utils.utils.LogUtils;


/**
 * Created by dab on 2021/3/6 14:42
 */
public class AdminFragment extends BaseFragment {

    @Override
    public int viewLayoutID() {
        return R.layout.fragment_admin;
    }

    @Override
    public void onVisibleToUser() {
        super.onVisibleToUser();
        LogUtils.e("onVisibleToUser*****: AdminFragment");
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();
        LogUtils.e("onInvisibleToUser*****: AdminFragment");
    }

    @Override
    public void onFirstVisibleToUser(View view) {
        click(R.id.ttv_account_list, v -> {
            Intent intent = new Intent(getActivity(), AccountListActivity.class);
            startActivity(intent);
        });
        click(R.id.ttv_recharge, v -> {
            Intent intent = new Intent(getActivity(), RechargeRecordActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);
        });
        click(R.id.ttv_recharge_set, v -> {
            Intent intent = new Intent(getActivity(), RechargeSetUpActivity.class);
            startActivity(intent);
        });

    }
}
