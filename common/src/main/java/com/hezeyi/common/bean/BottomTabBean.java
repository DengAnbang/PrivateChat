package com.hezeyi.common.bean;

import androidx.fragment.app.Fragment;

/**
 * Created by dab on 2021/9/17 14:02
 */

public class BottomTabBean {
    private String mTabTitle;
    private int mTabRes;
    private int mTabResPressed;
    private Fragment mFragments;

    public BottomTabBean(String tabTitle, int tabRes, int tabResPressed, Fragment fragments) {
        mTabTitle = tabTitle;
        mTabRes = tabRes;
        mTabResPressed = tabResPressed;
        mFragments = fragments;
    }

    public String getTabTitle() {
        return mTabTitle;
    }

    public void setTabTitle(String tabTitle) {
        mTabTitle = tabTitle;
    }

    public int getTabRes() {
        return mTabRes;
    }

    public void setTabRes(int tabRes) {
        mTabRes = tabRes;
    }

    public int getTabResPressed() {
        return mTabResPressed;
    }

    public void setTabResPressed(int tabResPressed) {
        mTabResPressed = tabResPressed;
    }

    public Fragment getFragments() {
        return mFragments;
    }

    public void setFragments(Fragment fragments) {
        mFragments = fragments;
    }
}
