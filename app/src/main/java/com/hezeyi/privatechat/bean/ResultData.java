package com.hezeyi.privatechat.bean;

import com.google.gson.Gson;

/**
 * Created by dab on 2018/1/4 0004 13:35
 */

public class ResultData<T> {


    private T data;
    private String code;
    private String msg;
    private String type;
    private String debugMsg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDebugMsg() {
        return debugMsg;
    }

    public void setDebugMsg(String debugMsg) {
        this.debugMsg = debugMsg;
    }

    public static <T> ResultData create(String code,String type, T data) {
        ResultData<T> tResultData = new ResultData<>();
        tResultData.setCode(code);
        tResultData.setData(data);
        tResultData.setType(type);
        return tResultData;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
