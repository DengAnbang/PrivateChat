package com.hezeyi.privatechat.net;


import com.hezeyi.privatechat.BuildConfig;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.RechargeRecordBean;
import com.hezeyi.privatechat.bean.ResultData;
import com.hezeyi.privatechat.bean.SecurityBean;
import com.hezeyi.privatechat.bean.SelectPriceBean;
import com.hezeyi.privatechat.bean.UpdatesBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.abxh.utils.dialog.ProgressDialog;
import com.abxh.utils.inteface.OnDataCallBack;
import com.abxh.utils.net.RequestHelper;
import com.abxh.utils.net.RequestHelperAgency;
import com.abxh.utils.utils.AppUtils;
import com.abxh.utils.utils.BitmapUtil;
import com.abxh.utils.utils.FunUtils;
import com.abxh.utils.utils.LogUtils;
import com.abxh.utils.utils.RxUtils;
import com.abxh.utils.utils.ToastUtil;

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
        filePath = BitmapUtil.compressImage(filePath);
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


    public static void updatesCheck(boolean showLoadDialog, final RequestHelper requestHelper, final OnDataCallBack<UpdatesBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .updatesCheck(), requestHelper, showLoadDialog, dataClick);
    }

    //    public static void login(String account, String password, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
//        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
//                .login(account, password), requestHelper, true, dataClick);
//    }
    public static void login(String account, String password, boolean showLoadDialog, final RequestHelper requestHelper, final OnDataCallBack<ResultData<UserMsgBean>> dataClick) {
        ResponseHelper.request(RetrofitFactory.getService(ApiService.class)
                .login(account, password), requestHelper, showLoadDialog, dataClick);
    }

    public static void register(String account, String password, String name, String headPortrait, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .register(account, password, name, headPortrait), requestHelper, true, dataClick);
    }

    public static void userUpdate(String account, String password, String name, String vip_time, String headPortrait, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userUpdate(account, password, name, vip_time, headPortrait), requestHelper, true, dataClick);
    }

    public static void friendCommentSet(String user_id, String to_user_id, String nickname, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .friendCommentSet(user_id, to_user_id, nickname), requestHelper, true, dataClick);
    }

    public static void userRecharge(String user_id, String execution_user_id, String recharge_type, String pay_id, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userRecharge(user_id, execution_user_id, recharge_type, pay_id), requestHelper, true, dataClick);
    }

    public static void securityUpdate(String user_id, String q1, String a1, String q2, String a2, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .securityUpdate(user_id, q1, a1, q2, a2), requestHelper, true, dataClick);
    }


    public static void securitySelect(String user_id, final RequestHelper requestHelper, final OnDataCallBack<SecurityBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .securitySelect(user_id), requestHelper, true, dataClick);
    }

    public static void friendAdd(String user_id, String to_user_id, String friend_type, String chat_pwd, final RequestHelper requestHelper, final OnDataCallBack<String> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .friendAdd(user_id, to_user_id, friend_type, chat_pwd), requestHelper, true, dataClick);
    }

    public static void friendDelete(String user_id, String to_user_id, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .friendDelete(user_id, to_user_id), requestHelper, true, dataClick);
    }

    public static void userSelectById(String user_id, String my_user_id, boolean showLoadDialog, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userSelectById(user_id, my_user_id), requestHelper, showLoadDialog, dataClick);
    }

    public static void userSelectByAccount(String account, boolean showLoadDialog, final RequestHelper requestHelper, final OnDataCallBack<UserMsgBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userSelectByAccount(account), requestHelper, showLoadDialog, dataClick);
    }

    public static void userSelectByFuzzySearch(String word, final RequestHelper requestHelper, final OnDataCallBack<List<UserMsgBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userSelectByFuzzySearch(word), requestHelper, true, dataClick);
    }

    public static void userSelectByFuzzySearchAll(String word, final RequestHelper requestHelper, final OnDataCallBack<List<UserMsgBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userSelectByFuzzySearchAll(word), requestHelper, true, dataClick);
    }

    public static void userSelectFriend(String user_id, String friend_type, final RequestHelper requestHelper, final OnDataCallBack<List<UserMsgBean>> dataClick) {
        userSelectFriend(user_id, friend_type, true, requestHelper, dataClick);
    }

    public static void userSelectFriend(String user_id, String friend_type, boolean showLoadDialog, final RequestHelper requestHelper, final OnDataCallBack<List<UserMsgBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .userSelectFriend(user_id, friend_type), requestHelper, showLoadDialog, dataClick);
    }

    public static void groupRegister(String group_name, String user_id, final RequestHelper requestHelper, final OnDataCallBack<ChatGroupBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupRegister(group_name, user_id), requestHelper, true, dataClick);
    }

    public static void groupAddUser(String user_ids, String group_id, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupAddUser(user_ids, group_id), requestHelper, true, dataClick);
    }

    public static void groupRemoveUser(String user_id, String group_id, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupRemoveUser(user_id, group_id), requestHelper, true, dataClick);
    }


    public static void groupSelectList(String user_id, boolean showLoadDialog, final RequestHelper requestHelper, final OnDataCallBack<List<ChatGroupBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupSelectList(user_id), requestHelper, showLoadDialog, dataClick);
    }

    public static void groupSelectUser(String group_id, final RequestHelper requestHelper, final OnDataCallBack<List<ChatGroupBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupSelectUser(group_id), requestHelper, true, dataClick);
    }

    public static void groupSelectUserMsg(String group_id, final RequestHelper requestHelper, final OnDataCallBack<List<UserMsgBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .groupSelectUserMsg(group_id), requestHelper, true, dataClick);
    }

    public static void rechargeAdd(String user_id, String execution_user_id, String money, String day, String recharge_type, final RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .rechargeAdd(user_id, execution_user_id, money, day, recharge_type), requestHelper, true, dataClick);
    }

    public static void rechargeSelectByType(String recharge_type, final RequestHelper requestHelper, final OnDataCallBack<List<RechargeRecordBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .rechargeSelectByType(recharge_type), requestHelper, true, dataClick);
    }

    public static void rechargeSelectByUserId(String user_id, final RequestHelper requestHelper, final OnDataCallBack<List<RechargeRecordBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .rechargeSelectByUserId(user_id), requestHelper, true, dataClick);
    }

    public static void rechargeSelectAll(final RequestHelper requestHelper, final OnDataCallBack<List<RechargeRecordBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .rechargeSelectAll(), requestHelper, true, dataClick);
    }

    public static void priceDelete(String id, RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .priceDelete(id), requestHelper, true, dataClick);
    }

    public static void priceUpdate(String money, String day, String giving_day, String pay_image, String id, RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .priceUpdate(money, day, giving_day, pay_image, id), requestHelper, true, dataClick);
    }

    public static void priceSelectById(String id, RequestHelper requestHelper, final OnDataCallBack<SelectPriceBean> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .priceSelectById(id), requestHelper, true, dataClick);
    }

    public static void priceAdd(String money, String day, String giving_day,String pay_image, RequestHelper requestHelper, final OnDataCallBack<Object> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .priceAdd(money, day, giving_day, pay_image), requestHelper, true, dataClick);
    }

    public static void priceSelectAll(String user_id, final RequestHelper requestHelper, final OnDataCallBack<List<SelectPriceBean>> dataClick) {
        ResponseHelper.requestSucceed(RetrofitFactory.getService(ApiService.class)
                .priceSelectAll(user_id), requestHelper, true, dataClick);
    }

    public static void updatesCheck(boolean showLoadDialog, RequestHelper requestHelper) {
        HttpManager.updatesCheck(showLoadDialog, requestHelper, updatesBean -> {
            if (updatesBean.getVersion_code() > BuildConfig.VERSION_CODE) {
                FunUtils.affirm(requestHelper.getSupportContext(), "发现新版本:" + updatesBean.getVersion_name(), "更新", aBoolean -> {
                    if (aBoolean) {
                        boolean delete = new File(Const.FilePath.FileAppUpdatesName).delete();
                        HttpManager.downloadFileProgress(Const.Api.AppUpdatesDownload, Const.FilePath.FileAppUpdatesName, (msg, finish, e) -> {
                            if (e != null) {
                                requestHelper.getProgressDialog().dismiss();
                                return;
                            }
                            if (!finish) {
                                RxUtils.runOnUiThread(() -> {
                                    ProgressDialog progressDialog = requestHelper.getProgressDialog();
                                    progressDialog.show();
                                    progressDialog.setProgress(msg);
                                });

                            } else {
                                RxUtils.runOnUiThread(() -> {
                                    requestHelper.getProgressDialog().dismiss();
                                    LogUtils.e("initEvent*****: " + Const.FilePath.FileAppUpdatesName);
                                    AppUtils.installApk(requestHelper.getSupportContext(), Const.FilePath.FileAppUpdatesName);
//                                        AppUtils.checkIsAndroidO(this, Const.FilePath.FileAppUpdatesName, KEY_CHECK_IS_ANDROID_O);
                                });

                            }
                        });
                    }
                });
            } else {
                if (showLoadDialog) {
                    ToastUtil.showToast("已经是最新版本了!");
                }
            }
//                LogUtils.e("initEvent*****: " + updatesBean.getVersion_code());
        });
    }

    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    public static Disposable downloadFile(final String fileUrl, final String completePath, final OnDataCallBack<Boolean> onDataClick) {

        Disposable disposable = RetrofitFactory.getService(ApiService.class).downloadFile(fileUrl)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(responseBodyResponse -> writeResponseBodyToDiskNew(completePath, responseBodyResponse, (msg, finish, e) -> {
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

    public static Disposable downloadFileProgress(final String fileUrl, final String completePath, final DownLoadCallback loadCallback) {

        Disposable disposable = RetrofitFactory.getService(ApiService.class).downloadFile(fileUrl)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(responseBodyResponse -> writeResponseBodyToDiskNew(completePath, responseBodyResponse, loadCallback), throwable -> {
                    throwable.printStackTrace();
                    RxUtils.runOnUiThread(() -> {
                        loadCallback.onDownLoad("", false, "下载出错了");
                    });

                });
        return disposable;
    }

    private static boolean writeResponseBodyToDiskNew(String completePath, Response<ResponseBody> response, DownLoadCallback downLoadCallback) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            ResponseBody body = response.body();
            if (response.code() == 404) {
                downLoadCallback.onDownLoad("", false, "文件已过期!");
                return false;
            }
            if (response.code() != 200 && response.code() != 201) {
                downLoadCallback.onDownLoad("", false, "下载错误");
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
                    long finalFileSizeDownloaded = fileSizeDownloaded;
                    RxUtils.runOnUiThread(() -> {
                        downLoadCallback.onDownLoad("正在下载..." + progress + "%", finalFileSizeDownloaded >= fileSize, null);
                    });

                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                RxUtils.runOnUiThread(() -> {
                    downLoadCallback.onDownLoad("0", false, "下载错误");
                });

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
            RxUtils.runOnUiThread(() -> {
                downLoadCallback.onDownLoad("0", false, "下载错误");
            });
            LogUtils.getTag(e.getMessage() + ">>>>下载写入错误");
            return false;
        }
    }

    public interface DownLoadCallback {
        void onDownLoad(String msg, boolean finish, Object e);
    }
}
