package com.hezeyi.privatechat.net.socket;


import com.google.gson.Gson;
import com.hezeyi.privatechat.Const;
import com.abxh.chatui.bean.chat.ChatMessage;
import com.abxh.utils.utils.AESEncryptUtil;
import com.abxh.utils.utils.LogUtils;
import com.abxh.utils.utils.RxBus;

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
            LogUtils.e("parseJson*****: "+json );
            dispense(type, data, senderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void dispense(String type, String content, Object senderId) {

        if (type.equals("2") || type.equals("10002")) {
            if (type.equals("10002")){
//                LogUtils.e("initSocket*****: "+content );
            }
            return;
        }
        if (type.equals("20000")) {
            ChatMessage message = new Gson().fromJson(content, ChatMessage.class);
            RxBus.get().post(Const.RxType.TYPE_MSG_UPDATE, message);
            return;
        }
        if (senderId != null) {
            String decrypt = AESEncryptUtil.decrypt(senderId.toString(), content);
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
