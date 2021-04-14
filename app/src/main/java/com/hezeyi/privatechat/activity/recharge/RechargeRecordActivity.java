package com.hezeyi.privatechat.activity.recharge;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.RechargeRecordAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/13 22:06
 * type 0系统管理员查看
 * type 1 自己查看(需要user_id)
 */
public class RechargeRecordActivity extends BaseActivity {

    private String mType;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_recharge_record;
    }

    private RechargeRecordAdapter mRechargeRecordAdapter = new RechargeRecordAdapter();

    @Override
    public void initView() {
        super.initView();
        setTitleString("充值查看(未完成)");
        mType = getIntent().getStringExtra("type");
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRechargeRecordAdapter);
//        SuspendDecoration suspendDecoration = new SuspendDecoration(this) {
//            @Override
//            public boolean isSameGroup(int priorGroupId, int nowGroupId) throws Exception {
//                String s1 = TimeUtils.toTimeByString(mRechargeRecordAdapter.getDataBeans().get(priorGroupId).getCreated(), "yyyy-MM-dd");
//                String s2 = TimeUtils.toTimeByString(mRechargeRecordAdapter.getDataBeans().get(nowGroupId).getCreated(), "yyyy-MM-dd");
//                return Objects.equals(s1, s2);
//            }
//
//            @Override
//            public String showTitle(int position) throws Exception {
//                return TimeUtils.toTimeByString(mRechargeRecordAdapter.getDataBeans().get(position).getCreated(), "yyyy-MM-dd");
//            }
//        };
//        suspendDecoration.setTitleGravity(SuspendDecoration.TitleGravity.TITLE_GRAVITY_CENTER);
//        recyclerView.addItemDecoration(suspendDecoration);
    }

    @Override
    public void initData() {
        super.initData();
        if (mType.equals("0")) {
            HttpManager.rechargeSelectAll(this, rechargeRecordBeans -> {
                mRechargeRecordAdapter.setRechargeRecordBeans(rechargeRecordBeans);
            });
        } else if (mType.equals("1")) {
            HttpManager.rechargeSelectByUserId(getIntent().getStringExtra("user_id"), this, rechargeRecordBeans -> {
                mRechargeRecordAdapter.setRechargeRecordBeans(rechargeRecordBeans);
            });
        }

    }
}
