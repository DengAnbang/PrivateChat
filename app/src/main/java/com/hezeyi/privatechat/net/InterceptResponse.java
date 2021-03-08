package com.hezeyi.privatechat.net;

/**
 * Created by dab on 2018/4/11 18:04
 */

public interface InterceptResponse<T> {
    boolean isIntercept(T t);
}
