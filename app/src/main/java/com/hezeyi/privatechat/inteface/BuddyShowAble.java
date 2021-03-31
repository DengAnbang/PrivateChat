package com.hezeyi.privatechat.inteface;

import androidx.annotation.DrawableRes;

/**
 * Created by dab on 2021/3/26 09:57
 */
public interface BuddyShowAble {
    String getShowName();

    String getId();

    String getShowPortrait();

    @DrawableRes
    int getPlaceholder();
}
