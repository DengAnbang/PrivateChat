package com.hezeyi.privatechat.net;

import com.hezeyi.privatechat.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by dab on 2021/4/25 13:37
 */

class HttpsUtils {
    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSlSocketFactory(InputStream certificates) {
        SSLContext sslContext = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            Certificate ca;
            try {
                ca = certificateFactory.generateCertificate(certificates);

            } finally {
                certificates.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tmf.getTrustManagers(), null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslContext != null ? sslContext.getSocketFactory() : null;
    }
    public static SSLSocketFactory getSSlSocketFactory() {
        return getSSlSocketFactory(getInputStream());
    }
    //读取证书文件
    public static InputStream getInputStream(){
        InputStream inputStream = null;
        try {
            inputStream = MyApplication.getInstance().getAssets().open("hezeyisoftware.pem");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
