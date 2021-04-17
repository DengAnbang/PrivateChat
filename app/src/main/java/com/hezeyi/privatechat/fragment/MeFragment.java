package com.hezeyi.privatechat.fragment;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.ContactUsActivity;
import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.hezeyi.privatechat.activity.account.SetupActivity;
import com.hezeyi.privatechat.activity.account.UserDetailsActivity;
import com.hezeyi.privatechat.base.BaseFragment;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.utils.StackManager;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.SPUtils;


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
    public void onFirstVisibleToUser(View view) {
        UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();
        if (userMsgBean == null) {
            return;
        }
        upUserMsg(view, userMsgBean);
        click(R.id.rl_details, view1 -> {
            Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
            intent.putExtra("user_id", MyApplication.getInstance().getUserMsgBean().getUser_id());
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
            Intent intent = new Intent(getActivity(), ContactUsActivity.class);
            startActivity(intent);
        });
    }

    private void upUserMsg(View view, UserMsgBean userMsgBean) {
        if (view == null) return;
        GlideUtils.loadHeadPortrait(userMsgBean.getHead_portrait(), view.findViewById(R.id.iv_head_portrait), userMsgBean.getPlaceholder());
        setTextViewString(R.id.tv_name, userMsgBean.getNickname());
        setTextViewString(R.id.tv_account, "账号:" + userMsgBean.getAccount());
    }

    private void ada() {
        ComponentName componentName = null;
        int sdkVersion = Build.VERSION.SDK_INT;
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //跳自启动管理
            if (sdkVersion >= 28) {//9:已测试
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity");//跳自启动管理
            } else if (sdkVersion >= 26) {//8：已测试
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.appcontrol.activity.StartupAppControlActivity");
            } else if (sdkVersion >= 23) {//7.6：已测试
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity");
            } else if (sdkVersion >= 21) {//5
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/com.huawei.permissionmanager.ui.MainActivity");
            }
            //componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");//锁屏清理
            intent.setComponent(componentName);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            //跳转失败
        }
    }
}
