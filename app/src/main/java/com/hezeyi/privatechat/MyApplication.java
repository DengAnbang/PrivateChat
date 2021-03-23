package com.hezeyi.privatechat;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;

import com.tencent.bugly.Bugly;
import com.xhab.chatui.emoji.EmojiDao;
import com.xhab.utils.StackManager;
import com.xhab.utils.utils.SPUtils;

import androidx.multidex.MultiDex;

/**
 * Created by Chony on 2017/2/5.
 */

public class MyApplication extends Application {

    private static MyApplication instance;


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
    }

    public static MyApplication getInstance() {
        return instance;
    }


}
