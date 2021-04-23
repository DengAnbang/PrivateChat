package com.hezeyi.privatechat.activity.recharge;

import android.text.TextUtils;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.SelectPriceAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.FunUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/14 11:31
 */
public class RechargeSetUpActivity extends BaseActivity {

    private String mId;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_recharge_setup;
    }

    private SelectPriceAdapter mSelectPriceAdapter = new SelectPriceAdapter();

    @Override
    public void initView() {
        super.initView();
        setTitleString("充值金额设置");
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(mSelectPriceAdapter);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mSelectPriceAdapter.setOnItemClickListener((view, position, selectPriceBean) -> {
            if (selectPriceBean != null) {
                setTextViewString(R.id.et_money, selectPriceBean.getMoney() + "");
                setTextViewString(R.id.et_day, selectPriceBean.getDay());
                setTextViewString(R.id.et_giving_day, selectPriceBean.getGiving_day());
                mId = selectPriceBean.getId();
            } else {
                mId = "";
            }
        });
        click(R.id.tv_update, v -> {
            String money = getTextViewString(R.id.et_money);
            FunUtils.checkIsNullable(money, "金额不能为空");
            String day = getTextViewString(R.id.et_day);
            FunUtils.checkIsNullable(day, "天数不能为空");
            FunUtils.checkIsNullable(mId, "请选择要修改的金额");
            String giving_day = getTextViewString(R.id.et_giving_day);
            if (TextUtils.isEmpty(giving_day)) {
                giving_day = "0";
            }
            HttpManager.priceUpdate(money, day, giving_day, mId, this, o -> {
                showSnackBar("修改成功!");
                initData();
            });
        });
        click(R.id.tv_add, v -> {
            String money = getTextViewString(R.id.et_money);
            FunUtils.checkIsNullable(money, "金额不能为空");
            String day = getTextViewString(R.id.et_day);
            FunUtils.checkIsNullable(day, "天数不能为空");
            String giving_day = getTextViewString(R.id.et_giving_day);
            if (TextUtils.isEmpty(giving_day)) {
                giving_day = "0";
            }
            HttpManager.priceAdd(money, day, giving_day, this, o -> {
                showSnackBar("添加成功!");
                initData();
            });

        });
        click(R.id.tv_delete, v -> {
            FunUtils.checkIsNullable(mId, "请选择要删除的金额");
            HttpManager.priceDelete(mId, this, o -> {
                showSnackBar("添加成功!");
                initData();
            });
        });
    }

    @Override
    public void initData() {
        super.initData();
        mSelectPriceAdapter.clear();
        mId = "";
        setTextViewString(R.id.et_money, "");
        setTextViewString(R.id.et_day, "");
        setTextViewString(R.id.et_giving_day, "");
        HttpManager.priceSelectAll(MyApplication.getInstance().getUserMsgBean().getUser_id(), this, selectPriceBeans -> {
            mSelectPriceAdapter.setSelectPriceBeans(selectPriceBeans);
        });
    }
}
