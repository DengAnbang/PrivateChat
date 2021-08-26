package com.abxh.utils.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by dab on 2021/3/18 21:20
 */
public class Base64Utils {
    /**
     * 字符Base64加密
     *
     * @param str
     * @return
     */
    public static String encodeToString(String str) {
//        return str;
        try {
            return Base64.encodeToString(str.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字符Base64解密
     *
     * @param str
     * @return
     */
    public static String decodeToString(String str) {
//        return str;
        try {
            return new String(Base64.decode(str.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
