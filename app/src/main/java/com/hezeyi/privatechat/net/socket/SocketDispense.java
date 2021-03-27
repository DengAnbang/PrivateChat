package com.hezeyi.privatechat.net.socket;


import com.xhab.utils.utils.AESEncryptUtil;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dab on 2021/3/18 16:52
 */
public class SocketDispense {
    public static void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
//            String code = jsonObject.get("code").toString();
//            if (!code.equals("0")) {
//                LogUtils.e("parseJson*****:code!=0 " + jsonObject);
//                return;
//            }
            String type = jsonObject.get("type").toString();
            String data = jsonObject.get("data").toString();
            boolean has = jsonObject.has("senderId");
            Object senderId = null;
            if (has) {
                senderId = jsonObject.get("senderId");
            }

            dispense(type, data, senderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void dispense(String type, String content, Object senderId) {
        if (type.equals("2") || type.equals("10002")) {
            return;
        }
        if (senderId != null) {
            String decrypt = AESEncryptUtil.decrypt(senderId.toString(), content);
            LogUtils.e("dispense*****: " + decrypt);
            RxBus.get().post(type, decrypt);
        } else {
            RxBus.get().post(type, content);
        }


//        switch (type) {
//            case Const.RxType.CONNECTION:
//                RxBus.get().post(type, content);
//                break;
//            case Const.RxType.TYPE_OTHER_LOGIN:
//                RxBus.get().post(type, content);
//                break;
//            case Const.RxType.TYPE_MSG_SEND:
//                RxBus.get().post(type, content);
//                break;
//            case Const.RxType.TYPE_MSG_UPDATE:
//                RxBus.get().post(type, content);
//                break;
//            case Const.RxType.TYPE_MSG_RECEIVE:
//                RxBus.get().post(type, content);
//                break;
//            case Const.RxType.TYPE_HEARTBEAT:
//
//                break;
//        }
    }
}
