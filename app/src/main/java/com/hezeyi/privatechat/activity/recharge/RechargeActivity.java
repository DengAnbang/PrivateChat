package com.hezeyi.privatechat.activity.recharge;

import android.content.Intent;
import android.view.Gravity;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.SelectPriceAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.TimeUtils;
import com.xhab.utils.utils.ToastUtil;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/13 17:04
 * user_id
 */
public class RechargeActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_recharge;
    }

    private UserMsgBean mUserMsgBean;
    private SelectPriceAdapter mSelectPriceAdapter = new SelectPriceAdapter();
    private String mUserId;


    @Override
    public void initData() {
        super.initData();
        mUserId = getIntent().getStringExtra("user_id");
        HttpManager.userSelectById(mUserId, MyApplication.getInstance().getUserMsgBean().getUser_id(), true, this, userMsgBean -> {
            if (userMsgBean != null) {
                mUserMsgBean = userMsgBean;
                setTwoTextLinearRightText(R.id.ttv_account, userMsgBean.getAccount()).getRightTextView().setGravity(Gravity.RIGHT);
                setTwoTextLinearRightText(R.id.ttv_vip_time, TimeUtils.toTimeByString(userMsgBean.getVip_time())).getRightTextView().setGravity(Gravity.RIGHT);
            } else {
                ToastUtil.showToast("用户不存在!");
                finish();
            }
        });

        setRightTitleString("充值记录", v -> {
            Intent intent = new Intent(this, RechargeRecordActivity.class);
            intent.putExtra("type", "1");
            intent.putExtra("user_id", mUserId);
            startActivity(intent);
        });
        HttpManager.priceSelectAll(mUserId, this, selectPriceBeans -> {
            mSelectPriceAdapter.setSelectPriceBeans(selectPriceBeans);
        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mSelectPriceAdapter.setOnItemClickListener((view, position, selectPriceBean) -> {
            if (mUserMsgBean != null) {
                String vip_time = mUserMsgBean.getVip_time();
                try {
                    long old = TimeUtils.toMillisecond(Long.parseLong(vip_time));
                    long now = TimeUtils.toMillisecond(System.currentTimeMillis());
                    if (old < now) {
                        old = now;
                    }
                    old = old + 24 * 60 * 60 * 1000L * selectPriceBean.getTotalDay();
                    setTextViewString(R.id.tv_hint, "支付" + selectPriceBean.getMoney() + "元,可延期至:" + TimeUtils.toTimeByString(old));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        click(R.id.tv_submit, v -> {
            if (mSelectPriceAdapter.getSelectPriceBean() == null) {
                showSnackBar("请先选择充值金额");
                return;
            }
            if (mUserMsgBean == null) {
                initData();
                return;
            }
            Intent intent = new Intent(this, RechargePayActivity.class);
            intent.putExtra("pay_id", mSelectPriceAdapter.getSelectPriceBean().getId());
            intent.putExtra("user_id", mUserMsgBean.getUser_id());
            intent.putExtra("account", mUserMsgBean.getAccount());
            startActivityForResult(intent, 0x86);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x86) {
            if (resultCode == RESULT_OK && data != null) {
                String pay_id = data.getStringExtra("pay_id");
                HttpManager.userRecharge(mUserId, pay_id, this, o -> {
                    showSnackBar("充值成功!");
                    initData();
                });
            } else {
                showSnackBar("支付失败!");
            }
        }
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("充值");
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(mSelectPriceAdapter);
    }
}
