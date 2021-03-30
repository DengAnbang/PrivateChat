package com.hezeyi.privatechat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.text.TextUtils;

import com.hezeyi.privatechat.activity.LockActivity;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.juphoon.cloud.JCCallItem;
import com.tencent.bugly.Bugly;
import com.xhab.chatui.emoji.EmojiDao;
import com.xhab.chatui.voiceCalls.JuphoonUtils;
import com.xhab.utils.StackManager;
import com.xhab.utils.utils.ForegroundCallbacks;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.multidex.MultiDex;

/**
 * Created by Chony on 2017/2/5.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private boolean isLock = false;

    public void setLock(boolean lock) {
        isLock = lock;
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
        JuphoonUtils.get().initialize(this, "2f28a4e830fb84d0da705096");
        String curProcess = getProcessName(this, android.os.Process.myPid());
        if (!TextUtils.equals(curProcess, "com.hezeyi.privatechat." + BuildConfig.FLAVOR)) {
            return;
        }
        initAppStatusListener();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    // 进程名
    private String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    private void initAppStatusListener() {
        ForegroundCallbacks.init(this).addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                LogUtils.e("onBecameForeground*****: 进入前台");
                String string = SPUtils.getString(Const.Sp.SecurityCode, "");
                if (isLock && SPUtils.getBoolean(Const.Sp.isOpenSecurityCode, true) && !string.equals("")) {
                    Activity packageContext = StackManager.currentActivity();
                    Intent intent = new Intent(packageContext, LockActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
//                Toast.makeText(mContext, "安质宝进入前台", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBecameBackground() {
                LogUtils.e("onBecameForeground*****: 退至后台" + isLock);


            }
        });
    }

    private String anotherId;

    public String getAnotherId() {
        return anotherId;
    }

    public void setAnotherId(String anotherId) {
        this.anotherId = anotherId;
    }

    private UserMsgBean mUserMsgBean;

    public UserMsgBean getUserMsgBean() {
        return mUserMsgBean;
    }

    public void setUserMsgBean(UserMsgBean userMsgBean) {
        mUserMsgBean = userMsgBean;
    }

    private JCCallItem mJCCallItem;

    public JCCallItem getJCCallItem() {
        return mJCCallItem;
    }

    public void setJCCallItem(JCCallItem JCCallItem) {
        mJCCallItem = JCCallItem;
    }

    private Map<String, ChatGroupBean> mGroupBeanMap = new HashMap<>();
    private List<ChatGroupBean> mChatGroupBeans;

    public List<ChatGroupBean> getChatGroupBeans() {
        return mChatGroupBeans;
    }

    public void setChatGroupBeans(List<ChatGroupBean> chatGroupBeans) {
        mChatGroupBeans = chatGroupBeans;
        mGroupBeanMap.clear();
        for (int i = 0; i < mChatGroupBeans.size(); i++) {
            ChatGroupBean chatGroupBean = mChatGroupBeans.get(i);
            mGroupBeanMap.put(chatGroupBean.getGroup_id(), chatGroupBean);
        }
    }

    public ChatGroupBean getChatGroupBeanById(String group_id) {
        return mGroupBeanMap.get(group_id);
    }

    private List<UserMsgBean> mUserMsgBeans;

    public List<UserMsgBean> getUserMsgBeans() {
        return mUserMsgBeans;
    }

    private Map<String, UserMsgBean> mMsgBeanMap = new HashMap<>();

    public void setUserMsgBeans(List<UserMsgBean> userMsgBeans) {
        mUserMsgBeans = userMsgBeans;
        mMsgBeanMap.clear();
        for (int i = 0; i < userMsgBeans.size(); i++) {
            UserMsgBean userMsgBean = userMsgBeans.get(i);
            mMsgBeanMap.put(userMsgBean.getUser_id(), userMsgBean);
        }
    }

    public UserMsgBean getUserMsgBeanById(String userId) {
        if (userId.equals(mUserMsgBean.getUser_id())) return mUserMsgBean;
        return mMsgBeanMap.get(userId);
    }
}
