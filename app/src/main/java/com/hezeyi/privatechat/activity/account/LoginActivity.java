package com.hezeyi.privatechat.activity.account;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.ImageView;

import com.hezeyi.privatechat.BuildConfig;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MainActivity;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.service.ChatService;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xhab.utils.activity.PrivacyPolicyActivity;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.SPUtils;
import com.xhab.utils.utils.SpanBuilder;
import com.xhab.utils.utils.ToastUtil;

import androidx.core.content.ContextCompat;
import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/8 20:36
 */
public class LoginActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_login;
    }

    private boolean isSelect;

    @Override
    public boolean isCanLock() {
        return false;
    }
    @Override
    public void canLiftClickFinish() {
    }

    @Override
    public void initData() {
        super.initData();
        requestPermissions();
    }

    @Override
    public void initView() {
        super.initView();
        NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.cancel(1);
        Intent chatService = new Intent(this, ChatService.class);
        chatService.putExtra("stop", "stop");
        LogUtils.e("initView*****: stop" );
        startService(chatService);
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
        SpannableStringBuilder build = SpanBuilder.content("勾选表示同意《用户协议》和《隐私政策》")
                .colorSpan(this, 6, 12, R.color.just_color_FF036EB8)
                .colorSpan(this, 14, 19, R.color.just_color_FF036EB8).build();
        ImageView imageView = findViewById(R.id.iv_select);
        imageView.setOnClickListener(v -> {
            isSelect = !isSelect;
            imageView.setImageDrawable(ContextCompat.getDrawable(this, isSelect ? R.mipmap.b3_icon1 : R.mipmap.b3_icon2));
        });
        setTextViewString(R.id.tv_protocol, build);
        click(R.id.tv_protocol, v -> {
            FunUtils.showPrivacy(this, integer -> {
                switch (integer) {
                    case 1:
                        Intent intent = new Intent(this, PrivacyPolicyActivity.class);
                        intent.putExtra("type", 0);
                        startActivity(intent);
                        break;
                    case 2:// showSnackBar("隐私政策");
                        intent = new Intent(this, PrivacyPolicyActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);

                        break;
                    case 4:
                        isSelect = true;
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.b3_icon1));
                        break;
                }
            });
        });

    }


    private void checkLogin() {
        String account = getTextViewString(R.id.et_account);
        String password = getTextViewString(R.id.et_password);
        if (FunUtils.checkIsNullable(account, "请输入账号!")) return;
        if (FunUtils.checkIsNullable(password, "请输入密码!")) return;
        if (!isSelect) {
            ToastUtil.showToast("请先勾选《用户协议》和《隐私政策》");
            return;
        }
        login(account, password);
    }

    private void login(String account, String password) {
        HttpManager.login(account, password, true, this, data -> {

            MyApplication.getInstance().userLogin(data, s -> {
                if (TextUtils.isEmpty(s)) {
                    Intent startIntent = new Intent(this, ChatService.class);
                    startIntent.putExtra("userId", data.getData().getUser_id());
                    startIntent.putExtra("account", account);
                    startIntent.putExtra("password", password);
                    startService(startIntent);
                    MyApplication.getInstance().setUserMsgBean(data.getData());
                    SPUtils.save(Const.Sp.account, account);
                    SPUtils.save(Const.Sp.password, password);
                    getUserList();
                } else {
                    showSnackBar(s);
                }
            });


        });
    }

    private void getUserList() {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, "1", this, userMsgBeans -> {
            MyApplication.getInstance().setFriendUserMsgBeans(userMsgBeans);
            HttpManager.groupSelectList(user_id, this, chatGroupBeans -> {
                MyApplication.getInstance().setChatGroupBeans(chatGroupBeans);
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
                Manifest.permission.RECORD_AUDIO
        )
                .subscribe(aBoolean -> {
                    if (aBoolean) {
//                        ensurePermissions();
                    } else {
                        showSnackBar("请到设置见面打开所需权限!");
                    }
                });
        addDisposable(subscribe);
    }
//    private void ensurePermissions() {
//        //检查是否已经授予权限 桌面启动activity的权限
//        if (!Settings.canDrawOverlays(this)) {
//            //若未授权则请求权限
//            FunUtils.affirm(this, "此应用需要显示在其他应用的上层的权限,以便于及时提醒", "去设置", aBoolean -> {
//                if (aBoolean) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                    intent.setData(Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent, 1);
//                }
//            });
//        } else {
//            getHangUpPermission(this);
//        }
//    }





}
