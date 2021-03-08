package com.hezeyi.privatechat.net;

/**
 * Created by dab on 2018/1/4 0004 13:35
 */

public class ResultData<T> {


    private T data;
    private String code;
    private String msg;
    private String debugMsg;

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
}
