package com.hezeyi.privatechat.fragment;


import android.view.View;

import com.hezeyi.privatechat.R;
import com.xhab.utils.LogUtils;
import com.xhab.utils.base.LazyFragment;


/**
 * Created by dab on 2021/3/6 14:42
 */
public class MeFragment extends LazyFragment {

    @Override
    public int viewLayoutID() {
        return R.layout.fragment_me;
    }
    @Override
    public void onVisibleToUser() {
        super.onVisibleToUser();
        LogUtils.e("onVisibleToUser*****: MeFragment");
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();
        LogUtils.e("onInvisibleToUser*****: MeFragment");
    }

    @Override
    public void onFirstVisibleToUser(View view) {

        LogUtils.e("onFirstVisibleToUser*****: MeFragment");
    }
}
