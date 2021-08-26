package com.abxh.utils.activity;

import android.content.Intent;
import android.text.SpannableStringBuilder;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abxh.utils.R;
import com.abxh.utils.adapter.WhitelistGuideAdapter;
import com.abxh.utils.base.BaseUtilActivity;
import com.abxh.utils.utils.SettingUtils;
import com.abxh.utils.utils.SpanBuilder;
import com.abxh.utils.utils.ToastUtil;

/**
 * Created by dab on 2021/4/21 21:50
 */
public class WhitelistActivity extends BaseUtilActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_whitelist;
    }

    public static String CHANNEL_MSG_ID = "id_108";//

    @Override
    public void canLiftClickFinish() {
        super.canLiftClickFinish();
    }

    private WhitelistGuideAdapter mWhitelistGuideAdapter = new WhitelistGuideAdapter();

    @Override
    public void initView() {
        super.initView();
        setTitleString("权限设置");
//        SettingUtils.getAppOps()
        visibility(R.id.layout_battery, !SettingUtils.isIgnoringBatteryOptimizations(this));
//        visibility(R.id.layout_banners, !SettingUtils.isBannersPermission(this, CHANNEL_MSG_ID));
        boolean bannersPermission = SettingUtils.isBannersPermission(this, CHANNEL_MSG_ID);
        String s = "";
        if (bannersPermission) {
            s = "通知横幅权限(已设置)";
            setTextViewString(R.id.view_05, s);
        } else {
            s = "通知横幅权限(未设置)";
            SpannableStringBuilder build = SpanBuilder.content(s).colorSpan(this, 7, 10, R.color.just_color_FF3859).build();
            setTextViewString(R.id.view_05, build);
        }
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(mWhitelistGuideAdapter);

    }

    @Override
    public void onBackPressed() {
        if (SettingUtils.isBannersPermission(this, CHANNEL_MSG_ID)) {
            finish();
        } else {
            ToastUtil.showToast("请先开启悬浮通知权限,否则将无法正常使用!");
        }

    }

    @Override
    public void initEvent() {
        super.initEvent();
        mWhitelistGuideAdapter.setWhitelistGuideBeans(SettingUtils.getWhitelistGuideBeans());
        click(R.id.tv_setup_battery, v -> {
            SettingUtils.requestIgnoreBatteryOptimizations(this, 0x86);
        });
        click(R.id.tv_setup_banners, v -> {
            SettingUtils.requestBannersPermission(this, CHANNEL_MSG_ID, 0x87);
        });
        click(R.id.tv_backstage, v -> {
            SettingUtils.enterWhiteListSetting(this, 0x88);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initView();
    }
}
