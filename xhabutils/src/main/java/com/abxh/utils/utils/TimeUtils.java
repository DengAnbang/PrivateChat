package com.abxh.utils.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dab on 2018/4/3 11:20
 */

public class TimeUtils {
    public static String toTimeByString(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(toMillisecond(timestamp)));
    }

    /**
     * 将秒转换成毫毛
     *
     * @param timestamp
     * @return
     */
    public static long toMillisecond(long timestamp) {
        if (timestamp < 20_0000_0000) {
            timestamp = timestamp * 1000;
        }
        return timestamp;
    }

    public static String toTimeByString(long timestamp) {
        return toTimeByString(timestamp, "yyyy-MM-dd HH:mm");
    }

    public static String toTimeByString(String timestamp) {
        return toTimeByString(timestamp, "yyyy-MM-dd HH:mm");
    }

    public static String toTimeByString(String timestamp, String pattern) {
        if (TextUtils.isEmpty(timestamp)) return "00:00";
        try {
            return toTimeByString(Long.parseLong(timestamp), pattern);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return timestamp;
        }
    }


    /**
     * 时间转时间戳
     *
     * @param time
     * @param pattern
     * @param def
     * @return
     */
    public static long toTimestampByString(String time, String pattern, long def) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        try {
            Date date = format.parse(time);
            long time1 = date.getTime();
            return time1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 根据秒 转换为时分秒
     *
     * @param second
     * @return
     */
    public static String getHMS(long second) {
        if (second<0||second>1_0000_0000) return "0秒";
        if (second < 60) {
            return second + "秒";
        } else if (second > 60 && second < 3600) {
            int m = (int) (second / 60);
            int s = (int) (second % 60);
            return m + "分" + s + "秒";
        } else {
            long h = second / 3600;
            int m = (int) ((second % 3600) / 60);
            int s = (int) ((second % 3600) % 60);
            return h + "小时" + m + "分" + s + "秒";
        }

    }
}
