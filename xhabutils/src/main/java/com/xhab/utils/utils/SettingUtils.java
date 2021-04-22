package com.xhab.utils.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import com.xhab.utils.R;
import com.xhab.utils.bean.WhitelistGuideBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

/**
 * Created by dab on 2021/4/22 09:29
 */
public class SettingUtils {
    public static void enterWhiteListSetting(Context context) {
        try {
            context.startActivity(getSettingIntent());
        } catch (Exception e) {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
        }
    }

    private static boolean sadas() {
        String brand = android.os.Build.BRAND;

        return false;

    }
    private static Intent getSettingIntent() {

        ComponentName componentName = null;

        String brand = android.os.Build.BRAND;
        System.out.println("brand===========================" + brand);
        switch (brand.toLowerCase()) {
            case "samsung":
                componentName = new ComponentName("com.samsung.android.sm",
                        "com.samsung.android.sm.app.dashboard.SmartManagerDashBoardActivity");
                break;
            case "huawei":
            case "honor":
                componentName = new ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
                break;
            case "xiaomi":
                componentName = new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity");
                break;
            case "vivo":
                componentName = new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity");
                break;
            case "oppo":
                componentName = new ComponentName("com.coloros.oppoguardelf",
                        "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity");
                break;
            case "360":
                componentName = new ComponentName("com.yulong.android.coolsafe",
                        "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity");
                break;
            case "meizu":
                componentName = new ComponentName("com.meizu.safe",
                        "com.meizu.safe.permission.SmartBGActivity");
                break;
            case "oneplus":
                componentName = new ComponentName("com.oneplus.security",
                        "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity");
                break;
            default:
                break;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (componentName != null) {
            intent.setComponent(componentName);
        } else {
            intent.setAction(Settings.ACTION_SETTINGS);
        }
        return intent;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return isIgnoring;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Activity context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, 0x89);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<WhitelistGuideBean> getWhitelistGuideBeans() {
        List<WhitelistGuideBean> whitelistGuideBeans = new ArrayList<>();
        String brand = android.os.Build.BRAND;
        switch (brand.toLowerCase()) {
            case "samsung":

                break;
            case "huawei":
            case "honor":
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.huawei1, "进入设置后选择电池"));
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.huawei2, "选择S.O.M"));
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.huawei3, "选择启动管理"));
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.huawei4, "打开自动启动和后台运行"));
                break;
            case "xiaomi":
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.xiaomi1, "应用设置"));
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.xiaomi2, "选择应用管理"));
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.xiaomi3, "选择S.O.M"));
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.xiaomi4, "打开自动启动"));
                whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.xiaomi5, "打开能被唤醒"));
                break;
            case "vivo":

                break;
            case "oppo":

                break;
            case "360":

                break;
            case "meizu":

                break;
            case "oneplus":

                break;
            default:
                break;
        }
        return whitelistGuideBeans;
    }
}
