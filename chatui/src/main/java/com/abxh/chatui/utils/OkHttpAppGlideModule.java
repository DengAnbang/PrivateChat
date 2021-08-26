package com.abxh.chatui.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by dab on 2021/4/29 11:03
 */

@GlideModule
public class OkHttpAppGlideModule  extends AppGlideModule {
    private SSLSocketFactory mSSLSocketFactory;
    private X509TrustManager mX509TrustManager;

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        replace(registry);
    }

    private void replace(Registry registry) {
        //信任管理器
        mX509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }
            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        //构建OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            //获取SSL上下文对象
            SSLContext sslContext = SSLContext.getInstance("SSL");
            //初始化信任管理器
            sslContext.init(null, new TrustManager[]{mX509TrustManager}, new SecureRandom());
            //获取Socket工厂
            mSSLSocketFactory = sslContext.getSocketFactory();
            //OkHttp设置Socket工厂
            builder.sslSocketFactory(mSSLSocketFactory, mX509TrustManager);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        //构建OkHttpClient
        OkHttpClient okHttpClient = builder.build();
        //替换Glide内部的OkHttpClient
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
