package com.hezeyi.privatechat;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.fragment.AdminFragment;
import com.hezeyi.privatechat.fragment.BuddyFragment;
import com.hezeyi.privatechat.fragment.ChatFragment;
import com.hezeyi.privatechat.fragment.MeFragment;
import com.xhab.utils.StackManager;
import com.xhab.utils.base.BaseBottomTabUtilActivity;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.Disposable;


public class MainActivity extends BaseBottomTabUtilActivity {

    private List<String> mTabTitle = Arrays.asList("消息", "通讯录", "我的", "管理");
    private List<Integer> mTabRes = Arrays.asList(R.mipmap.tab_jiaoliu, R.mipmap.tab_tongxunlu, R.mipmap.tab_wode, R.mipmap.tab_shouye);
    private List<Integer> mTabResPressed = Arrays.asList(R.mipmap.tab_jiaoliu1, R.mipmap.tab_tongxunlu1, R.mipmap.tab_wode1, R.mipmap.tab_shouye1);


    @Override
    public int getTabViewResID(int position) {
        return R.layout.layout_bottom_tab_view;
    }

    @Override
    public int getContentViewRes() {
        return R.layout.layout_bottom_tab;
    }

    @Override
    public void changeTab(View view, int position, boolean isSelected) {
        int tabIconRes;
        int color;

        if (isSelected) {
            tabIconRes = mTabResPressed.get(position);
            color = R.color.just_color_btn_end;
        } else {
            tabIconRes = mTabRes.get(position);
            color = R.color.just_color_999999;
        }
        ImageView tabIcon = view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(tabIconRes);
        TextView tabText = view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle.get(position));
        tabText.setTextColor(ContextCompat.getColor(this, color));
    }


    @Override
    public void initData() {
        super.initData();
        UserMsgBean userMsgBean = DataInMemory.getInstance().getUserMsgBean();
//        String s = ResultData.create("0", Const.RxType.TYPE_LOGIN, userMsgBean).toJson();
//        Message message = new Message();
//        message.setMsgType();
        MyApplication.getInstance().loginSocket(userMsgBean.getUser_id());
//        MyApplication.getInstance().sendSendMsgBean(s);

        Disposable subscribe = RxBus.get().register(Const.RxType.TYPE_OTHER_LOGIN, Object.class).subscribe(o -> {
            ToastUtil.showToast("其他人登录了此账号,请重新登陆!");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            StackManager.finishExcludeActivity(LoginActivity.class);
        });
    }


    //    @NotNull
//    @Override
//    public Fragment[] fragments() {
//        Fragment[] fragments1 = new Fragment[4];
//        fragments1[0] = new ChatFragment();
//        fragments1[1] = new BuddyFragment();
//        fragments1[2] = new MeFragment();
//        fragments1[3] = new AdminFragment();
////        ArrayList<Fragment> fragments = new ArrayList<>();
////        fragments.add(new ChatFragment());
////        fragments.add(new BuddyFragment());
////        fragments.add(new MeFragment());
////        fragments.add(new AdminFragment());
//
//        return fragments1;
//    }
    @Override
    public ArrayList<Fragment> fragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChatFragment());
        fragments.add(new BuddyFragment());
        fragments.add(new MeFragment());
        fragments.add(new AdminFragment());
        return fragments;
    }

}
