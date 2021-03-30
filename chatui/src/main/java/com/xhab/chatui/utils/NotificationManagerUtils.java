package com.xhab.chatui.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xhab.chatui.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;

/**
 * Created by dab on 2021/3/30 11:56
 */
public class NotificationManagerUtils {


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

//    @WorkerThread
//    public static void showNotification(Context context, String conversationType, int targetId, String avatar, String name, String content) {
//        showNotification(context, conversationType, targetId, avatar, name, content);
//    }

    @WorkerThread
    public static void showNotification(Context context, Intent intent, boolean shouldRemind, int targetId, String avatar, String name, String content) {
        String channel_id = "channel_id_1";

//        NotificationManagerCompat notificationManager = (NotificationManagerCompat) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(channel_id,
                    channel_id, NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channel_id);
        mBuilder.setPriority(Notification.PRIORITY_MAX);//可以让通知显示在最上面
//        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        if (shouldRemind) {
            mBuilder.setDefaults(Notification.DEFAULT_ALL);//使用默认的声音、振动、闪光
        }
        Bitmap bmIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_img_failed);
        Bitmap largeIcon = bmIcon;
//        Bitmap bmAvatar = null;
        Bitmap bmAvatar = GetImageInputStream(avatar);
        if (bmAvatar != null) {
            //如果可以获取到网络头像则用网络头像
            largeIcon = bmAvatar;
        }
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setContentTitle(name);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //通知首次出现在通知栏，带上升动画效果的
        mBuilder.setTicker(content);
        //内容
        mBuilder.setContentText(content);
        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();

        //弹出通知栏
        mNotificationManager.notify(targetId, notification);

    }


}
