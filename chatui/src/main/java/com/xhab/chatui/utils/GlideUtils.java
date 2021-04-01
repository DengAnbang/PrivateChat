package com.xhab.chatui.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xhab.chatui.R;
import com.xhab.chatui.bean.ImageSize;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class GlideUtils {
    public static String KEY_API_HOST = "";

    public static void loadChatImage(final Context mContext, String imgUrl, final ImageView imageView) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_img_failed)// 正在加载中的图片
                .error(R.mipmap.default_img_failed); // 加载失败的图片

        Glide.with(mContext)
                .load(imgUrl) // 图片地址
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        ImageSize imageSize = BitmapUtil.getImageSize(((BitmapDrawable) resource).getBitmap());
                        RelativeLayout.LayoutParams imageLP = (RelativeLayout.LayoutParams) (imageView.getLayoutParams());
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
                .apply(options)
                .into(imageView);
    }

    public static void loadChatImage(@DrawableRes int imgUrl, final ImageView imageView) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_img_failed)// 正在加载中的图片
                .error(R.mipmap.default_img_failed); // 加载失败的图片

        Glide.with(imageView.getContext())
                .load(imgUrl)
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
                .placeholder(placeholder)// 正在加载中的图片
                ; // 加载失败的图片
        String replace = imgUrl.replace("\\", "/");
        Glide.with(imageView.getContext())
                .load(replace)
                .apply(options)
                .into(imageView);
    }

}
