package com.hezeyi.privatechat.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hezeyi.privatechat.MyApplication;
import com.xhab.utils.base.BaseUtilActivity;

/**
 * Created by dab on 2021/3/9 15:40
 */
public abstract class BaseActivity extends BaseUtilActivity {

    public boolean isCanLock() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().setLock(isCanLock());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication.getInstance().setLock(isCanLock());

    }
}

