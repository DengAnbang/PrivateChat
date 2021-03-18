package com.hezeyi.privatechat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.text.TextUtils;

import com.tencent.bugly.Bugly;
import com.xhab.chatui.emoji.EmojiDao;
import com.xhab.utils.StackManager;
import com.xhab.utils.utils.SPUtils;

import java.util.LinkedList;

import androidx.multidex.MultiDex;

/**
 * Created by Chony on 2017/2/5.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private LinkedList<Activity> mActivities = new LinkedList<>();//添加activity管理
    private String appName = "BIM管理";

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        if (!TextUtils.isEmpty(appName)) {
            this.appName = appName;
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        StackManager.initStackManager(this);
        EmojiDao.init(this);
        //初始化Bugly
//        CrashReport.initCrashReport(getApplicationContext(), "ec8f9b812a", true);
        Bugly.init(getApplicationContext(), "ebd08610e4", true);
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String channel = appInfo.metaData.getString("CHANNEL");
            Bugly.setAppChannel(getApplicationContext(), channel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        SPUtils.init(this, "privatechat");

//        ResponseHelper.addInterceptResponse("login", stateBean -> {
//            if (stateBean == null || stateBean.getCode().equals("1")) return false;
//            if (stateBean.getMsg().contains("登录失效") || stateBean.getMsg().contains("请登录")) {
//                StackManager.finishExcludeActivity(LoginActivity.class);
////                Activity activity = StackManager.currentActivity();
////                Intent intent = new Intent(activity, LoginActivity.class);
//////                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
////                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////                startActivity(intent);
//                ToastUtil.showShort("登陆过期,请重新登陆!");
//                return true;
//            } else {
//                return false;
//            }
//        });
    }

    public static MyApplication getContext() {
        return instance;
    }


    /**
     * 整个应用退出
     */
    public void exit() {
        StackManager.finishExcludeActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.gc();
        System.exit(0);
//        Activity activity;
//        while (mActivities.size() != 0) {
//            activity = mActivities.poll();
//            if (!activity.isFinishing()) {
//                activity.finish();
//            }
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.gc();
//            System.exit(0);
//        }
//        System.exit(0);

    }
}
