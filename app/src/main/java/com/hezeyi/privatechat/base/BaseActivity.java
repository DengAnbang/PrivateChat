package com.hezeyi.privatechat.base;

import com.hezeyi.privatechat.R;
import com.xhab.utils.base.BaseUtilActivity;

/**
 * Created by dab on 2021/3/9 15:40
 */
public abstract class BaseActivity extends BaseUtilActivity {
    public void setTitleString(String titleString) {
        setTextViewString(R.id.tv_title, titleString);
    }
}
