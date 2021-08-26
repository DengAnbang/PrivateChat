package com.abxh.utils.bean;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * Created by dab on 2017/8/17 0017 09:32
 * 可以根据首字母排序的类
 */

public abstract class Sortable implements Comparable<Sortable> {
    private String pinyin = "";

    @NonNull
    public abstract String getSortableString();

    //获取拼音
    public String getPinyin() {
        if (TextUtils.isEmpty(pinyin)) {
            try {
                return PinyinHelper.getShortPinyin(getSortableString()).toUpperCase();
            } catch (PinyinException e) {
                e.printStackTrace();
            }
        }

        return pinyin;
    }

    /**
     * 获取首字母
     *
     * @return
     */
    public String getInitial() {
        if (TextUtils.isEmpty(pinyin)) {
            pinyin = getPinyin();
        }
        if (TextUtils.isEmpty(pinyin)) return "";
        return pinyin.replace(" ", "").substring(0, 1);
    }

    @Override
    public int compareTo(@NonNull Sortable bopomofoSortable) {
        int c1 = getInitial().hashCode();
        int c2 = bopomofoSortable.getInitial().hashCode();
        boolean c1Flag = (c1 < "A".hashCode() || c1 > "Z".hashCode()); // 不是字母
        boolean c2Flag = (c2 < "A".hashCode() || c2 > "Z".hashCode()); // 不是字母
        if (c1Flag && !c2Flag) {
            return 1;
        } else if (!c1Flag && c2Flag) {
            return -1;
        }

        return c1 - c2;
    }


}
