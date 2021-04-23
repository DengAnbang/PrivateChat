package com.hezeyi.privatechat;


import com.xhab.chatui.utils.FileUtils;

/**
 * Created by dab on 2018/4/11 17:40
 * 证书指纹:
 * MD5: 1F:95:15:C7:E2:7E:1B:D4:D2:82:27:D1:00:23:57:BB
 * SHA1: AF:FC:B4:57:3A:00:07:26:DF:38:59:FE:94:2B:E8:46:39:15:98:DD
 * SHA256: 0B:AE:86:C8:BE:17:C8:E0:F3:32:21:E3:9E:A0:0C:74:B7:0F:BA:09:EA:93:72:C8:3F:85:45:32:7D:50:AB:12
 * 签名算法名称: SHA256withRSA
 * 版本: 3
 */

public class Const {
    public static class Sp {
        public static final String account = "account";
        public static final String password = "password";
        public static final String SecurityCode = "Code";
        public static final String isOpenSecurityCode = "isOpenSecurityCode";
        public static final String isNewMsgCode = "isNewMsgCode";
        public static final String isNewMsgDesCode = "isNewMsgDesCode";
    }

    public static class FilePath {
        //        public final static String TEMP_DOCUMENTS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + MyApplication.getInstance().getPackageName() + "/";
        public final static String TEMP_DOCUMENTS_PATH = FileUtils.getSDPath(MyApplication.getInstance());
        //本地目录
        public static final String chatFileLocalPath = TEMP_DOCUMENTS_PATH;
        public static final String databaseFileLocalPath = TEMP_DOCUMENTS_PATH;
        public static final String FileLocalPath = TEMP_DOCUMENTS_PATH+"/pay/";
        //网络目录
        public static final String chatFileType = "chat/file";
        public static final String userFileType = "user/file";

    }

    public static class Notification {
        public static final String CHANNEL_MSG_ID = "id_108";// com.xhab.utils.activity.WhitelistActivity 写死了的
        public static final String CHANNEL_MSG_NAME = "新消息提醒";
        public static final String CHANNEL_ID_109 = "id_109";
        public static final String CHANNEL_NAME_109 = "普通提醒";
//        public static final String CHANNEL_VOICE_ID = "id_110";
//        public static final String CHANNEL_VOICE_NAME = "语音通话";
    }

    public static class Api {
        public static final String API_HOST = "http://" + getUrl() + ":9090/";
        public static final String SOCKET_SERVER = getUrl();
        public static final int SOCKET_PORT = 9091;
    }

    private static String getUrl() {
        switch (BuildConfig.FLAVOR) {
            case "nb":
//                return "192.168.0.107";
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
        //其他的推送
        public static final String TYPE_FRIEND_ADD = "21001";//有好友申请的推送
        public static final String TYPE_FRIEND_CHANGE = "21002";//有好友变化的推送(包括在线离线)

        //推送消息以外
        public static final String TYPE_SHOW_LIST = "30001";//更新消息列表
        public static final String TYPE_SHOW_FRIEND_RED_PROMPT = "type_show_friend_red_prompt";//更新好友申请的点点消息列表
        public static final String TYPE_FRIEND_CHANGE_SHOW = "type_friend_change_show";//页面好友改变的变化

    }

    public static boolean isNB() {
        return "nb".equals(BuildConfig.FLAVOR);
    }
}
