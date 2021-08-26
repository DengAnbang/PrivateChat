package com.abxh.utils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;

import com.abxh.utils.inteface.OnDataCallBack;

import me.devilsen.czxing.Scanner;
import me.devilsen.czxing.code.BarcodeFormat;
import me.devilsen.czxing.code.BarcodeWriter;
import me.devilsen.czxing.util.BarCodeUtil;
import me.devilsen.czxing.view.ScanActivityDelegate;
import me.devilsen.czxing.view.ScanView;

/**
 * Created by dab on 2021/3/10 10:02
 */
public class QRCodeUtils {
    private static final int CODE_SELECT_IMAGE = 0x520;

//    public static Bitmap createQR(String text, int size, int color, Bitmap logo) {
//
//        return null;
//    }
//
//    public static void scanQrCode(Context context, OnDataCallBack<String> onDataCallBack) {
//
//    }

    public static Bitmap createQR(String text, int size) {
        return createQR(text, size, Color.BLACK, null);
    }

    /**
     * 生成图片
     *
     * @param text  要生成的文本
     * @param size  图片边长
     * @param color 要生成的二维码颜色
     * @param logo  放在中间的logo
     * @return 条码bitmap
     */
    public static Bitmap createQR(String text, int size, int color, Bitmap logo) {
        BarcodeWriter writer = new BarcodeWriter();
        return writer.write(text, size, color, logo);
    }

    public static void scanQrCode(Context context, OnDataCallBack<String> onDataCallBack) {
        //        Resources resources = getResources();
//        List<Integer> scanColors = Arrays.asList(resources.getColor(R.color.scan_side), resources.getColor(R.color.scan_partial), resources.getColor(R.color.scan_middle));

        Scanner.with(context)
//                .setMaskColor(resources.getColor(R.color.mask_color))   // 设置设置扫码框四周颜色
//                .setBorderColor(resources.getColor(R.color.box_line))   // 扫码框边框颜色
                .setBorderSize(BarCodeUtil.dp2px(context, 200))            // 设置扫码框大小
//        .setBorderSize(BarCodeUtil.dp2px(this, 200), BarCodeUtil.dp2px(this, 100))     // 设置扫码框长宽（如果同时调用了两个setBorderSize方法优先使用上一个）
//                .setCornerColor(resources.getColor(R.color.corner))     // 扫码框角颜色
//                .setScanLineColors(scanColors)                          // 扫描线颜色（这是一个渐变颜色）
//        .setHorizontalScanLine()                              // 设置扫码线为水平方向（从左到右）
                .setScanMode(ScanView.SCAN_MODE_TINY)                   // 扫描区域 0：混合 1：只扫描框内 2：只扫描整个屏幕
//        .setBarcodeFormat(BarcodeFormat.EAN_13)                 // 设置扫码格式
                .setTitle("扫描二维码")                               // 扫码界面标题
                .showAlbum(false)                                        // 显示相册(默认为true)
                .setScanNoticeText("扫描二维码")                         // 设置扫码文字提示
                .setFlashLightOnText("打开闪光灯")                       // 打开闪光灯提示
                .setFlashLightOffText("关闭闪光灯")                      // 关闭闪光灯提示
//                .setFlashLightOnDrawable(R.drawable.ic_highlight_blue_open_24dp)       // 闪光灯打开时的样式
//                .setFlashLightOffDrawable(R.drawable.ic_highlight_white_close_24dp)    // 闪光灯关闭时的样式
                .setFlashLightInvisible()                               // 不使用闪光灯图标及提示
//                .continuousScan()                                       // 连续扫码，不关闭扫码界面
                .enableOpenCVDetect(false)                              // 关闭OpenCV探测，避免没有发现二维码也放大的现象，但是这样可能降低扫码的成功率，请结合业务关闭（默认开启）
                .setOnClickAlbumDelegate(new ScanActivityDelegate.OnClickAlbumDelegate() {
                    @Override
                    public void onClickAlbum(Activity activity) {       // 点击右上角的相册按钮
                        Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(albumIntent, CODE_SELECT_IMAGE);
                    }

                    @Override
                    public void onSelectData(int requestCode, Intent data) { // 选择图片返回的数据
                        if (requestCode == CODE_SELECT_IMAGE) {
//                            selectPic(data);
                        }
                    }
                })
                .setOnScanResultDelegate(new ScanActivityDelegate.OnScanDelegate() { // 接管扫码成功的数据
                    @Override
                    public void onScanResult(Activity activity, String result, BarcodeFormat format) {
                        LogUtils.e("onScanResult*****: " + result);
                        onDataCallBack.onCallBack(result);
                    }
                })
                .start();

    }

//    public static void selectPic(Intent data) {
//        // 适当压缩图片
//        Bitmap bitmap = BitmapUtil.getDecodeAbleBitmap(picturePath);
//        // 这个方法因为要做bitmap的变换，所以比较耗时，推荐放到子线程执行
//        CodeResult result = BarcodeReader.getInstance().read(bitmap);
//        if (result == null) {
//            Log.d("Scan >>> ", "no code");
//            return;
//        } else {
//            Log.d("Scan >>> ", result.getText());
//        }
//    }
}
