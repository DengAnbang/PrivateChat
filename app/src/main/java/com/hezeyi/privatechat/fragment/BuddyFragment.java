package com.hezeyi.privatechat.fragment;

import android.view.View;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseFragment;
import com.xhab.utils.utils.LogUtils;


/**
 * Created by dab on 2021/3/9 09:54
 */
public class BuddyFragment extends BaseFragment {
    @Override
    public int viewLayoutID() {
        return R.layout.fragment_buddy;
    }
    @Override
    public void onVisibleToUser() {
        super.onVisibleToUser();
        LogUtils.e("onVisibleToUser*****: BuddyFragment");
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();
        LogUtils.e("onInvisibleToUser*****: BuddyFragment");
    }

    @Override
    public void onFirstVisibleToUser(View view) {

        LogUtils.e("onFirstVisibleToUser*****: BuddyFragment");
    }
}
