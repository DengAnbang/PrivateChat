package com.hezeyi.privatechat.activity.recharge;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.abxh.utils.adapter.WhitelistGuideAdapter;
import com.abxh.utils.bean.WhitelistGuideBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dab on 2021/4/24 11:51
 */

public class DescriptionActivity extends BaseActivity {
    private WhitelistGuideAdapter mWhitelistGuideAdapter = new WhitelistGuideAdapter();
    @Override
    public int getContentViewRes() {
        return R.layout.activity_pay_description;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("充值说明");
        RecyclerView recyclerView = findViewById(com.abxh.utils.R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(mWhitelistGuideAdapter);
        List<WhitelistGuideBean> whitelistGuideBeans = new ArrayList<>();
        whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.pay_hint1, "第一步"));
        whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.pay_hint2, "第二步"));
        mWhitelistGuideAdapter.setWhitelistGuideBeans(whitelistGuideBeans);
    }
}
