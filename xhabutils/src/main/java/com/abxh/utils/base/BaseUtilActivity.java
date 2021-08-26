package com.abxh.utils.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abxh.utils.R;
import com.abxh.utils.net.RequestHelperAgency;
import com.abxh.utils.net.RequestHelperImp;
import com.abxh.utils.popupWindow.BasePopupWindow;
import com.abxh.utils.utils.LogUtils;
import com.abxh.utils.view.TwoTextLinearView;

/**
 * Created by dab on 2021/3/6 12:02
 */
public abstract class BaseUtilActivity extends AppCompatActivity implements RequestHelperImp {
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
        canLiftClickFinish();
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

    public TextView setTextViewString(@IdRes int res, CharSequence charSequence) {
        View textView = findViewById(res);
        if (textView instanceof TextView) {
            ((TextView) textView).setText(charSequence);
            return (TextView) textView;
        } else {
            LogUtils.e("visibility*****: textView instanceof TextView !!!!!!!");
        }
        return null;
    }

    public void setTitleString(String titleString) {
        setTextViewString(R.id.tv_title, titleString);
    }

    public void canLiftClickFinish() {
        visibility(R.id.iv_back, true);
        click(R.id.iv_back, v -> onBackPressed());
    }

    public TwoTextLinearView setTwoTextLinearRightText(@IdRes int res, CharSequence charSequence) {
        TwoTextLinearView twoTextLinearView = findViewById(res);
        if (twoTextLinearView != null) {
            twoTextLinearView.setRightText(charSequence);
        } else {
            LogUtils.e("visibility*****: textView instanceof TextView !!!!!!!");
        }
        return twoTextLinearView;
    }

    public String getTwoTextLinearRightText(@IdRes int res) {
        TwoTextLinearView twoTextLinearView = findViewById(res);
        if (twoTextLinearView != null) {
            return twoTextLinearView.getRightText().toString();
        } else {
            LogUtils.e("visibility*****: textView instanceof TextView !!!!!!!");
        }
        return "";
    }


    public TextView setRightTitleString(String titleString, @Nullable View.OnClickListener l) {
        TextView textView = setTextViewString(R.id.tv_right, titleString);
        click(R.id.tv_right, l);
        return textView;
    }

    public void showPopupWindow(BasePopupWindow basePopupWindow) {
        View decorView = this.getWindow().getDecorView();
        basePopupWindow.showAtLocation(decorView, Gravity.CENTER, 0, 0);
    }


    private RequestHelperAgency mRequestHelperAgency;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestHelperAgency != null) {
            mRequestHelperAgency.destroy();
        }
    }

    @Override
    public RequestHelperAgency initRequestHelper() {
        if (mRequestHelperAgency == null) {
            mRequestHelperAgency = new RequestHelperAgency(this);
        }
        return mRequestHelperAgency;
    }
}
