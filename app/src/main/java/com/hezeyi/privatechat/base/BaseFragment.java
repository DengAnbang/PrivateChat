package com.hezeyi.privatechat.base;

import android.view.View;
import android.widget.TextView;

import com.abxh.utils.base.LazyFragment;
import com.abxh.utils.utils.LogUtils;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/3/9 18:43
 */
public abstract class BaseFragment extends LazyFragment {

    public void click(@IdRes int res, @Nullable View.OnClickListener l) {
        View view = getView();
        if (view != null) {
            View textView = view.findViewById(res);
            if (textView != null) {
                textView.setOnClickListener(l);
                return;
            }


        }
        LogUtils.e("click*****:调用失败 !!!!!!!");

    }

    public void setTextViewString(@IdRes int res, CharSequence charSequence) {
        View view = getView();
        if (view != null) {
            View textView = view.findViewById(res);
            if (textView instanceof TextView) {
                ((TextView) textView).setText(charSequence);
                return;
            }
        }
        LogUtils.e("setTextViewString*****:调用失败 !!!!!!!");

    }
}
