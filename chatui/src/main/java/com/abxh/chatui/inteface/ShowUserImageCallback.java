package com.abxh.chatui.inteface;

import android.widget.ImageView;

import com.abxh.chatui.bean.chat.ChatMessage;

/**
 * Created by dab on 2021/3/30 20:10
 */
public interface ShowUserImageCallback {
    void showImage(ChatMessage item, ImageView imageView);
}
