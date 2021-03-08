package com.xhab.utils.base;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.xhab.utils.LogUtils;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by dab on 2021/3/6 12:02
 */
public abstract class BaseActivity extends AppCompatActivity implements RequestHelperImp {
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

    public abstract int getContentViewRes();

    public void initData() {
    }

    public void initEvent() {
    }

    public void initView() {
    }

    public void visibility(@IdRes int res, boolean visibility) {
        View viewById = findViewById(res);
        if (viewById != null) {
            viewById.setVisibility(visibility ? View.VISIBLE : View.GONE);
        } else {
            LogUtils.e("visibility*****: res=====null!!!!!!!");
        }
    }

    public void click(@IdRes int res, @Nullable View.OnClickListener l) {
        View viewById = findViewById(res);
        if (viewById != null) {
            viewById.setOnClickListener(l);
        } else {
            LogUtils.e("visibility*****: res=====null!!!!!!!");
        }
    }


    public String getTextViewString(@IdRes int res) {
        View textView = findViewById(res);
        if (textView instanceof TextView) {
            return ((TextView) textView).getText().toString();
        } else {
            LogUtils.e("visibility*****: textView instanceof TextView !!!!!!!");
            return "";
        }
    }

    private RequestHelperAgency mRequestHelperAgency;


    @Override
    public RequestHelperAgency initRequestHelper() {
        if (mRequestHelperAgency == null) {
            mRequestHelperAgency = new RequestHelperAgency(this);
        }
        return mRequestHelperAgency;
    }
}
