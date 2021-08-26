package com.abxh.utils.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abxh.utils.Const;
import com.abxh.utils.R;
import com.abxh.utils.dialog.PrivacyDialog;
import com.abxh.utils.inteface.OnDataCallBack;

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

    /**
     * 显示用户协议和隐私政策
     * 1:点击用户协议
     * 2.点击隐私协议
     * 3.点击退出
     * 4.点击同意
     */
    public static void showPrivacy(Activity activity, @NonNull OnDataCallBack<Integer> callBack) {

        final PrivacyDialog dialog = new PrivacyDialog(activity);
        TextView tv_privacy_tips = dialog.findViewById(R.id.tv_privacy_tips);
        TextView btn_exit = dialog.findViewById(R.id.btn_exit);
        TextView btn_enter = dialog.findViewById(R.id.btn_enter);


        String string = activity.getResources().getString(R.string.privacy_tips);
        String key1 = activity.getResources().getString(R.string.privacy_tips_key1);
        String key2 = activity.getResources().getString(R.string.privacy_tips_key2);
        int index1 = string.indexOf(key1);
        int index2 = string.indexOf(key2);

        //需要显示的字串
        SpannableString spannedString = new SpannableString(string);
        //设置点击字体颜色
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(activity.getResources().getColor(R.color.just_color_FF036EB8));
        spannedString.setSpan(colorSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(activity.getResources().getColor(R.color.just_color_FF036EB8));
        spannedString.setSpan(colorSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击字体大小
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(18, true);
        spannedString.setSpan(sizeSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        AbsoluteSizeSpan sizeSpan2 = new AbsoluteSizeSpan(18, true);
        spannedString.setSpan(sizeSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击事件
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                callBack.onCallBack(1);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                callBack.onCallBack(2);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置点击后的颜色为透明，否则会一直出现高亮
        tv_privacy_tips.setHighlightColor(Color.TRANSPARENT);
        //开始响应点击事件
        tv_privacy_tips.setMovementMethod(LinkMovementMethod.getInstance());

        tv_privacy_tips.setText(spannedString);

        //设置弹框宽度占屏幕的80%
        WindowManager m = activity.getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (defaultDisplay.getWidth() * 0.80);
        dialog.getWindow().setAttributes(params);
        dialog.show();
        btn_exit.setOnClickListener(v -> {
            dialog.dismiss();
            callBack.onCallBack(3);
        });

        btn_enter.setOnClickListener(v -> {
            dialog.dismiss();
            callBack.onCallBack(4);
        });

    }
}
