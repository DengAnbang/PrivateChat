package com.hezeyi.privatechat.fragment;


import android.view.View;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseFragment;
import com.xhab.utils.utils.LogUtils;


/**
 * Created by dab on 2021/3/6 14:42
 */
public class AdminFragment extends BaseFragment {

    @Override
    public int viewLayoutID() {
        return R.layout.fragment_admin;
    }
    @Override
    public void onVisibleToUser() {
        super.onVisibleToUser();
        LogUtils.e("onVisibleToUser*****: AdminFragment");
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();
        LogUtils.e("onInvisibleToUser*****: AdminFragment");
    }

    @Override
    public void onFirstVisibleToUser(View view) {

        LogUtils.e("onFirstVisibleToUser*****: AdminFragment");
    }
}
