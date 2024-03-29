package com.abxh.chatui.utils;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.abxh.chatui.R;
import com.abxh.chatui.bean.ImageSize;


public class GlideUtils {
    public static String KEY_API_HOST = "";

    public static void loadChatImage(final Context mContext, String imgUrl, final ImageView imageView) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_img_failed)// 正在加载中的图片
                .error(R.mipmap.default_img_failed); // 加载失败的图片

        Glide.with(mContext)
                .load(imgUrl) // 图片地址
                .apply(options)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        ImageSize imageSize = BitmapUtil.getImageSize(((BitmapDrawable) resource).getBitmap());
                        ViewGroup.LayoutParams imageLP = (ViewGroup.LayoutParams) (imageView.getLayoutParams());
                        if (imageSize != null) {

                            imageLP.width = imageSize.getWidth();
                            imageLP.height = imageSize.getHeight();
                            imageView.setLayoutParams(imageLP);
                            Glide.with(mContext)
                                    .load(resource)
                                    .apply(options) // 参数
                                    .into(imageView);
                        } else {
                            Glide.with(mContext)
                                    .load("")
                                    .apply(options) // 参数
                                    .into(imageView);
                        }

                    }
                });
    }

    public static void loadImage(final Context mContext, String imgUrl, final ImageView imageView) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_img_failed)// 正在加载中的图片
                .error(R.mipmap.default_img_failed); // 加载失败的图片

        Glide.with(mContext)
                .load(imgUrl) // 图片地址
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options)
                .into(imageView);
    }
    public static void loadImage(final Context mContext, String imgUrl, final ImageView imageView,@DrawableRes int placeholder) {
        final RequestOptions options = new RequestOptions()
                .placeholder(placeholder)// 正在加载中的图片
                .error(placeholder); // 加载失败的图片
        imgUrl = KEY_API_HOST + imgUrl;
        Glide.with(mContext)
                .load(imgUrl) // 图片地址
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options)
                .into(imageView);
    }

    public static void loadChatImage(@DrawableRes int imgUrl, final ImageView imageView) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_img_failed)// 正在加载中的图片
                .error(R.mipmap.default_img_failed); // 加载失败的图片

        Glide.with(imageView.getContext())
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options) // 参数
                .into(imageView);
    }

    public static void loadHeadPortrait(String imgUrl, final ImageView imageView, @DrawableRes final int placeholder) {
        if (imageView == null) return;
        imgUrl = KEY_API_HOST + imgUrl;


        final RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .apply(RequestOptions.bitmapTransform(new GreyPicTransform()))
                .placeholder(placeholder)// 正在加载中的图片
                ; // 加载失败的图片
        String replace = imgUrl.replace("\\", "/");
        Glide.with(imageView.getContext())
                .load(replace)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options)

                .into(imageView);
    }

    public static void isOnline(ImageView imageView, boolean online) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(online ? 1 : 0);//饱和度 0灰色 1正常
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }
}
