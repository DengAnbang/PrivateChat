package com.hezeyi.privatechat.activity.account;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;

import com.hezeyi.privatechat.BuildConfig;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MainActivity;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.service.ChatService;
import com.xhab.utils.activity.PrivacyPolicyActivity;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.SPUtils;
import com.xhab.utils.utils.SpanBuilder;
import com.xhab.utils.utils.ToastUtil;

import androidx.core.content.ContextCompat;

/**
 * Created by dab on 2021/3/8 20:36
 */
public class LoginActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_login;
    }

    private boolean isSelect;

    @Override
    public void initView() {
        super.initView();
        click(R.id.tv_submit, view -> checkLogin());
        click(R.id.tv_forget, view -> {
            Intent intent = new Intent(this, ForgetActivity.class);
            startActivity(intent);
        });
        click(R.id.tv_register, view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        String account = SPUtils.getString(Const.Sp.account, "");
        String password = SPUtils.getString(Const.Sp.password, "");
        setTextViewString(R.id.et_account, account);
        setTextViewString(R.id.et_password, password);
        setTextViewString(R.id.tv_version, "版本号:v" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        SpannableStringBuilder build = SpanBuilder.content("勾选表示同意《用户协议》和《隐私政策》")
                .colorSpan(this, 6, 12, R.color.just_color_FF036EB8)
                .colorSpan(this, 14, 19, R.color.just_color_FF036EB8).build();
        ImageView imageView = findViewById(R.id.iv_select);
        imageView.setOnClickListener(v -> {
            isSelect = !isSelect;
            imageView.setImageDrawable(ContextCompat.getDrawable(this, isSelect ? R.mipmap.b3_icon1 : R.mipmap.b3_icon2));
        });
        setTextViewString(R.id.tv_protocol, build);
        click(R.id.tv_protocol, v -> {
            FunUtils.showPrivacy(this, integer -> {
                switch (integer) {
                    case 1:
                        Intent intent = new Intent(this, PrivacyPolicyActivity.class);
                        intent.putExtra("type", 0);
                        startActivity(intent);
                        break;
                    case 2:// showSnackBar("隐私政策");
                        intent = new Intent(this, PrivacyPolicyActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);

                        break;
                    case 4:
                        isSelect = true;
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.b3_icon1));
                        break;
                }
            });
        });

    }

    private void checkLogin() {
        String account = getTextViewString(R.id.et_account);
        String password = getTextViewString(R.id.et_password);
        if (FunUtils.checkIsNullable(account, "请输入账号!")) return;
        if (FunUtils.checkIsNullable(password, "请输入密码!")) return;
        if (!isSelect) {
            ToastUtil.showToast("请先勾选《用户协议》和《隐私政策》");
            return;
        }
        login(account, password);
    }

    private void login(String account, String password) {
        HttpManager.login(account, password, this, userMsgBean -> {
            Intent startIntent = new Intent(this, ChatService.class);
            startIntent.putExtra("userId", userMsgBean.getUser_id());
            startIntent.putExtra("account", account);
            startIntent.putExtra("password", password);
            startService(startIntent);
            MyApplication.getInstance().setUserMsgBean(userMsgBean);
            SPUtils.save(Const.Sp.account, account);
            SPUtils.save(Const.Sp.password, password);
            getUserList();


        });
    }

    private void getUserList() {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, "1", this, userMsgBeans -> {
            MyApplication.getInstance().setFriendUserMsgBeans(userMsgBeans);
            HttpManager.groupSelectList(user_id, this, chatGroupBeans -> {
                MyApplication.getInstance().setChatGroupBeans(chatGroupBeans);
                MyApplication.getInstance().setLock(true);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }


}
