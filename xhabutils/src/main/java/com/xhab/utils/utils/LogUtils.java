package com.xhab.utils.utils;

import android.util.Log;

import com.xhab.utils.BuildConfig;

/**
 * Created by Mei on 2017/4/8.
 */

public class LogUtils {
    private static String TAG = "日志";
    private static boolean showLog = BuildConfig.DEBUG;

    public static void getTag(String stringInfo) {
        if (!showLog) return;
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String className = stackTrace[1].getFileName();
        String methodName = stackTrace[1].getMethodName();
        int lineNumber = stackTrace[1].getLineNumber();
        String tag = "(" + className + ":" + lineNumber + ")";
        Log.e(tag, methodName + ":" + stringInfo);
    }

    public static void e(Object stringInfo) {
        if (!showLog) return;
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String className = stackTrace[1].getFileName();
        String methodName = stackTrace[1].getMethodName();
        int lineNumber = stackTrace[1].getLineNumber();
        String tag = "(" + className + ":" + lineNumber + ")";
        if (stringInfo == null) {
            Log.e(tag, methodName + ":" + null);
        } else {
            Log.e(tag, methodName + ":" + stringInfo.toString());
        }
    }

}
