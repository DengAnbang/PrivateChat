package com.hezeyi.privatechat.inteface;

import androidx.annotation.DrawableRes;

/**
 * Created by dab on 2021/3/26 09:57
 */
public interface BuddyShowAble {
    String getShowName();

    String getId();

    String getShowPortrait();

    boolean isOnline();//是否在线,显示灰色头像

    boolean isGroup();//是否是群

    @DrawableRes
    int getPlaceholder();
}
