package com.hezeyi.privatechat.net;

import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.hezeyi.privatechat.bean.ResultData;
import com.abxh.utils.inteface.OnDataCallBack;
import com.abxh.utils.net.RequestHelper;
import com.abxh.utils.utils.LogUtils;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


/**
 * Created by dab on 2018/4/11 18:add_video
 */

public class ResponseHelper {
    /**
     * 请求拦截器
     */
    public static Map<String, InterceptResponse<ResultData>> sIntercept;

    public static void addInterceptResponse(String key, InterceptResponse<ResultData> interceptResponse) {
        if (sIntercept == null) {
            sIntercept = new HashMap<>();
        }
        sIntercept.put(key, interceptResponse);
    }

    public static void removeInterceptResponse(String key, InterceptResponse<ResultData> interceptResponse) {
        if (sIntercept == null) return;
        sIntercept.remove(key);
    }

    /**
     * 处理拦截器
     *
     * @param stateBean
     * @param requestHelper
     * @return
     */
    public static boolean interceptResponse(ResultData stateBean, final RequestHelper requestHelper) {
        if (sIntercept != null && sIntercept.size() > 0) {
            for (InterceptResponse<ResultData> interceptResponse : sIntercept.values()) {
                if (interceptResponse.isIntercept(stateBean)) {
                    return true;
                }
            }
        }
        if (!stateBean.getCode().equals("0")) {
            requestHelper.showSnackBar(stateBean.getMsg());
            return true;
        }
        return false;
    }

    /**
     * 简化请求的一个方法(只有code==1时才算成功),直接在主线程返回,如果请求失败,会在这个里面提醒用户
     * 请求需要一个RequestHelper接口,因为要
     * 显示请求动画,
     * 取消动画,
     * 请求完成自动取消订阅,
     * 取消未完成时取消请求,
     * 错误提醒的弹窗提示
     *
     * @param observable
     * @param requestHelper
     * @param showLoadDialog
     * @param dataClick
     * @param <O>
     * @param <I>
     */
    public static <O, I extends ResultData<O>> void requestSucceed(Observable<I> observable, final RequestHelper requestHelper, final boolean showLoadDialog, final OnDataCallBack<O> dataClick) {
        request(observable, requestHelper, showLoadDialog, i -> {
            if (i == null) return;
            if (interceptResponse(i, requestHelper)) return;
            dataClick.onCallBack(i.getData());
        });
    }

    /**
     * 统一处理网络错误,请求等等
     *
     * @param observable
     * @param requestHelper
     * @param showLoadDialog
     * @param dataClick
     * @param <T>
     */
    public static <T> void request(Observable<T> observable, final RequestHelper requestHelper, final boolean showLoadDialog, final OnDataCallBack<T> dataClick) {

        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (showLoadDialog) {
                        requestHelper.showLoadDialog("加载中");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T value) {
                        dataClick.onCallBack(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestHelper.dismissLoadDialog();
                        String message = "";
                        do {
                            if (e instanceof JsonSyntaxException) {
                                message = "数据解析出错！";
                                break;
                            }
                            if (e instanceof ConnectException) {
                                message = "网络异常，请检查您的网络状态！";
                                break;
                            }
                            if (e instanceof SocketTimeoutException) {
                                message = "网络异常，请检查您的网络状态！";
                                break;
                            }
                            if (e instanceof HttpException) {
                                message = "服务器异常，请稍后重试！";
                                break;
                            }
                            if (e instanceof SocketException) {
                                message = "服务器连接异常，请稍后重试！";
                                break;
                            }
                            if (e instanceof UnknownHostException) {
                                message = "服务器连接异常，请稍后重试！";
                                break;
                            }
                            message = "连接异常，请稍后重试！";
                        } while (false);
                        dataClick.onCallBack(null);
                        LogUtils.e(e.getMessage());
                        if (!TextUtils.isEmpty(message)) {
                            requestHelper.showSnackBar(message);
                            return;
                        }
//                        requestHelper.showSnackBar(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        requestHelper.addDisposable(d);
                    }

                    @Override
                    public void onComplete() {
                        requestHelper.dismissLoadDialog();
                    }
                });


    }

    /**
     * 统一处理网络错误,请求等等
     *
     * @param observable
     * @param requestHelper
     * @param showLoadDialog
     * @param observer
     * @param <T>
     */
    public static <T> void justRequest(Observable<T> observable, final RequestHelper requestHelper, final boolean showLoadDialog, final Observer<T> observer) {

        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (showLoadDialog) {
                        requestHelper.showLoadDialog("加载中");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T value) {
                        observer.onNext(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        requestHelper.addDisposable(d);
                    }

                    @Override
                    public void onComplete() {
                        requestHelper.dismissLoadDialog();
                    }
                });


    }
}
