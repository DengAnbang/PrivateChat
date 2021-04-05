package com.xhab.utils.utils;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;

import com.xhab.utils.Const;
import com.xhab.utils.inteface.OnDataCallBack;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

/**
 * Created by dab on 2021/3/9 16:13
 */
public class FunUtils {
    public static boolean checkIsNullable(String s, String hint) {
        if (TextUtils.isEmpty(s)) {
            ToastUtil.showSnackBar(hint);
            return true;
        }
        return false;
    }

    public static String getChineseName() {
        String str = null;
        String name = null;
        int highPos, lowPos;
        Random random = new Random();
        //区码，0xA0打头，从第16区开始，即0xB0=11*16=176,16~55一级汉字，56~87二级汉字
        highPos = (176 + Math.abs(random.nextInt(72)));
        random = new Random();
        //位码，0xA0打头，范围第1~94列
        lowPos = 161 + Math.abs(random.nextInt(94));

        byte[] bArr = new byte[2];
        bArr[0] = (new Integer(highPos)).byteValue();
        bArr[1] = (new Integer(lowPos)).byteValue();
        try {
            //区位码组合成汉字
            str = new String(bArr, "GB2312");
            int index = random.nextInt(Const.Surname.length - 1);
            //获得一个随机的姓氏
            name = Const.Surname[index] + str;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 判断服务是否运行
     */
    public static boolean isServiceRunning(Context context, final String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }

    public static void affirm(Context ctx, String msg, String rightMsg, final OnDataCallBack<Boolean> onDataClick) {
        final AlertDialog.Builder abDialog = new AlertDialog.Builder(ctx);
        abDialog.setCancelable(true);
        abDialog.setMessage(msg);
        abDialog.setNegativeButton("取消", (dialog, which) -> {
            dialog.dismiss();
            onDataClick.onCallBack(false);
        });
        abDialog.setPositiveButton(rightMsg, (dialog, which) -> {
            dialog.dismiss();
            onDataClick.onCallBack(true);
        });
        abDialog.show();
    }
}
