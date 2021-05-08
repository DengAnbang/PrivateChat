package com.hezeyi.privatechat.net;

import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.RechargeRecordBean;
import com.hezeyi.privatechat.bean.ResultData;
import com.hezeyi.privatechat.bean.SecurityBean;
import com.hezeyi.privatechat.bean.SelectPriceBean;
import com.hezeyi.privatechat.bean.UpdatesBean;
import com.hezeyi.privatechat.bean.UserMsgBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Observable<ResultData<UpdatesBean>> updatesCheck();

    //文件上传
    @Multipart
    @POST("/public/file/upload")
    Observable<ResultData<String>> fileUpload(
            @Query("fileType") String fileType
            , @Part MultipartBody.Part body);

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
            @Query("vip_time") String vip_time,
            @Query("headPortrait") String headPortrait
    );

    @POST("/app/user/recharge")
    Observable<ResultData<Object>> userRecharge(
            @Query("user_id") String user_id,
            @Query("execution_user_id") String execution_user_id,
            @Query("recharge_type") String recharge_type,
            @Query("pay_id") String pay_id
    );

    @POST("/app/user/security/update")
    Observable<ResultData<Object>> securityUpdate(
            @Query("user_id") String user_id,
            @Query("q1") String q1,
            @Query("a1") String a1,
            @Query("q2") String q2,
            @Query("a2") String a2
    );

    @POST("/app/user/select/security")
    Observable<ResultData<SecurityBean>> securitySelect(
            @Query("user_id") String user_id
    );

    @POST("/app/user/friend/add")
    Observable<ResultData<String>> friendAdd(
            @Query("user_id") String account,
            @Query("to_user_id") String to_user_id,
            @Query("friend_type") String friend_type,
            @Query("chat_pwd") String chat_pwd
    );

    @POST("/app/user/comment/set")
    Observable<ResultData<Object>> friendCommentSet(
            @Query("user_id") String account,
            @Query("to_user_id") String to_user_id,
            @Query("nickname") String nickname
    );

    @POST("/app/user/friend/delete")
    Observable<ResultData<Object>> friendDelete(
            @Query("user_id") String account,
            @Query("to_user_id") String to_user_id
    );

    @POST("/app/user/select/by/id")
    Observable<ResultData<UserMsgBean>> userSelectById(
            @Query("user_id") String user_id,
            @Query("my_user_id") String my_user_id
    );

    @POST("/app/user/select/by/account")
    Observable<ResultData<UserMsgBean>> userSelectByAccount(
            @Query("account") String account
    );

    @POST("/app/user/select/by/fuzzy/search")
    Observable<ResultData<List<UserMsgBean>>> userSelectByFuzzySearch(
            @Query("word") String word
    );

    @POST("/app/user/select/by/fuzzy/search/all")
    Observable<ResultData<List<UserMsgBean>>> userSelectByFuzzySearchAll(
            @Query("word") String word
    );

    @POST("/app/user/select/friend")
    Observable<ResultData<List<UserMsgBean>>> userSelectFriend(
            @Query("user_id") String account,
            @Query("friend_type") String friend_type
    );

    @POST("/app/group/register")
    Observable<ResultData<ChatGroupBean>> groupRegister(
            @Query("group_name") String group_name,
            @Query("user_id") String user_id
    );

    @POST("/app/group/add/users")
    Observable<ResultData<Object>> groupAddUser(
            @Query("user_ids") String user_ids,
            @Query("group_id") String group_id
    );

    @POST("/app/group/remove/user")
    Observable<ResultData<Object>> groupRemoveUser(
            @Query("user_id") String user_id,
            @Query("group_id") String group_id
    );

    @POST("/app/group/select/list")
    Observable<ResultData<List<ChatGroupBean>>> groupSelectList(
            @Query("user_id") String user_id
    );

    @POST("/app/group/select/user")
    Observable<ResultData<List<ChatGroupBean>>> groupSelectUser(
            @Query("group_id") String group_id
    );

    @POST("/app/group/select/user/msg")
    Observable<ResultData<List<UserMsgBean>>> groupSelectUserMsg(
            @Query("group_id") String group_id
    );

    @POST("/app/recharge/add")
    Observable<ResultData<Object>> rechargeAdd(
            @Query("user_id") String user_id,
            @Query("execution_user_id") String execution_user_id,
            @Query("money") String money,
            @Query("day") String day,
            @Query("recharge_type") String recharge_type
    );

    @POST("/app/select/by/type")
    Observable<ResultData<List<RechargeRecordBean>>> rechargeSelectByType(
            @Query("recharge_type") String recharge_type
    );

    @POST("/app/select/by/user/id")
    Observable<ResultData<List<RechargeRecordBean>>> rechargeSelectByUserId(
            @Query("user_id") String user_id
    );

    @POST("/app/select/by/execution/user/id")
    Observable<ResultData<List<RechargeRecordBean>>> rechargeSelectByExecutionUserId(
            @Query("user_id") String user_id
    );

    @POST("/app/select/by/time")
    Observable<ResultData<List<RechargeRecordBean>>> rechargeSelectByTime(
            @Query("user_id") String user_id
    );

    @POST("/app/select/all")
    Observable<ResultData<List<RechargeRecordBean>>> rechargeSelectAll(
    );

    @POST("/app/price/add")
    Observable<ResultData<Object>> priceAdd(
            @Query("money") String money,
            @Query("day") String day,
            @Query("giving_day") String giving_day,
            @Query("pay_image") String pay_image
    );


    @POST("/app/price/delete")
    Observable<ResultData<Object>> priceDelete(
            @Query("id") String id
    );

    @POST("/app/price/update")
    Observable<ResultData<Object>> priceUpdate(
            @Query("money") String money,
            @Query("day") String day,
            @Query("giving_day") String giving_day,
            @Query("pay_image") String pay_image,
            @Query("id") String id
    );

    @POST("/app/price/select/by/id")
    Observable<ResultData<SelectPriceBean>> priceSelectById(
            @Query("id") String id
    );

    @POST("/app/price/select/all")
    Observable<ResultData<List<SelectPriceBean>>> priceSelectAll(
            @Query("user_id") String user_id
    );


}
