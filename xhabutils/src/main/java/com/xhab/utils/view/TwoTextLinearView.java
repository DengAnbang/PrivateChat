package com.xhab.utils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhab.utils.R;

/**
 * Created by dab on 2021/3/9 18:13
 */
public class TwoTextLinearView extends LinearLayout {
    private TextView leftView;
    private TextView rightView;
    private ColorStateList leftColor;
    private ColorStateList rightColor;

    public TwoTextLinearView(Context context) {
        super(context);
        init(context, null);
    }

    public TwoTextLinearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TwoTextLinearView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
//        int padding = dp2px(getContext(), 16);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        setPadding(padding, padding, padding, padding);
        setBackgroundColor(Color.WHITE);
        setOrientation(HORIZONTAL);
        leftView = new TextView(getContext());
        leftView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        leftView.setSingleLine(true);
        leftView.setGravity(Gravity.CENTER_VERTICAL);
        rightView = new TextView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = padding;
        rightView.setLayoutParams(params);
        //以下此3句等同singleLine。
        rightView.setMaxLines(1);
        rightView.setEllipsize(TextUtils.TruncateAt.END);
        rightView.setHorizontallyScrolling(true);
        rightView.setGravity(Gravity.CENTER);

        CharSequence leftText = null;
        CharSequence rightText = null;
        int leftTextSize = 15;
        int rightTextSize = 15;
        int gravity = Gravity.LEFT;
        int drawableLeft = 0;
        int drawableRight = 0;
        int drawablePadding = 0;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TwoTextLinearView);
            leftColor = a.getColorStateList(R.styleable.TwoTextLinearView_leftTextColor);
            rightColor = a.getColorStateList(R.styleable.TwoTextLinearView_rightTextColor);
            leftText = a.getText(R.styleable.TwoTextLinearView_leftText);
            rightText = a.getText(R.styleable.TwoTextLinearView_rightText);
            leftTextSize = a.getDimensionPixelSize(R.styleable.TwoTextLinearView_leftTextSize, 15);
            rightTextSize = a.getDimensionPixelSize(R.styleable.TwoTextLinearView_rightTextSize, 15);
            gravity = a.getInteger(R.styleable.TwoTextLinearView_rightGravity, Gravity.LEFT);
            drawableLeft = a.getResourceId(R.styleable.TwoTextLinearView_leftDrawable, 0);
            drawableRight = a.getResourceId(R.styleable.TwoTextLinearView_rightDrawable, 0);
            drawablePadding = a.getDimensionPixelSize(R.styleable.TwoTextLinearView_drawablePadding, padding / 8);
            a.recycle();
        }

        leftView.setTextColor(leftColor != null ? leftColor : ColorStateList.valueOf(0xFF333333));
        rightView.setTextColor(rightColor != null ? rightColor : ColorStateList.valueOf(0xFFababab));
        rightView.setGravity(gravity);
        if (leftText != null) {
            leftView.setText(leftText);
        }
        rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRight, 0);
        rightView.setCompoundDrawablePadding(drawablePadding);
        leftView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        leftView.setCompoundDrawablePadding(drawablePadding);
        if (rightText != null) {
            rightView.setText(rightText);
        }
        leftView.setTextSize(TypedValue.COMPLEX_UNIT_SP, leftTextSize);
        rightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightTextSize);
        addView(leftView);
        addView(rightView);
    }

    public TextView getLeftTextView() {
        return leftView;
    }

    public TextView getRightTextView() {
        return rightView;
    }

    /**
     * 设置左边文字大小
     *
     * @param size 默认单位sp
     */
    public void setLeftTextSize(int size) {
        leftView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置右边文字大小
     *
     * @param size 默认单位sp
     */
    public void setRightTextSize(int size) {
        rightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置左边文字
     *
     * @param s 文字
     */
    public void setLeftText(CharSequence s) {
        leftView.setText(s);
    }

    /**
     * 设置右边文字
     *
     * @param s 文字
     */
    public void setRightText(CharSequence s) {
        rightView.setText(s);
    }

    /**
     * 获取右边文字
     */
    public CharSequence getRightText() {
        return rightView.getText();
    }

    /**
     * 设置左边文字颜色
     *
     * @param color 颜色
     */
    public void setLeftTextColor(int color) {
        leftView.setTextColor(color);
    }

    /**
     * 设置右边文字颜色
     *
     * @param color 颜色
     */
    public void setRightTextColor(int color) {
        rightView.setTextColor(color);
    }
}
