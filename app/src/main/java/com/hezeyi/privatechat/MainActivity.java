package com.hezeyi.privatechat;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.activity.LockActivity;
import com.hezeyi.privatechat.activity.account.SelectUserActivity;
import com.hezeyi.privatechat.activity.account.UserDetailsActivity;
import com.hezeyi.privatechat.activity.chat.ChatGroupAddActivity;
import com.hezeyi.privatechat.fragment.AdminFragment;
import com.hezeyi.privatechat.fragment.BuddyFragment;
import com.hezeyi.privatechat.fragment.ChatFragment;
import com.hezeyi.privatechat.fragment.MeFragment;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.popupWindow.SelectWindow;
import com.xhab.utils.base.BaseBottomTabUtilActivity;
import com.xhab.utils.utils.DisplayUtils;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.QRCodeUtils;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.SPUtils;
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
    private SelectWindow mSelectWindow;


    @Override
    public int getTabViewResID(int position) {
        return R.layout.layout_bottom_tab_view;
    }

    @Override
    public int getContentViewRes() {
        return R.layout.layout_bottom_tab;
    }

    @Override
    public void canLiftClickFinish() {
    }

    @Override
    public void changeTab(View view, int position, boolean isSelected) {
        int tabIconRes;
        int color;
        if (isSelected) {
            tabIconRes = mTabResPressed.get(position);
            color = R.color.just_color_btn_end;
            switch (position) {
                case 0:
                    setTitleString("聊天");
                    break;
                case 1:
                    setTitleString("通讯录");
                    break;
                case 2:
                    setTitleString("我的");
                    break;
                case 3:
                    setTitleString("管理页面");
                    break;
            }
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
    public ArrayList<Fragment> fragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ChatFragment chatFragment = new ChatFragment();
        fragments.add(chatFragment);
        fragments.add(new BuddyFragment());
        fragments.add(new MeFragment());
        if (MyApplication.getInstance().getUserMsgBean().isAdmin()) {
            AdminFragment e = new AdminFragment();
            fragments.add(e);
        }

        return fragments;
    }

    public void showRedPrompt(int index, boolean show) {
        if (index == 1) {
            MyApplication.getInstance().setHasNewFriend(show);
        }
        getTabAt(index).getCustomView().findViewById(R.id.iv_prompt).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtils.e("initData*****: setLock"  );
        MyApplication.getInstance().setLock(true);
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, "2", false, this, userMsgBeans -> {
            boolean show = userMsgBeans.size() > 0;
            showRedPrompt(1, show);
        });
        Disposable subscribe = RxBus.get().register(Const.RxType.TYPE_SHOW_FRIEND_RED_PROMPT, Integer.class).subscribe(integer -> {
            showRedPrompt(1, integer != 0);
        });
        addDisposable(subscribe);
    }


    @Override
    public void initEvent() {
        super.initEvent();
        String string = SPUtils.getString(Const.Sp.SecurityCode + MyApplication.getInstance().getUserMsgBean().getUser_id(), "");
        if (TextUtils.isEmpty(string)) {
            ToastUtil.showToast("未设置安全码,请先设置!");
            Intent intent = new Intent(this, LockActivity.class);
            intent.putExtra("isSetUp", true);
            startActivityForResult(intent, 0x88);
        }
        TextView rightTitle = findViewById(R.id.tv_right);
        rightTitle.setTextSize(DisplayUtils.dp2px(this, 9));
        rightTitle.setText("+");
        rightTitle.setOnClickListener(v -> {
            mSelectWindow = new SelectWindow(this);
            mSelectWindow.setOnClickListener(v1 -> {
                switch (v1.getId()) {
                    case R.id.tv_scan:
                        QRCodeUtils.scanQrCode(this, s -> {
                            Intent intent = new Intent(this, UserDetailsActivity.class);
                            intent.putExtra("user_id", s);
                            startActivity(intent);
                        });
                        break;
                    case R.id.tv_add_group:
                        mSelectWindow.dismiss();
                        Intent intent = new Intent(this, ChatGroupAddActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.tv_add_user:
                        mSelectWindow.dismiss();
                        intent = new Intent(this, SelectUserActivity.class);
                        startActivity(intent);
                        break;
                }
            });
            mSelectWindow.showAsDropDown(rightTitle, 0, 0, Gravity.START);
//            showPopupWindow(selectWindow);
        });
    }

    //点击返回键返回桌面而不是退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
