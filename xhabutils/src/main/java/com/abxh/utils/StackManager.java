package com.abxh.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by dab on 2017/11/29 0029 09:50
 * 栈管理器
 */

public class StackManager {
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public static void pushActivity(Activity activity) {
        mActivitys.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public static void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return null;
        }
        return mActivitys.get(mActivitys.size() - 1);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (Objects.equals(activity.getClass(), cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束指定类名以外的Activity
     */
    public static void finishExcludeActivity(Class<?> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        for (Activity next : mActivitys) {
            if (!Objects.equals(next.getClass(), cls)) {
                next.finish();
//                iterator.remove();
            }
        }
    }

    public static void finishExcludeActivity(Class<?>... cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        for (Activity next : mActivitys) {
            boolean has = false;
            for (Class<?> aClass : cls) {
                if (Objects.equals(next.getClass(), aClass)) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                next.finish();
            }
        }

    }


    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (Objects.equals(activity.getClass(), cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    public static void initStackManager(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                /**
                 *  监听到 Activity创建事件 将该 Activity 加入list
                 */
                pushActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (null == mActivitys || mActivitys.isEmpty()) {
                    return;
                }
                if (mActivitys.contains(activity)) {
                    /**
                     *  监听到 Activity销毁事件 将该Activity 从list中移除
                     */
                    popActivity(activity);
                }
            }
        });

    }
}
