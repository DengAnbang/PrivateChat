package com.hezeyi.privatechat.activity.recharge;

import android.content.Intent;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.SelectPriceBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.ToastUtil;

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
        click(R.id.rl_alipay, v -> {
            FunUtils.affirm(this, "模拟充值,点击充值就充值成功了", "充值", aBoolean -> {
                if (aBoolean) {
                    HttpManager.rechargeAdd(user_id, user_id, mSelectPriceBean.getMoney() + "", mSelectPriceBean.getTotalDay() + "", "1", this, o -> {
                        success();
                    });

                }
            });
        });
        click(R.id.rl_weixin, v -> {
            FunUtils.affirm(this, "模拟充值,点击充值就充值成功了", "充值", aBoolean -> {
                if (aBoolean) {
                    HttpManager.rechargeAdd(user_id, user_id, "0", mSelectPriceBean.getTotalDay() + "", "2", this, o -> {
                        success();
                    });

                }
            });
        });
    }

    private void success() {
        ToastUtil.showToast("充值成功!");
        Intent intent = new Intent();
        intent.putExtra("pay_id", getIntent().getStringExtra("pay_id"));
        setResult(RESULT_OK, intent);
        finish();
    }
}

