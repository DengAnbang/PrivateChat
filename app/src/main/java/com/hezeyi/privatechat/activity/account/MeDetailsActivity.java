package com.hezeyi.privatechat.activity.account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.popupWindow.ModifyNameWindow;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.utils.activity.SelectPhotoDialog;
import com.xhab.utils.utils.LogUtils;

/**
 * Created by dab on 2021/3/9 20:33
 */
public class MeDetailsActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_me_details;
    }


    @Override
    public void initView() {
        super.initView();
        setTitleString("个人信息");
        UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();
        if (userMsgBean != null) {
            setTwoTextLinearRightText(R.id.ttv_account, userMsgBean.getAccount()).getRightTextView().setGravity(Gravity.RIGHT);
            setTwoTextLinearRightText(R.id.ttv_name, userMsgBean.getUser_name()).getRightTextView().setGravity(Gravity.RIGHT);
            GlideUtils.loadHeadPortrait(userMsgBean.getHead_portrait(), findViewById(R.id.iv_head_portrait), userMsgBean.getPlaceholder());
            click(R.id.ttv_name, view -> {
                ModifyNameWindow modifyNameWindow = new ModifyNameWindow(this, userMsgBean.getUser_name());

                modifyNameWindow.setOnDataCallBack(s -> {
                    if (TextUtils.isEmpty(s)) {
                        showSnackBar("名字不能为空");
                        return;
                    }
                    HttpManager.userUpdate(userMsgBean.getAccount(), "", s, "", this, userMsgBean1 -> {
                        userMsgBean.setUser_name(s);
                        showSnackBar("修改完成");
                        initView();
                        modifyNameWindow.dismiss();
                    });
                });
                modifyNameWindow.showAsDropDown(findViewById(R.id.ttv_name),0,0,Gravity.END);
            });
        }
        click(R.id.ttv_qr, view -> {
            Intent intent = new Intent(this, MeQrCodeActivity.class);
            startActivity(intent);
        });


        click(R.id.iv_head_portrait, view -> {
            startActivityForResult(new Intent(this, SelectPhotoDialog.class), 55);
//
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 55 && data != null) {
            String stringExtra = data.getStringExtra(SelectPhotoDialog.DATA);
            UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();

            HttpManager.fileUpload(Const.FilePath.userFileType, stringExtra, this, s -> {
                HttpManager.userUpdate(userMsgBean.getAccount(), "", "", s, this, userMsgBean1 -> {
                    userMsgBean.setHead_portrait(s);
                    initView();
                    showSnackBar("修改完成");
                });
            });

            LogUtils.e("onActivityResult*****: " + stringExtra);

        }
    }
}
