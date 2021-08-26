package com.abxh.utils.utils;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.abxh.utils.StackManager;


/**
 * Created by Chony on 2016/11/4.
 */

public class ToastUtil {
    private static Context sContext;

    private ToastUtil() {
    }

    public void init(Application application) {
        if (sContext == null) {
            sContext = application;
        }
    }

    private static boolean check() {
        if (sContext == null) {
            LogUtils.e("context ==null,检查com.xhab.utils.init()是否初始化!");
            return false;
        }
        return true;
    }



    public static void showToast(String message) {
        RxUtils.runOnUiThread(() -> {
            if (StackManager.currentActivity() != null) {
                Toast.makeText(StackManager.currentActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showSnackBar(final String message) {
        RxUtils.runOnUiThread(() -> {
            if (StackManager.currentActivity() != null) {
                View childAt = ((ViewGroup) StackManager.currentActivity().findViewById(android.R.id.content)).getChildAt(0);
                Snackbar make = Snackbar.make(childAt, message, Snackbar.LENGTH_SHORT);
                make.show();
            } else {
                LogUtils.e("StackManager.currentActivity() ==null,检查StackManager是否初始化!");
            }

        });
    }



}
