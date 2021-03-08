package com.hezeyi.privatechat;


/**
 * Created by dab on 2018/4/11 17:40
 */

public class Const {
    public static class Sp {
        public static final String BaseUrl = "baseUrl";
        public static final String UserPhone = "UserPhone";
        public static final String UserPwd = "UserPwd";
    }

    public static class Api {
        public static final String API_HOST = getUrl();
    }


    private static String getUrl() {
        switch (BuildConfig.FLAVOR) {
            case "nb":
                return "http://47.108.172.20:9090/";
            default:
                return "http://192.168.31.213:9090/";
        }
    }


    public static boolean isNB() {
        return "nb".equals(BuildConfig.FLAVOR);
    }
}
