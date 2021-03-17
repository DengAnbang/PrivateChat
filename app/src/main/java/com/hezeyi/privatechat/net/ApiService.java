package com.hezeyi.privatechat.net;

import com.hezeyi.privatechat.bean.ResultData;
import com.hezeyi.privatechat.bean.SecurityBean;
import com.hezeyi.privatechat.bean.UserMsgBean;

import java.util.List;

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

    @POST("/app/user/update")
    Observable<ResultData<UserMsgBean>> userUpdate(
            @Query("account") String account,
            @Query("pwd") String pwd,
            @Query("user_name") String user_name,
            @Query("headPortrait") String headPortrait
    );

    @POST("/app/user/security/update")
    Observable<ResultData<Object>> securityUpdate(
            @Query("account") String account,
            @Query("q1") String q1,
            @Query("a1") String a1,
            @Query("q2") String q2,
            @Query("a2") String a2
    );

    @POST("/app/user/select/security")
    Observable<ResultData<SecurityBean>> securitySelect(
            @Query("account") String account
    );

    @POST("/app/user/add/friend")
    Observable<ResultData<Object>> addFriend(
            @Query("user_id") String account,
            @Query("to_user_id") String to_user_id,
            @Query("friend_type") String friend_type
    );

    @POST("/app/user/select/by/id")
    Observable<ResultData<UserMsgBean>> userSelectById(
            @Query("user_id") String account
    );
    @POST("/app/user/select/friend")
    Observable<ResultData<List<UserMsgBean>>> userSelectFriend(
            @Query("user_id") String account
    );
}
