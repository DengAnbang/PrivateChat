package com.xhab.utils.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 *  字符串特定位置开始变换fontsize或者字符颜色工具。
 */

public class SpanBuilder {

    private SpannableStringBuilder builder;
    //不可初始化空。
    private SpanBuilder(){}
    private SpanBuilder(String content) {
        builder = new SpannableStringBuilder(content);
    }

    public static SpanBuilder content(String content) {
        return new SpanBuilder(content);
    }

    public SpanBuilder sizeSpan(int start, int end,int dpSize) {
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(dpSize,true);
        builder.setSpan(sizeSpan, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpanBuilder colorSpan(Context context, int start, int end, int colorRes) {
        builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorRes)), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }
    public SpannableStringBuilder build() {
        return builder;
    }
}
