package com.xhab.utils.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by dab on 2021/3/6 14:25
 */
public abstract class LazyFragment extends Fragment implements RequestHelperImp{
    private boolean isPrepared = false;//是否已经加载好了
    private boolean isVisibleToMe = false;//自己是否用户可见
    private boolean isHidden = false;//自己当前的状态是否是隐藏了的

    public abstract int viewLayoutID();

    public void onVisibleToUser() {
        onVisibleOrInvisibleToUser(true);
    }

    public void onInvisibleToUser() {
        onVisibleOrInvisibleToUser(false);
    }

    public void onVisibleOrInvisibleToUser(boolean visible) {

    }

    public void onFirstVisibleToUser(View view) {

    }

    private void onVisible() {
        if (isVisibleToMe) return;
        if (isPrepared) {
            onVisibleToUser();
        } else {
            onFirstVisibleToUser(getView());
            isPrepared = true;
        }
        isVisibleToMe = true;
    }

    private void invisible() {
        if (!isVisibleToMe) return;
        onInvisibleToUser();
        isVisibleToMe = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (viewLayoutID() != 0) {
            return inflater.inflate(viewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden) {
            onVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isHidden) {
            invisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.isHidden = hidden;
        if (hidden) {
            invisible();
        } else {
            onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        } else {
            invisible();
        }
    }

    private RequestHelperAgency mRequestHelperAgency;


    @Override
    public RequestHelperAgency initRequestHelper() {
        if (mRequestHelperAgency == null) {
            mRequestHelperAgency = new RequestHelperAgency(getActivity());
        }
        return mRequestHelperAgency;
    }
}
