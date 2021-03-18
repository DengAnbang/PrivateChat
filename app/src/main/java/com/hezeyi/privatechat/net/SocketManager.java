package com.hezeyi.privatechat.net;


import com.hezeyi.privatechat.Const;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dab on 2021/3/18 16:52
 */
public class SocketManager {
    public static void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String code = jsonObject.get("code").toString();
            if (!code.equals("0")) {
                LogUtils.e("parseJson*****:code!=0 " + jsonObject);
                return;
            }
            String type = jsonObject.get("type").toString();
            String data = jsonObject.get("data").toString();
            dispense(type, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void dispense(String type, String content) {
        switch (type) {
            case Const.RxType.CONNECTION:
                RxBus.get().post(Const.RxType.CONNECTION, content);
                break;
            case Const.RxType.TYPE_OTHER_LOGIN:
                RxBus.get().post(Const.RxType.TYPE_OTHER_LOGIN, content);
                break;
            case Const.RxType.TYPE_MSG_TEXT:
                RxBus.get().post(Const.RxType.TYPE_MSG_TEXT, content);
                break;
            case Const.RxType.TYPE_MSG_STATUS_SEND:
                RxBus.get().post(Const.RxType.TYPE_MSG_STATUS_SEND, content);
                break;
        }
    }
}
