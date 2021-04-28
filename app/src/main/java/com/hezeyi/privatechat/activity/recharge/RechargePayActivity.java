package com.hezeyi.privatechat.activity.recharge;

import android.content.Intent;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.SelectPriceBean;
import com.hezeyi.privatechat.net.HttpManager;

/**
 * Created by dab on 2021/4/14 10:33
 * pay_id 金额的id
 * account 充值的对应账号
 * user_id 充值的对应id
 */
public class RechargePayActivity extends BaseActivity {

    private String user_id;
    private SelectPriceBean mSelectPriceBean;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_recharge_pay;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("收银台");
        String pay_id = getIntent().getStringExtra("pay_id");
        user_id = getIntent().getStringExtra("user_id");
        String account = getIntent().getStringExtra("account");
        setTwoTextLinearRightText(R.id.ttv_account, account);
        HttpManager.priceSelectById(pay_id, this, selectPriceBean -> {
            mSelectPriceBean = selectPriceBean;
            setTwoTextLinearRightText(R.id.ttv_msg, selectPriceBean.getMoney() + "元");

        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        visibility(R.id.rl_alipay,false);
        visibility(R.id.rl_weixin,false);
//        visibility(R.id.rl_customer_service, false);
//        click(R.id.rl_alipay, v -> {
//            FunUtils.affirm(this, "模拟充值,点击充值就充值成功了", "充值", aBoolean -> {
//                if (aBoolean) {
//                    success("zfb");
//                }
//            });
//        });
//        click(R.id.rl_weixin, v -> {
//            FunUtils.affirm(this, "模拟充值,点击充值就充值成功了", "充值", aBoolean -> {
//                if (aBoolean) {
//                    success("wx");
//                }
//            });
//        });
        click(R.id.rl_customer_service, v -> {
            Intent intent = new Intent(this, CustomerServicePayActivity.class);
            intent.putExtra("pay_id", getIntent().getStringExtra("pay_id"));
            startActivity(intent);
        });
    }

    private void success(String recharge_type) {
        Intent intent = new Intent();
        intent.putExtra("pay_id", getIntent().getStringExtra("pay_id"));
        intent.putExtra("recharge_type", recharge_type);
        setResult(RESULT_OK, intent);
        finish();
    }
}

