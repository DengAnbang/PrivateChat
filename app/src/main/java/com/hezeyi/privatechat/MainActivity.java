package com.hezeyi.privatechat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hezeyi.privatechat.fragment.AdminFragment;
import com.hezeyi.privatechat.fragment.BuddyFragment;
import com.hezeyi.privatechat.fragment.ChatFragment;
import com.hezeyi.privatechat.fragment.MeFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xhab.utils.base.BaseBottomTabUtilActivity;

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

    @Override
    public void initEvent() {
        super.initEvent();
        requestPermissions();
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

                    } else {
                        showSnackBar("请到设置见面打开所需权限!");
//                        gotoHuaweiPermission();
                    }
                });
        addDisposable(subscribe);
        getHangUpPermission("id_108");
    }

    /**
     * 跳转横幅通知权限,详细channelId授予权限
     */
    private void getHangUpPermission(String channelId) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android 8.0及以上
            NotificationChannel channel = mNotificationManager.getNotificationChannel(channelId);//CHANNEL_ID是自己定义的渠道ID
            if (channel.getImportance() == NotificationManager.IMPORTANCE_DEFAULT) {//未开启
                Toast.makeText(this, "请打开横幅通知权限!", Toast.LENGTH_LONG).show();
                // 跳转到设置页面
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= 26) {
                    // android8.0单个channelid设置
                    intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
                } else {
                    // android 5.0以上一起设置
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);
                }
                startActivity(intent);
            }
        }


    }

}
