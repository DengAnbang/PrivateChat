package com.hezeyi.privatechat.activity.account;

import android.Manifest;
import android.content.Intent;

import com.hezeyi.privatechat.BuildConfig;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MainActivity;
import com.hezeyi.privatechat.MyApplication;
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
        click(R.id.tv_submit, view -> checkLogin());
        click(R.id.tv_forget, view -> {
            Intent intent = new Intent(this, ForgetActivity.class);
            startActivity(intent);
        });
        click(R.id.tv_register, view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        String account = SPUtils.getString(Const.Sp.account, "");
        String password = SPUtils.getString(Const.Sp.password, "");
        setTextViewString(R.id.et_account, account);
        setTextViewString(R.id.et_password, password);
        setTextViewString(R.id.tv_version, "版本号:v" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        requestPermissions();
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
            MyApplication.getInstance().setUserMsgBean(userMsgBean);
            SPUtils.save(Const.Sp.account, account);
            SPUtils.save(Const.Sp.password, password);
            getUserList();


        });
    }

    private void getUserList() {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, this, userMsgBeans -> {
            MyApplication.getInstance().setUserMsgBeans(userMsgBeans);
            HttpManager.groupSelectList(user_id, this, chatGroupBeans -> {
                MyApplication.getInstance().setChatGroupBeans(chatGroupBeans);
                MyApplication.getInstance().setLock(true);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        Disposable subscribe = rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                Manifest.permission.RECORD_AUDIO
        )
                .subscribe(aBoolean -> {
                    if (aBoolean) {

                    } else {
                        showSnackBar("请到设置见面打开所需权限!");
//                        gotoHuaweiPermission();
                    }
                });
        addDisposable(subscribe);
    }


}
