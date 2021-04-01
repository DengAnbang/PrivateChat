package com.hezeyi.privatechat;


import android.os.Environment;

/**
 * Created by dab on 2018/4/11 17:40
 */

public class Const {
    public static class Sp {
        public static final String account = "account";
        public static final String password = "password";
        public static final String SecurityCode = "SecurityCode";
        public static final String isOpenSecurityCode = "isOpenSecurityCode";
    }

    public static class FilePath {
        public final static String TEMP_DOCUMENTS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + MyApplication.getInstance().getPackageName() + "/";
        //本地目录
        public static final String chatFileLocalPath = TEMP_DOCUMENTS_PATH;
        public static final String databaseFileLocalPath = TEMP_DOCUMENTS_PATH;
        //网络目录
        public static final String chatFileType = "chat/file";
        public static final String userFileType = "user/file";

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
//                return "192.168.155.2";
//                return "192.168.31.213:9090/public/file/upload";
            default:
                return "192.168.31.213";
        }
    }

    public static class RxType {
        public static final String CONNECTION = "1";//连接
        public static final String TYPE_HEARTBEAT = "2";//心跳
        public static final String TYPE_OTHER_LOGIN = "10001";//其他人登陆
        public static final String TYPE_LOGIN = "10002"; //登录
        public static final String TYPE_LOGIN_OUT = "10003"; //退出登录
        //消息
        public static final String TYPE_MSG_UPDATE = "20000";//更新消息状态
        public static final String TYPE_MSG_SEND = "20001";//发送消息
        public static final String TYPE_MSG_RECEIVE = "20002";//接收消息
        public static final String TYPE_MSG_ADD = "20004";//添加消息
        public static final String TYPE_MSG_GROUP_SEND = "20003";//发送群消息
        //消息以外
        public static final String TYPE_SHOW_LIST = "30001";//更新消息列表

    }

    public static boolean isNB() {
        return "nb".equals(BuildConfig.FLAVOR);
    }
}
