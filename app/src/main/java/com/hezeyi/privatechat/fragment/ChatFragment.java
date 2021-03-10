package com.hezeyi.privatechat.fragment;


import android.view.View;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseFragment;
import com.xhab.utils.utils.LogUtils;

import org.jetbrains.annotations.Nullable;

/**
 * Created by dab on 2021/3/6 14:42
 */
public class ChatFragment extends BaseFragment {

    @Override
    public int viewLayoutID() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onVisibleToUser() {
        super.onVisibleToUser();
        LogUtils.e("onVisibleToUser*****: ChatFragment");
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();
        LogUtils.e("onInvisibleToUser*****: ChatFragment");
    }

    @Override
    public void onFirstVisibleToUser(@Nullable View view) {
        LogUtils.e("onFirstVisibleToUser*****: ChatFragment");
    }
}
