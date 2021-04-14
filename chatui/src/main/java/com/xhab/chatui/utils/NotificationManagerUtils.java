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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.DrawableRes;
import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;

/**
 * Created by dab on 2021/3/30 11:56
 */
public class NotificationManagerUtils {
    public static final String CHANNEL_ID = "id_108";
    public static final String CHANNEL_NAME = "新消息提醒";
    public static final String CHANNEL_ID_109 = "id_109";
    public static final String CHANNEL_NAME_109 = "普通提醒";


    public static void initHangUpPermission(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android 8.0及以上
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            notificationChannel.enableVibration(true);//震动不可用
            mNotificationManager.createNotificationChannel(notificationChannel);
            NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_ID_109,
                    CHANNEL_NAME_109, NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel2);
        }
    }


    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public static Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(true); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @WorkerThread
    public static void showNotification(Context context, Intent intent, boolean shouldRemind, String avatar, String name, String content, @DrawableRes int avatarDef) {

        avatar = avatar.replace("\\", "/");
//        NotificationManagerCompat notificationManager = (NotificationManagerCompat) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            //只在Android O之上需要渠道
//            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
//                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
//            //通知才能正常弹出
//            notificationChannel.enableVibration(true);//震动不可用
//            mNotificationManager.createNotificationChannel(notificationChannel);
//        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        mBuilder.setPriority(Notification.PRIORITY_MAX);//可以让通知显示在最上面
        mBuilder.setSmallIcon(R.mipmap.logo);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        if (shouldRemind) {
            mBuilder.setDefaults(Notification.DEFAULT_ALL);//使用默认的声音、振动、闪光
        }
        Bitmap bmIcon = BitmapFactory.decodeResource(context.getResources(), avatarDef);
        Bitmap largeIcon = bmIcon;
//        Bitmap bmAvatar = null;
        Bitmap bmAvatar = GetImageInputStream(avatar);
        if (bmAvatar != null) {
            //如果可以获取到网络头像则用网络头像
            largeIcon = bmAvatar;
        }
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setContentTitle(name);
        mBuilder.setChannelId(CHANNEL_ID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setFullScreenIntent(pendingIntent, true);
        //通知首次出现在通知栏，带上升动画效果的
        mBuilder.setTicker(content);
        //内容
        mBuilder.setContentText(content);
        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        //弹出通知栏
        mNotificationManager.notify(1, notification);
    }

    @WorkerThread
    public static void showNotification109(Context context, Intent intent, boolean shouldRemind,  String content) {
        String avatar = "";
//        NotificationManagerCompat notificationManager = (NotificationManagerCompat) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID_109);
        mBuilder.setPriority(Notification.PRIORITY_MAX);//可以让通知显示在最上面
        mBuilder.setSmallIcon(R.mipmap.logo);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        if (shouldRemind) {
            mBuilder.setDefaults(Notification.DEFAULT_ALL);//使用默认的声音、振动、闪光
        }
        Bitmap bmIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
        Bitmap largeIcon = bmIcon;
//        Bitmap bmAvatar = null;
        Bitmap bmAvatar = GetImageInputStream(avatar);
        if (bmAvatar != null) {
            //如果可以获取到网络头像则用网络头像
            largeIcon = bmAvatar;
        }
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setContentTitle("S.O.M");
        mBuilder.setChannelId(CHANNEL_ID_109);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setFullScreenIntent(pendingIntent, true);
        //通知首次出现在通知栏，带上升动画效果的
        mBuilder.setTicker(content);
        //内容
        mBuilder.setContentText(content);
        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        //弹出通知栏
        mNotificationManager.notify(0, notification);
    }


}
