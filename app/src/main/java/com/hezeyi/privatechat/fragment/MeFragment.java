package com.hezeyi.privatechat.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.hezeyi.privatechat.activity.account.MeDetailsActivity;
import com.hezeyi.privatechat.activity.account.SetupActivity;
import com.hezeyi.privatechat.base.BaseFragment;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.utils.StackManager;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.SPUtils;

import androidx.annotation.RequiresApi;


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

        click(R.id.ttv_login_out, view1 -> {
            RxBus.get().post(Const.RxType.TYPE_LOGIN_OUT, Object.class);
            SPUtils.save(Const.Sp.password, "");
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            StackManager.finishExcludeActivity(LoginActivity.class);
        });
        click(R.id.ttv_contact_us, v -> {
            if (!isIgnoringBatteryOptimizations()) {
                requestIgnoreBatteryOptimizations();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getActivity().getPackageName());
        }
        return isIgnoring;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void upUserMsg(View view, UserMsgBean userMsgBean) {
        if (view == null) return;
        GlideUtils.loadHeadPortrait(userMsgBean.getHead_portrait(), view.findViewById(R.id.iv_head_portrait), userMsgBean.getPlaceholder());
        setTextViewString(R.id.tv_name, userMsgBean.getUser_name());
        setTextViewString(R.id.tv_account, "账号:" + userMsgBean.getAccount());
    }
}
