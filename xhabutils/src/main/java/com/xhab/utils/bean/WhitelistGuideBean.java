package com.xhab.utils.bean;

import androidx.annotation.DrawableRes;

/**
 * Created by dab on 2021/4/22 11:24
 */
public class WhitelistGuideBean {
    @DrawableRes
    private int resId;

    private String description;

    public WhitelistGuideBean(@DrawableRes int resId, String description) {
        this.resId = resId;
        this.description = description;
    }

    @DrawableRes
    public int getResId() {
        return resId;
    }

    public void setResId(@DrawableRes int resId) {
        this.resId = resId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
