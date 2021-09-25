package com.hezeyi.common.base;

import android.os.Bundle;
import android.view.Window;

import com.abxh.jetpack.IViewModel;
import com.abxh.jetpack.base.BaseViewModelActivity;

import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/9/17 11:10
 */

public abstract class BaseActivity<VM extends IViewModel> extends BaseViewModelActivity<VM> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getContentViewRes() != 0) {
            setContentView(getContentViewRes());
        }
        initView();
        initEvent();
        initData();
    }

    public int getContentViewRes() {
        return 0;
    }

    public void initData() {
    }

    public void initEvent() {
    }

    public void initView() {
    }
}
