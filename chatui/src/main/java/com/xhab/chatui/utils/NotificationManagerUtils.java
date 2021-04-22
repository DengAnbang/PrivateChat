package com.xhab.chatui.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.xhab.chatui.R;
import com.xhab.utils.utils.NetUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;

/**
 * Created by dab on 2021/3/30 11:56
 */
public class NotificationManagerUtils {


    public static void initHangUpPermission(Context context, String id, String name) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android 8.0及以上
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            //通知才能正常弹出
            notificationChannel.enableVibration(true);//震动不可用
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }


    @WorkerThread
    public static void showNotification(Context context, Intent intent, String avatar, String name, String content, @DrawableRes int avatarDef, String channel_id) {
        avatar = avatar.replace("\\", "/");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = create(context, intent, true, avatar, name, content, avatarDef, channel_id);
        //弹出通知栏
        mNotificationManager.notify(1, notification);
    }

    @WorkerThread
    public static void showNotification109(Context context, Intent intent, String content, String channel_id) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = create(context, intent, true, "", "S.O.M", content, R.mipmap.logo, channel_id);
        //弹出通知栏
        mNotificationManager.notify(0, notification);
    }

    @WorkerThread
    public static Notification getNotification(Context context, Intent intent, boolean autoCancel, String content, String channel_id) {
        return create(context, intent, autoCancel, "", "S.O.M", content, R.mipmap.logo, channel_id);
    }

    private static Notification create(Context context, Intent intent, boolean autoCancel, String avatar, String name, String content, @DrawableRes int avatarDef, String channel_id) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channel_id);
        mBuilder.setPriority(Notification.PRIORITY_MAX);//可以让通知显示在最上面
        mBuilder.setSmallIcon(R.mipmap.logo);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(autoCancel);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);//使用默认的声音、振动、闪光
        Bitmap bmAvatar = NetUtils.GetImageInputStream(avatar);
        if (bmAvatar != null) {
            //如果可以获取到网络头像则用网络头像
            bmAvatar = BitmapFactory.decodeResource(context.getResources(), avatarDef);
        }
        mBuilder.setLargeIcon(bmAvatar);
        mBuilder.setContentTitle(name);
        mBuilder.setChannelId(channel_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setFullScreenIntent(pendingIntent, true);
        //通知首次出现在通知栏，带上升动画效果的
        mBuilder.setTicker(content);
        //内容
        mBuilder.setContentText(content);
        mBuilder.setContentIntent(pendingIntent);
        return mBuilder.build();
    }

}
