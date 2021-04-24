package com.hezeyi.privatechat.activity.recharge;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.xhab.utils.adapter.WhitelistGuideAdapter;
import com.xhab.utils.bean.WhitelistGuideBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        RecyclerView recyclerView = findViewById(com.xhab.utils.R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(mWhitelistGuideAdapter);
        List<WhitelistGuideBean> whitelistGuideBeans = new ArrayList<>();
        whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.pay1, "第一步"));
        whitelistGuideBeans.add(new WhitelistGuideBean(R.mipmap.pay2, "第二步"));
        mWhitelistGuideAdapter.setWhitelistGuideBeans(whitelistGuideBeans);
    }
}
