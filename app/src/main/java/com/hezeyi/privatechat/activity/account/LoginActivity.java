package com.hezeyi.privatechat.activity.account;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;

import com.hezeyi.privatechat.DataInMemory;
import com.hezeyi.privatechat.MainActivity;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.service.ChatService;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.SPUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/8 20:36
 */
public class LoginActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        super.initView();
        click(R.id.tv_submit, view -> requestPermissions());
        click(R.id.tv_forget, view -> {
            Intent intent = new Intent(this, ForgetActivity.class);
            startActivity(intent);
        });
        click(R.id.tv_register, view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        String account = SPUtils.getString("account", "");
        String password = SPUtils.getString("password", "");
        setTextViewString(R.id.et_account, account);
        setTextViewString(R.id.et_password, password);
    }

    private void checkLogin() {
        String account = getTextViewString(R.id.et_account);
        String password = getTextViewString(R.id.et_password);
        if (FunUtils.checkIsNullable(account, "请输入账号!")) return;
        if (FunUtils.checkIsNullable(password, "请输入密码!")) return;
        login(account, password);
    }

    private void login(String account, String password) {
        HttpManager.login(account, password, this, userMsgBean -> {
            Intent startIntent = new Intent(this, ChatService.class);
            startIntent.putExtra("userId", userMsgBean.getUser_id());
            startIntent.putExtra("account", account);
            startIntent.putExtra("password", password);
            startService(startIntent);
            DataInMemory.getInstance().setUserMsgBean(userMsgBean);
            SPUtils.save("account", account);
            SPUtils.save("password", password);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        Disposable subscribe = rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        )
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        checkLogin();
                    } else {
                        showSnackBar("请到设置见面打开所需权限!");
//                        gotoHuaweiPermission();
                    }
                });
        addDisposable(subscribe);
    }

    /**
     * 华为的权限管理页面
     */
    private void gotoHuaweiPermission() {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
//                startActivity(getAppDetailSettingIntent());
        }

    }
}
