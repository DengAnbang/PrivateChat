package com.xhab.utils.inteface;

import android.view.View;

/**
 * Created by dab on 2018/4/4 10:07
 */

public interface OnItemLongClickListener<T> {
    boolean onLongClick(View view, int position, T t);
}
