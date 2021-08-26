package com.hezeyi.privatechat.activity.account;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.abxh.utils.utils.DisplayUtils;
import com.abxh.utils.utils.QRCodeUtils;

/**
 * Created by dab on 2021/3/10 10:11
 */
public class MeQrCodeActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_qr_code;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("我的二维码");
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        Bitmap codeBitmap = QRCodeUtils.createQR(user_id, DisplayUtils.dp2px(this, 200));
        ImageView imageView = findViewById(R.id.iv_qr_code);
        imageView.setImageBitmap(codeBitmap);
    }
}
