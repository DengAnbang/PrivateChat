package com.hezeyi.privatechat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hezeyi.privatechat.activity.account.SelectUserActivity;
import com.hezeyi.privatechat.activity.account.UserDetailsActivity;
import com.hezeyi.privatechat.activity.chat.ChatGroupAddActivity;
import com.hezeyi.privatechat.fragment.AdminFragment;
import com.hezeyi.privatechat.fragment.BuddyFragment;
import com.hezeyi.privatechat.fragment.ChatFragment;
import com.hezeyi.privatechat.fragment.MeFragment;
import com.hezeyi.privatechat.popupWindow.SelectWindow;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xhab.chatui.utils.NotificationManagerUtils;
import com.xhab.utils.base.BaseBottomTabUtilActivity;
import com.xhab.utils.utils.DisplayUtils;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.QRCodeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.Disposable;


public class MainActivity extends BaseBottomTabUtilActivity {

    private List<String> mTabTitle = Arrays.asList("消息", "通讯录", "我的", "管理");
    private List<Integer> mTabRes = Arrays.asList(R.mipmap.tab_jiaoliu, R.mipmap.tab_tongxunlu, R.mipmap.tab_wode, R.mipmap.tab_shouye);
    private List<Integer> mTabResPressed = Arrays.asList(R.mipmap.tab_jiaoliu1, R.mipmap.tab_tongxunlu1, R.mipmap.tab_wode1, R.mipmap.tab_shouye1);
    private ChatFragment mChatFragment;
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
        if (Objects.equals(MyApplication.getInstance().getUserMsgBean().getPermissions(), "1")) {
            AdminFragment e = new AdminFragment();
            fragments.add(e);
        }

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
                        ensurePermissions();
                    } else {
                        showSnackBar("请到设置见面打开所需权限!");
                    }
                });
        addDisposable(subscribe);
    }

    private void ensurePermissions() {
        //检查是否已经授予权限 桌面启动activity的权限
        if (!Settings.canDrawOverlays(this)) {
            //若未授权则请求权限
            FunUtils.affirm(this, "此应用需要显示在其他应用的上层的权限,以便于及时提醒", "去设置", aBoolean -> {
                if (aBoolean) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            getHangUpPermission(this);
        }
    }

    /**
     * 跳转横幅通知权限,详细channelId授予权限
     */
    public static void getHangUpPermission(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android 8.0及以上
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NotificationManagerUtils.CHANNEL_ID);//CHANNEL_ID是自己定义的渠道ID
            if (channel.getImportance() == NotificationManager.IMPORTANCE_DEFAULT) {
                FunUtils.affirm(context, "此应用需要横幅通知权限的权限,以便于新消息的及时提醒", "去设置", aBoolean -> {
                    if (aBoolean) {
                        //未开启
                        Toast.makeText(context, "请打开横幅通知权限!", Toast.LENGTH_LONG).show();
                        // 跳转到设置页面
                        Intent intent = new Intent();
                        if (Build.VERSION.SDK_INT >= 26) {
                            // android8.0单个channelid设置
                            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotificationManagerUtils.CHANNEL_ID);
                        } else {
                            // android 5.0以上一起设置
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.putExtra("app_package", context.getPackageName());
                            intent.putExtra("app_uid", context.getApplicationInfo().uid);
                        }
                        context.startActivity(intent);
                    }
                });


            }
        }


    }
}
