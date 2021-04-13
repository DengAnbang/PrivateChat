package com.hezeyi.privatechat.activity.account;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.RechargeRecordAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/13 22:06
 */
public class RechargeRecordActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_recharge_record;
    }

    private RechargeRecordAdapter mRechargeRecordAdapter = new RechargeRecordAdapter();

    @Override
    public void initView() {
        super.initView();
        setTitleString("充值查看(未完成)");
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRechargeRecordAdapter);
    }

    @Override
    public void initData() {
        super.initData();
        HttpManager.rechargeSelectByType("0", this, rechargeRecordBeans -> {
            mRechargeRecordAdapter.setRechargeRecordBeans(rechargeRecordBeans);
        });
    }
}
