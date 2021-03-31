package com.hezeyi.privatechat.net;


import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.SecurityBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.xhab.utils.inteface.OnDataCallBack;
import com.xhab.utils.net.RequestHelper;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by dab on 2018/4/11 17:52
 */

public class HttpManager {
    public static void fileUpload(String fileType, String filePath, final RequestHelper requestHelper, final OnDataCallBack<String> dataClick) {

        File file = new File(filePath);
        if (file.exists()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                    .fileUpload(fileType, part), requestHelper, false, dataClick);
        }

    }

    public static void fileUpload(String fileType, String filePath, final OnDataCallBack<String> dataClick) {
        File file = new File(filePath);
        if (file.exists()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                    .fileUpload(fileType, part), new RequestHelperAgency(MyApplication.getInstance()), false, dataClick);
        }

    }


    public static void updatesCheck(int version_code, String version_channel, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .updatesCheck(version_code, version_channel), requestHelper, true, dataClick);
    }

    public static void login(String account, String password, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .login(account, password), requestHelper, true, dataClick);
    }

    public static void register(String account, String password, String name, String headPortrait, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .register(account, password, name, headPortrait), requestHelper, true, dataClick);
    }

    public static void userUpdate(String account, String password, String name, String headPortrait, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userUpdate(account, password, name, headPortrait), requestHelper, true, dataClick);
    }

    public static void securityUpdate(String account, String q1, String a1, String q2, String a2, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .securityUpdate(account, q1, a1, q2, a2), requestHelper, true, dataClick);
    }


    public static void securitySelect(String account, final RequestHelper requestHelper, final OnDataCallBack<SecurityBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .securitySelect(account), requestHelper, true, dataClick);
    }

    public static void addFriend(String user_id, String to_user_id, String friend_type, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .addFriend(user_id, to_user_id, friend_type), requestHelper, true, dataClick);
    }

    public static void userSelectById(String user_id, boolean showLoadDialog, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userSelectById(user_id), requestHelper, showLoadDialog, dataClick);
    }

    public static void userSelectFriend(String user_id, final RequestHelper requestHelper, final OnDataCallBack<List<UserMsgBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userSelectFriend(user_id), requestHelper, true, dataClick);
    }

    public static void groupRegister(String user_id, final RequestHelper requestHelper, final OnDataCallBack<ChatGroupBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupRegister(user_id), requestHelper, true, dataClick);
    }

    public static void groupAddUser(String user_id, String group_id, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupAddUser(user_id, group_id), requestHelper, true, dataClick);
    }

    public static void groupRemoveUser(String user_id, String group_id, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupRemoveUser(user_id, group_id), requestHelper, true, dataClick);
    }


    public static void groupSelectList(String user_id, final RequestHelper requestHelper, final OnDataCallBack<List<ChatGroupBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupSelectList(user_id), requestHelper, true, dataClick);
    }

    public static void groupSelectUser(String group_id, final RequestHelper requestHelper, final OnDataCallBack<List<ChatGroupBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupSelectUser(group_id), requestHelper, true, dataClick);
    }

    public static void groupSelectUserMsg(String group_id, final RequestHelper requestHelper, final OnDataCallBack<List<UserMsgBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupSelectUserMsg(group_id), requestHelper, true, dataClick);
    }

    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    public static Disposable downloadFileNew(final String fileUrl, final String completePath, final OnDataCallBack<Boolean> onDataClick) {

        Disposable disposable = RetrofitFactory.getService(ApiService.class).downloadFile(fileUrl)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(responseBodyResponse -> writeResponseBodyToDiskNew(completePath, responseBodyResponse, (msg, finish) -> {
                    if (!finish) {

                    } else {
                        onDataClick.onCallBack(true);
                    }
                }), throwable -> {
                    throwable.printStackTrace();
                    onDataClick.onCallBack(false);

                });
        return disposable;
    }

    private static boolean writeResponseBodyToDiskNew(String completePath, Response<ResponseBody> response, DownLoadCallback downLoadCallback) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            ResponseBody body = response.body();
            if (response.code() != 200 && response.code() != 201) {
                return false;
            }
            File futureStudioIconFile = new File(completePath);//创建文件
            File dir = new File(futureStudioIconFile.getParent());
            if (!dir.exists()) {//如果该文件夹不存在，则进行创建  
                dir.mkdirs();//创建文件夹  
            }
            try {
                byte[] fileReader = new byte[1024];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                long startTime = System.currentTimeMillis();
                int read;
                long mProgress = 0;
                while ((read = inputStream.read(fileReader)) != -1) {
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    long progress = (long) (((float) fileSizeDownloaded / fileSize) * 100);
                    downLoadCallback.onDownLoad("正在下载..." + progress + "%", fileSizeDownloaded >= fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                LogUtils.getTag(e.getMessage() + ">>>>下载写入错误");
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            LogUtils.getTag(e.getMessage() + ">>>>下载写入错误");
            return false;
        }
    }

    public interface DownLoadCallback {
        void onDownLoad(String msg, boolean finish);
    }
}
