package com.xhab.utils.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import com.xhab.utils.Const;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    public static void showPicture(Context context, String s) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(s);
        PictureConfig config = new PictureConfig.Builder()
                .setListData(strings)    //图片数据List<String> list
//                .setPosition(position)    //图片下标（从第position张图片开始浏览）
//                            .setDownloadPath("pictureviewer")	//图片下载文件夹地址
                .setIsShowNumber(false)//是否显示数字下标
                .needDownload(true)    //是否支持图片下载
//                        .setPlacrHolder(R.mipmap.addpoint)    //占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                .build();
        ImagePagerActivity.startActivity(context, config);
    }
}
