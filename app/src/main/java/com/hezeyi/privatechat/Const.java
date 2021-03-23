package com.hezeyi.privatechat;


import android.os.Environment;

/**
 * Created by dab on 2018/4/11 17:40
 */

public class Const {
    public static class Sp {
        public static final String BaseUrl = "baseUrl";
        public static final String UserPhone = "UserPhone";
        public static final String UserPwd = "UserPwd";
    }

    public static class FilePath {
        public final static String TEMP_DOCUMENTS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + MyApplication.getInstance().getPackageName() + "/";

        public static final String chatFileType = "chat/file";
        public static final String chatFileLocalPath = TEMP_DOCUMENTS_PATH;

    }

    public static class Api {
        public static final String API_HOST = "http://" + getUrl() + ":9090/";
        public static final String SOCKET_SERVER = getUrl();
        public static final int SOCKET_PORT = 9091;
    }


    private static String getUrl() {
        switch (BuildConfig.FLAVOR) {
            case "nb":
//                return "192.168.0.102";
                return "47.108.172.20";
//                return "192.168.31.213";
//                return "192.168.31.213:9090/public/file/upload";
            default:
                return "http://192.168.31.213";
        }
    }

    public static class RxType {
        public static final String CONNECTION = "10002";//连接
        public static final String TYPE_OTHER_LOGIN = "10001";//其他人登陆
        public static final String TypeHeartbeat = "2";//心跳
        public static final String TYPE_LOGIN = "10002"; //登录
        //消息
        public static final String TYPE_MSG_UPDATE = "20000";//更新消息状态
        public static final String TYPE_MSG_SEND = "20001";//发送消息
        public static final String TYPE_MSG_RECEIVE = "20002";//接收消息
        public static final String TYPE_MSG_ADD = "20004";//添加消息
    }

    public static boolean isNB() {
        return "nb".equals(BuildConfig.FLAVOR);
    }
}
