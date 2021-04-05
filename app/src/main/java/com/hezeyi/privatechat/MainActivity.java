package com.hezeyi.privatechat;

import android.Manifest;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.activity.account.MeDetailsActivity;
import com.hezeyi.privatechat.activity.account.UserDetailsActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.fragment.AdminFragment;
import com.hezeyi.privatechat.fragment.BuddyFragment;
import com.hezeyi.privatechat.fragment.ChatFragment;
import com.hezeyi.privatechat.fragment.MeFragment;
import com.hezeyi.privatechat.popupWindow.SelectWindow;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xhab.chatui.utils.NotificationManagerUtils;
import com.xhab.utils.base.BaseBottomTabUtilActivity;
import com.xhab.utils.utils.DisplayUtils;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.QRCodeUtils;

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
    private ChatFragment mChatFragment;


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
        mChatFragment = new ChatFragment();
        fragments.add(mChatFragment);
        fragments.add(new BuddyFragment());
        fragments.add(new MeFragment());
        fragments.add(new AdminFragment());
        return fragments;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        requestPermissions();

        TextView rightTitle = findViewById(R.id.tv_right);
        rightTitle.setTextSize(DisplayUtils.dp2px(this, 7));
        rightTitle.setText("+");
        rightTitle.setOnClickListener(v -> {
            SelectWindow selectWindow = new SelectWindow(this);
            selectWindow.setOnClickListener(v1 -> {
                switch (v1.getId()) {
                    case R.id.tv_scan:
                        UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();
                        QRCodeUtils.scanQrCode(this, s -> {
                            LogUtils.e("onFirstVisibleToUser*****: " + s);
                            if (s.equals(userMsgBean.getUser_id())) {
                                Intent intent = new Intent(this, MeDetailsActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(this, UserDetailsActivity.class);
                                intent.putExtra("user_id", s);
                                startActivity(intent);

                            }

                        });
                        break;
                    case R.id.tv_add_group:

                        break;
                    case R.id.tv_add_user:
                        break;
                }
            });
            selectWindow.showAsDropDown(rightTitle, 0, 0, Gravity.START);
//            showPopupWindow(selectWindow);
        });
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        Disposable subscribe = rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.RECORD_AUDIO
        )
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        NotificationManagerUtils.getHangUpPermission(this);
                    } else {
                        showSnackBar("请到设置见面打开所需权限!");
                    }
                });
        addDisposable(subscribe);
    }


}
