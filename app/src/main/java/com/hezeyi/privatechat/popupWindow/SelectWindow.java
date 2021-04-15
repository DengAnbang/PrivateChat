package com.hezeyi.privatechat.popupWindow;

import android.app.Activity;
import android.view.View;

import com.hezeyi.privatechat.R;
import com.xhab.utils.popupWindow.BasePopupWindow;

/**
 * Created by dab on 2021/4/1 20:24
 */
public class SelectWindow extends BasePopupWindow {
    public SelectWindow(Activity activity) {
        super(activity);
    }

    private View.OnClickListener mOnClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public int setContentRes() {
        return R.layout.window_select;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

            view.findViewById(R.id.tv_scan).setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            });
            view.findViewById(R.id.tv_add_group).setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            });
            view.findViewById(R.id.tv_add_user).setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            });
        }



    @Override
    public int setAnim() {
        return R.style.AnimRightToLeft;
    }

}
