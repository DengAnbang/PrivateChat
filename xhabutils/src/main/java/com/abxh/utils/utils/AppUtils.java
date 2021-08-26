package com.abxh.utils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by dab on 2021/4/25 08:58
 */

public class AppUtils {
    /**
     * 判断是否是8.0,8.0需要处理未知应用来源权限问题,否则直接安装
     */
    public static void checkIsAndroidO(Activity activity, String apkPath,int requestCode) {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = activity.getPackageManager().canRequestPackageInstalls();
            if (b) {
                AppUtils.installApk(activity, apkPath);
                //安装应用的逻辑(写自己的就可以)
            } else {
                Uri packageUri = Uri.parse("package:" + activity.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity. startActivityForResult(intent, requestCode);
                //设置安装未知应用来源的权限
//                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
//                activity.startActivityForResult(intent, requestCode);
            }
        } else {
            AppUtils.installApk(activity, apkPath);
        }
    }

    /**
     * 安装APK
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, context.getOpPackageName(), file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
