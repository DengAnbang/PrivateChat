package com.abxh.utils.popupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.abxh.utils.R;

import androidx.annotation.LayoutRes;

/**
 * Created by dab on 2021/3/31 21:53
 */
public abstract class BasePopupWindow extends PopupWindow {
    private static final String TAG = "BasePopupWindow";

    @LayoutRes
    public abstract int setContentRes();

    private Activity activity;

    public int setBackgroundColor() {
        return Color.TRANSPARENT;
    }

    public float setBackgroundAlpha() {
        return 0.7f;
    }

    public boolean setFocusable() {
        return true;
    }

    //是否点击外部消失
    public boolean setClickDismiss() {
        return true;
    }

    public int setAnim() {
        return R.style.AnimBottomToTop;
    }

    public void initView(View view) {
        getContentView().setOnClickListener(v -> {
            if (setClickDismiss()) {
                dismiss();
            }
        });
    }

    public void initEvent() {

    }

    public BasePopupWindow(Activity activity) {
        this.activity = activity;
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = layoutInflater.inflate(setContentRes(), null);
        this.setContentView(inflate);
        //设置SelectPicPopupWindow的View
        setContentView(inflate);
        //设置SelectPicPopupWindow弹出窗体的宽
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        if (setAnim() != 0) {
            setAnimationStyle(setAnim());
        }
        //实例化一个ColorDrawable颜色为全透明
        ColorDrawable colorDrawable = new ColorDrawable(setBackgroundColor());
//        ColorDrawable dw = new ColorDrawable(Constant.COLOR_CONFIRM_DELETE_POPUP_WINDOW);
//        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(colorDrawable);
        changeWindowAlpha(activity.getWindow(), setBackgroundAlpha());

        initView(inflate);
        initEvent();
    }

    private void changeWindowAlpha(Window window, Float alpha) {
        if (window == null) {
            Log.e(TAG, "changeWindowAlpha: window == null");
            return;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        window.setAttributes(attributes);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        changeWindowAlpha(activity.getWindow(), 1f);

    }
}
