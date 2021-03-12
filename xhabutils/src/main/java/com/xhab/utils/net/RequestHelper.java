package com.xhab.utils.net;

import android.content.Context;

import com.xhab.utils.view.ProgressDialog;

import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2018/4/11 18:11
 */

public interface RequestHelper {
    /**
     * 显示SnackBar消息
     *
     * @param msg 消息
     */
    void showSnackBar(String msg);

    /**
     * 显示加载进度
     *
     * @param msg 显示的内容
     * @return
     */
    void showLoadDialog(String msg);

    /**
     * 取消加载进度显示
     */
    void dismissLoadDialog();

    void addDisposable(Disposable subscribe);

    /**
     * 获取进度控件
     *
     * @return
     */
    ProgressDialog getProgressDialog();

    Context getSupportContext();
}
