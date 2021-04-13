package com.hezeyi.privatechat.activity.account;

import android.view.Gravity;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.SelectPriceAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.SelectPriceBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.TimeUtils;
import com.xhab.utils.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

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
        HttpManager.userSelectById(mUserId, true, this, userMsgBean -> {
            if (userMsgBean != null) {
                mUserMsgBean = userMsgBean;
                setTwoTextLinearRightText(R.id.ttv_account, userMsgBean.getAccount()).getRightTextView().setGravity(Gravity.RIGHT);
                setTwoTextLinearRightText(R.id.ttv_vip_time, TimeUtils.toTimeByString(userMsgBean.getVip_time())).getRightTextView().setGravity(Gravity.RIGHT);
            } else {
                ToastUtil.showToast("用户不存在!");
                finish();
            }
        });
        List<SelectPriceBean> mSelectPriceBeans = new ArrayList<>();

        mSelectPriceBeans.add(new SelectPriceBean("1", "1"));
        mSelectPriceBeans.add(new SelectPriceBean("7", "5"));
        mSelectPriceBeans.add(new SelectPriceBean("14", "10"));
        mSelectPriceBeans.add(new SelectPriceBean("30", "15"));
        mSelectPriceBeans.add(new SelectPriceBean("60", "25"));
        mSelectPriceBeans.add(new SelectPriceBean("180", "70"));
        mSelectPriceBeans.add(new SelectPriceBean("360", "120"));
        mSelectPriceAdapter.setSelectPriceBeans(mSelectPriceBeans);
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
                    old = old + 24 * 60 * 60 * 1000L * Long.parseLong(selectPriceBean.getDay());
                    setTextViewString(R.id.tv_hint, "支付" + selectPriceBean.getMoney() + "元,可延期至:" + TimeUtils.toTimeByString(old));
                } catch (Exception e) {

                }

            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(mSelectPriceAdapter);
    }
}
