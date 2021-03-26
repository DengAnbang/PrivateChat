package com.hezeyi.privatechat.base;

import android.view.View;

import com.hezeyi.privatechat.R;
import com.xhab.utils.base.BaseUtilActivity;

import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/3/9 15:40
 */
public abstract class BaseActivity extends BaseUtilActivity {
    public void setTitleString(String titleString) {
        setTextViewString(R.id.tv_title, titleString);
    }

    public void setRightTitleString(String titleString, @Nullable View.OnClickListener l) {
        setTextViewString(R.id.tv_right, titleString);
        click(R.id.tv_right, l);
    }

}
