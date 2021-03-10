package com.xhab.utils.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by dab on 2021/3/10 11:12
 */
public class DisplayUtils {
    public static int dp2px(Context context,float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

}
