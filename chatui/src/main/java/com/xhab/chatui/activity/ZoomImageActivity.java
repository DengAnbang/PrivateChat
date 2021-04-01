package com.xhab.chatui.activity;

import android.os.Bundle;

import com.xhab.chatui.R;
import com.xhab.chatui.utils.GlideUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by dab on 2021/4/1 10:25
 */
public class ZoomImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        String url = getIntent().getStringExtra("url");
        GlideUtils.loadImage(this, url, findViewById(R.id.ziv_image));
    }
}
