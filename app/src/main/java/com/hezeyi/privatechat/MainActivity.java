package com.hezeyi.privatechat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.fragment.ChatFragment;
import com.hezeyi.privatechat.inteface.OnDataClick;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.LogUtils;
import com.xhab.utils.base.BaseBottomTabActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;


public class MainActivity extends BaseBottomTabActivity {

    private List<String> mTabTitle = Arrays.asList("消息", "通讯录", "我的", "管理");
    private List<Integer> mTabRes = Arrays.asList(R.mipmap.tab_jiaoliu, R.mipmap.tab_tongxunlu, R.mipmap.tab_wode, R.mipmap.tab_shouye);
    private List<Integer> mTabResPressed = Arrays.asList(R.mipmap.tab_jiaoliu1, R.mipmap.tab_tongxunlu1, R.mipmap.tab_wode1, R.mipmap.tab_shouye1);

    @Override
    public ArrayList<Fragment> fragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChatFragment());
        fragments.add(new ChatFragment());
        fragments.add(new ChatFragment());
        fragments.add(new ChatFragment());

        return fragments;
    }


    @Override
    public int getTabViewResID(int position) {
        return R.layout.layout_bottom_tab_view;
    }

    @Override
    public void changeTab(View view, int position, boolean isSelected) {
        int tabIconRes;
//        int color;

        if (isSelected) {
            tabIconRes = mTabResPressed.get(position);
//            color = R.color.color_4864f3
        } else {
            tabIconRes = mTabRes.get(position);
//            color = R.color.color_d8c7f2
        }
        ImageView tabIcon = view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(tabIconRes);
        TextView tabText = view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle.get(position));
//        tabText.setTextColor(ContextCompat.getColor(this, color));
    }

    @Override
    public void initData() {
        super.initData();
        HttpManager.updatesCheck(BuildConfig.VERSION_CODE, BuildConfig.FLAVOR, this, new OnDataClick<Object>() {
            @Override
            public void onClick(Object o) {
                LogUtils.e("onClick*****: " + o);
            }
        });
    }
}
