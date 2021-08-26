package com.abxh.utils.net;

import android.content.Context;

import com.abxh.utils.utils.RxUtils;
import com.abxh.utils.utils.ToastUtil;
import com.abxh.utils.dialog.ProgressDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2019/9/19 10:01
 */
public class RequestHelperAgency implements RequestHelper {
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private int mDialogNum;

    ///////////////必须调用destroy
    public RequestHelperAgency(Context context) {
        mContext = context;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
    }

    @Override
    public void showLoadDialog(final String msg) {

        mDialogNum++;
        RxUtils.runOnUiThread(() -> {
            mProgressDialog.setProgress(msg);
            mProgressDialog.show();
        });
    }

    @Override
    public void showSnackBar(String msg) {
        ToastUtil.showSnackBar(msg);
    }

    @Override
    public void dismissLoadDialog() {
        mDialogNum--;
        if (mDialogNum <= 0) {
            RxUtils.runOnUiThread(mProgressDialog::dismiss);
        }
    }

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void addDisposable(Disposable subscribe) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscribe);
    }

    @Override
    public ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        return mProgressDialog;
    }

    @Override
    public Context getSupportContext() {
        return mContext;
    }

    public void destroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

}
