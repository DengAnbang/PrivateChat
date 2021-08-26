package com.hezeyi.privatechat.activity.account;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.annotation.Nullable;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.abxh.utils.inteface.OnItemClickListener;
import com.abxh.utils.utils.FunUtils;
import com.abxh.utils.utils.ToastUtil;

/**
 * Created by dab on 2021/3/12 10:52
 * 安全问题设置
 */
public class SecurityQuestionSetupActivity extends BaseActivity {
    private static final int ResultCode = 0x789;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_security_question_set_up;
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = new Intent(this, SecurityQuestionVerifyActivity.class);
        intent.putExtra("account", MyApplication.getInstance().getUserMsgBean().getAccount());
        MyApplication.getInstance().setLock(false);
        startActivityForResult(intent, ResultCode);
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("安全问题设置");
    }

    private String[] strings = {"第一只宠物叫什么名字"
            , "第一只宠物什么品种"
            , "配偶名字"
            , "母亲生日"
            , "父亲生日"
            , "配偶生日"
            , "子女生日"
            , "子女生日"
            , "自己生日"
            , "大学在哪里"};

    @Override
    public void initEvent() {
        super.initEvent();
        click(R.id.tv_submit, view -> {
            String a1 = getTextViewString(R.id.et_a1);
            String a2 = getTextViewString(R.id.et_a2);
            String q1 = getTwoTextLinearRightText(R.id.ttv_q1);
            String q2 = getTwoTextLinearRightText(R.id.ttv_q2);
            if (FunUtils.checkIsNullable(q1, "问题1,不能为空!")) return;
            if (FunUtils.checkIsNullable(a1, "答案1,不能为空!")) return;
            if (FunUtils.checkIsNullable(q2, "问题2,不能为空!")) return;
            if (FunUtils.checkIsNullable(a2, "答案2,不能为空!")) return;
            HttpManager.securityUpdate(MyApplication.getInstance().getUserMsgBean().getUser_id(), q1, a1, q2, a2, this, o -> {
                ToastUtil.showToast("设置成功!");
                finish();
            });
        });
        click(R.id.ttv_q1, v -> {
            showListPopupWindow(this, strings, findViewById(R.id.ttv_q1), (id, position, s) -> {
                setTwoTextLinearRightText(R.id.ttv_q1, s);
            });
        });

        click(R.id.ttv_q2, v -> {
            showListPopupWindow(this, strings, findViewById(R.id.ttv_q2), (id, position, s) -> {
                setTwoTextLinearRightText(R.id.ttv_q2, s);
            });
        });

    }

    public static void showListPopupWindow(Activity activity, final String[] strings, View view, final OnItemClickListener<String> onDataClick) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(activity, R.layout.window_list, strings);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setWidth(view.getWidth());
        listPopupWindow.setAdapter(stringArrayAdapter);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setModal(true);//设置是否是模式
        listPopupWindow.setOnItemClickListener((parent, view1, position, id) -> {
            listPopupWindow.dismiss();
            onDataClick.onItemClick(parent, position, strings[position]);
        });
        listPopupWindow.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCode) {
            if (SecurityQuestionVerifyActivity.RESULT_CODE_NO_SETUP == resultCode) {
                return;
            }
            if (SecurityQuestionVerifyActivity.RESULT_CODE_OK == resultCode) {
                return;
            }
        }
        finish();
    }
}
