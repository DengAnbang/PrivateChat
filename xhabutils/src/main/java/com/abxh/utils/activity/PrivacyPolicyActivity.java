package com.abxh.utils.activity;

import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.abxh.utils.R;
import com.abxh.utils.base.BaseUtilActivity;

/**
 * Created by dab on 2021/4/7 12:07
 * type 0用户协议
 * type 1隐私政策
 */
public class PrivacyPolicyActivity extends BaseUtilActivity {

    private int mType;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_privacy_policy;
    }

    private FrameLayout web_view_container;
    private WebView web_view;

    @Override
    public void initEvent() {
        super.initEvent();
    }

    @Override
    public void initView() {
        super.initView();
        mType = getIntent().getIntExtra("type", 0);
        setTitleString(mType == 0 ? "用户协议" : "隐私政策");
        web_view_container = findViewById(R.id.web_view_container);
        web_view = new WebView(getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        web_view.setLayoutParams(params);
        web_view.setWebViewClient(new WebViewClient());
        //动态添加WebView，解决在xml引用WebView持有Activity的Context对象，导致内存泄露
        web_view_container.addView(web_view);
        String url = "";
        if (mType == 0) {
            url = "file:///android_asset/user_agreement.html";
        }else {
            url = "file:///android_asset/privacy_policy.html";
        }
        web_view.loadUrl(url);

    }
}
