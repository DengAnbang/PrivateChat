package com.hezeyi.privatechat.activity.recharge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.base.BaseActivity;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.SpanBuilder;
import com.xhab.utils.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by dab on 2021/4/23 20:53
 */

public class CustomerServicePayActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_pay_customer_service;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("人工充值");
        String pay_id = getIntent().getStringExtra("pay_id");
        ImageView imageView = findViewById(R.id.iv_qr_code);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, Objects.equals(pay_id, "1") ? R.mipmap.pay1 : R.mipmap.pay2));
        SpannableStringBuilder build = SpanBuilder.content("点击图片,保存二维码,转账后,备注好账号,即可充值成功")
                .colorSpan(this, 15, 20, R.color.just_color_FF3859).build();
        setTextViewString(R.id.tv_hint, build);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        click(R.id.iv_qr_code, v -> {
            FunUtils.affirm(this, "确认保存图片到相册吗?", "保存", aBoolean -> {
                if (aBoolean) {
                    Drawable drawable = ((ImageView) findViewById(R.id.iv_qr_code)).getDrawable();
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    String s = saveImageToGallery(this, bitmap);
                    ToastUtil.showToast("已经保存到目录:" + s);
                }
            });
        });
        click(R.id.tv_description, v -> {
            startActivity(new Intent(this, DescriptionActivity.class));
        });
    }

    public static String saveImageToGallery(Context context, Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(Const.FilePath.FileLocalPath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //最后通知图库更新
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//扫描单个文件
        intent.setData(Uri.fromFile(file));//给图片的绝对路径
        context.sendBroadcast(intent);
        return file.getAbsolutePath();
    }
}
