package com.hezeyi.privatechat.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MainActivity;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.service.ChatService;
import com.abxh.utils.StackManager;
import com.abxh.utils.utils.LogUtils;
import com.abxh.utils.utils.SPUtils;

/**
 * 启动页
 *
 * @author Chony
 * @time 2017/2/6 9:00
 */

public class SplashActivity extends BaseActivity {
    @Override
    public boolean isCanLock() {
        return false;
    }

    @Override
    public void initView() {
        super.initView();
//        SPUtils.save(Const.Sp.again_password, System.currentTimeMillis());
        long aLong = SPUtils.getLong(Const.Sp.again_password, System.currentTimeMillis());
        long l = System.currentTimeMillis() - aLong;
        if (l > 1000 * 60 * 60 * 24 * 3) {
//        if (l > 1000 * 60 ) {
            //上次登录以后,超过了3天,清空密码,重新登录
            SPUtils.save(Const.Sp.password, "");
        }
        LogUtils.e("initView*****: " + l);
        String account = SPUtils.getString(Const.Sp.account, "");
        String password = SPUtils.getString(Const.Sp.password, "");
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            //停留1.5S进入主页
            getWindow().getDecorView().postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, GuiderActivity.class));
                finish();
            }, 1500);
            return;
        }
        login(account, password);
        //停留1.5S进入主页
        getWindow().getDecorView().postDelayed(() -> {
            if (isFinishing()) return;
            startActivity(new Intent(SplashActivity.this, GuiderActivity.class));
            finish();
        }, 10000);
        return;
    }

    private void login(String account, String password) {
        HttpManager.login(account, password, false, this, data -> {
            MyApplication.getInstance().userLogin(data, s -> {
                if (TextUtils.isEmpty(s)) {
                    Intent startIntent = new Intent(this, ChatService.class);
                    startIntent.putExtra("userId", data.getData().getUser_id());
                    startIntent.putExtra("account", account);
                    startIntent.putExtra("password", password);
                    startService(startIntent);
                    MyApplication.getInstance().setUserMsgBean(data.getData());
                    getUserList();
                } else {

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    StackManager.finishExcludeActivity(LoginActivity.class);
                }
            });
        });
    }

    private void getUserList() {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, "1", this, userMsgBeans -> {
            MyApplication.getInstance().setFriendUserMsgBeans(userMsgBeans);
            HttpManager.groupSelectList(user_id, false,this, chatGroupBeans -> {
                MyApplication.getInstance().setChatGroupBeans(chatGroupBeans);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    public int getContentViewRes() {
        return 0;
    }
}
