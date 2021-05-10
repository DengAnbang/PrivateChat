package com.xhab.utils.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


import com.google.android.material.tabs.TabLayout;
import com.xhab.utils.R;

import java.util.ArrayList;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;


/**
 * Created by dab on 2021/3/6 12:09
 */
public abstract class BaseBottomTabUtilActivity extends BaseUtilActivity {

    private ArrayList<Fragment> mFragments;
    private TabLayout mTabLayout;

    /**
     * 获取Fragment的集合
     *
     * @return
     */
    public abstract ArrayList<Fragment> fragments();


    /**
     * 获取底部按钮的样式
     *
     * @return
     */
    @LayoutRes
    public abstract int getTabViewResID(int position);

    /**
     * 修改底部图标的样式
     *
     * @param position   *
     * @param isSelected 是否选中
     */
    public abstract void changeTab(View view, int position, boolean isSelected);


    /**
     * 显示第几个tab(默认显示第一个)
     *
     * @return
     */
    public int showTab() {
        return 0;
    }


    /**
     * 获取Tab 显示的内容
     *
     * @param context  *
     * @param position *
     * @return
     */
    public View getTabView(Context context, int position) {
        return LayoutInflater.from(context).inflate(getTabViewResID(position), null);
    }

    @Override
    public int getContentViewRes() {
        return R.layout.layout_bottom_tab;
    }

    @Override
    public void initView() {
        super.initView();
        mFragments = fragments();
        mTabLayout = findViewById(R.id.bottom_tab_layout);
        addFragment();

    }

    @Override
    public void initEvent() {
        super.initEvent();
        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    TabLayout.Tab tabAt = mTabLayout.getTabAt(i);
                    if (tabAt == null) {
                        continue;
                    }
                    View customView = tabAt.getCustomView();
                    if (customView == null) {
                        continue;
                    }
                    changeTab(customView, i, i == tab.getPosition());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        int showTab = showTab();
        // 提供自定义的布局添加Tab
        for (int i = 0; i < mFragments.size(); i++) {
            View tabView = getTabView(this, i);
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView));
            changeTab(tabView, i, i == showTab);
        }
    }

    public TabLayout.Tab getTabAt(int index) {
        return mTabLayout.getTabAt(index);
    }

    private void addFragment() {
        for (int i = 0; i < mFragments.size(); i++) {
            getSupportFragmentManager().beginTransaction().add(R.id.home_container, mFragments.get(i)).commit();
        }
    }

    private void onTabItemSelected(int position) {
        if (mFragments.size() < position) return;
        for (int i = 0; i < mFragments.size(); i++) {
            if (i == position) {
                getSupportFragmentManager().beginTransaction().show(mFragments.get(i)).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mFragments.get(i)).commit();
            }
        }

    }
}
