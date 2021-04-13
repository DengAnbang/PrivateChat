package com.hezeyi.privatechat.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MainActivity;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.service.ChatService;
import com.xhab.utils.StackManager;
import com.xhab.utils.base.BaseUtilActivity;
import com.xhab.utils.utils.SPUtils;

/**
 * 启动页
 *
 * @author Chony
 * @time 2017/2/6 9:00
 */

public class SplashActivity extends BaseUtilActivity {


    @Override
    public void initView() {
        super.initView();
        Intent startIntent = new Intent(this, ChatService.class);
        startService(startIntent);
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
        }, 5000);
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
            HttpManager.groupSelectList(user_id, this, chatGroupBeans -> {
                MyApplication.getInstance().setChatGroupBeans(chatGroupBeans);
                MyApplication.getInstance().setLock(true);
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
