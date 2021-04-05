package com.hezeyi.privatechat.popupWindow;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.hezeyi.privatechat.R;
import com.xhab.utils.inteface.OnDataCallBack;
import com.xhab.utils.popupWindow.BasePopupWindow;

/**
 * Created by dab on 2021/4/1 20:24
 */
public class ModifyNameWindow extends BasePopupWindow {
    public ModifyNameWindow(Activity activity, String name) {
        super(activity);

        EditText editText = getContentView().findViewById(R.id.et_name);
        editText.setText(name);
    }



    private OnDataCallBack<String> mOnDataCallBack;

    public void setOnDataCallBack(OnDataCallBack<String> onDataCallBack) {
        mOnDataCallBack = onDataCallBack;
    }


    @Override
    public int setContentRes() {
        return R.layout.window_modify_name;
    }

    @Override
    public void initView(View view) {
//        super.initView(view);
        EditText editText = view.findViewById(R.id.et_name);

        View viewById = view.findViewById(R.id.tv_submit);
        viewById.setOnClickListener(v -> {
            if (mOnDataCallBack != null) {
                String s = editText.getText().toString();
                mOnDataCallBack.onCallBack(s);
            }
        });


    }

    @Override
    public boolean setFocusable() {
        return false;
    }
}
