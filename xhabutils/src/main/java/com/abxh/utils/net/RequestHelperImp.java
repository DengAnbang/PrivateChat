package com.abxh.utils.net;

import android.content.Context;

import com.abxh.utils.dialog.ProgressDialog;

import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2019/9/19 09:41
 */
public interface RequestHelperImp extends RequestHelper {
    RequestHelperAgency initRequestHelper();

    @Override
    default void showSnackBar(String msg) {
        initRequestHelper().showSnackBar(msg);
    }

    @Override
    default void showLoadDialog(String msg) {
        initRequestHelper().showLoadDialog(msg);
    }

    @Override
    default void dismissLoadDialog() {
        initRequestHelper().dismissLoadDialog();
    }

    @Override
    default void addDisposable(Disposable subscribe) {
        initRequestHelper().addDisposable(subscribe);
    }

    @Override
    default ProgressDialog getProgressDialog() {
        return initRequestHelper().getProgressDialog();
    }

    @Override
    default Context getSupportContext() {
        return initRequestHelper().getSupportContext();
    }
}
