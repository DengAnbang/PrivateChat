package com.xhab.utils.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xhab.utils.R;
import com.xhab.utils.adapter.WhitelistGuideAdapter;
import com.xhab.utils.base.BaseUtilActivity;
import com.xhab.utils.utils.SettingUtils;
import com.xhab.utils.utils.ToastUtil;

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
        visibility(R.id.layout_banners, !SettingUtils.isBannersPermission(this, CHANNEL_MSG_ID));
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
