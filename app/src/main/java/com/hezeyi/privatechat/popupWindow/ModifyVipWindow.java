package com.hezeyi.privatechat.popupWindow;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.hezeyi.privatechat.R;
import com.abxh.utils.inteface.OnDataCallBack;
import com.abxh.utils.popupWindow.BasePopupWindow;

/**
 * Created by dab on 2021/4/1 20:24
 */
public class ModifyVipWindow extends BasePopupWindow {
    public ModifyVipWindow(Activity activity) {
        super(activity);
    }

    @Override
    public int setContentRes() {
        return R.layout.window_modify_vip;
    }


    private OnDataCallBack<String> mOnDataCallBack;

    public void setOnDataCallBack(OnDataCallBack<String> onDataCallBack) {
        mOnDataCallBack = onDataCallBack;
    }


    @Override
    public void initView(View view) {
//        super.initView(view);
        EditText editText = view.findViewById(R.id.et_name);
        view.findViewById(R.id.tv_add).setOnClickListener(v -> {
            if (mOnDataCallBack != null) {
                String s = editText.getText().toString();
                mOnDataCallBack.onCallBack(s);
            }
        });
        view.findViewById(R.id.tv_less).setOnClickListener(v -> {
            if (mOnDataCallBack != null) {
                String s = editText.getText().toString();
                mOnDataCallBack.onCallBack("-" + s);
            }
        });
        view.findViewById(R.id.tv_fist_recharge).setOnClickListener(v -> {
            if (mOnDataCallBack != null) {
                mOnDataCallBack.onCallBack("fist_recharge");
            }
        });


    }

    @Override
    public boolean setFocusable() {
        return false;
    }
}
