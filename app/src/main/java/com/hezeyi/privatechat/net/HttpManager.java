package com.hezeyi.privatechat.net;


import com.hezeyi.privatechat.bean.SecurityBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.inteface.OnDataClick;
import com.xhab.utils.net.RequestHelper;

/**
 * Created by dab on 2018/4/11 17:52
 */

public class HttpManager {

    public static void updatesCheck(int version_code, String version_channel, final RequestHelper requestHelper, final OnDataClick<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .updatesCheck(version_code, version_channel), requestHelper, true, dataClick);
    }

    public static void login(String account, String password, final RequestHelper requestHelper, final OnDataClick<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .login(account, password), requestHelper, true, dataClick);
    }

    public static void register(String account, String password, String name, String headPortrait, final RequestHelper requestHelper, final OnDataClick<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .register(account, password, name, headPortrait), requestHelper, true, dataClick);
    }

    public static void userUpdate(String account, String password, String name, String headPortrait, final RequestHelper requestHelper, final OnDataClick<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userUpdate(account, password, name, headPortrait), requestHelper, true, dataClick);
    }

    public static void securityUpdate(String account, String q1, String a1, String q2, String a2, final RequestHelper requestHelper, final OnDataClick<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .securityUpdate(account, q1, a1, q2, a2), requestHelper, true, dataClick);
    }


    public static void securitySelect(String account, final RequestHelper requestHelper, final OnDataClick<SecurityBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .securitySelect(account), requestHelper, true, dataClick);
    }


}
