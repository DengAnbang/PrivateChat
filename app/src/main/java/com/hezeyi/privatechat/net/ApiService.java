package com.hezeyi.privatechat.net;

import com.hezeyi.privatechat.bean.UserMsgBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Created by dab on 2018/4/11 17:57
 */

public interface ApiService {
    @Streaming
    @POST
    Observable<Response<ResponseBody>> downloadFile(@Url String fileUrl);

    @POST("/public/app/updates/check")
    Observable<ResultData<Object>> updatesCheck(
            @Query("versionCode") int versionCode,
            @Query("version_channel") String version_channel
    );

    @POST("/app/user/login")
    Observable<ResultData<UserMsgBean>> login(
            @Query("account") String account,
            @Query("pwd") String password
    );

    @POST("/app/user/register")
    Observable<ResultData<UserMsgBean>> register(
            @Query("account") String account,
            @Query("pwd") String pwd,
            @Query("user_name") String user_name,
            @Query("headPortrait") String headPortrait
    );

}
