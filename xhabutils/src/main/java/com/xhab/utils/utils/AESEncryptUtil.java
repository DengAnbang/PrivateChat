package com.xhab.utils.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by dab on 2021/3/24 11:20
 */
public class AESEncryptUtil {

    /**
     * 加密
     *
     * @param key
     * @param cleartext
     * @return
     */
    public static String encrypt(String key, String cleartext) {
        return cleartext;
//        if (TextUtils.isEmpty(cleartext)) {
//            return cleartext;
//        }
//        try {
//            byte[] result = encrypt(key, cleartext.getBytes());
//            return new String(Base64.encode(result, Base64.DEFAULT));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    /**
     * 解密
     *
     * @param key
     * @param encrypted
     * @return
     */
    public static String decrypt(String key, String encrypted) {
        return encrypted;
//        if (TextUtils.isEmpty(encrypted)) {
//            return encrypted;
//        }
//        try {
//            byte[] enc = Base64.decode(encrypted, Base64.DEFAULT);
//            byte[] result = decrypt(key, enc);
//            return new String(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    private static byte[] encrypt(String key, byte[] clear) throws Exception {
        byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }



    private static byte[] decrypt(String key, byte[] encrypted) throws Exception {
        byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private final static String HEX = "0123456789ABCDEF";
    //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    //AES 加密
    private static final String AES = "AES";
    // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    private static final String SHA1PRNG = "SHA1PRNG";



    /**
     * 对秘钥进行处理
     *
     * @param seed 动态生成的秘钥
     * @return
     * @throws Exception
     */
    private static byte[] getRawKey(byte[] seed) throws Exception {
        String s = Hash.md5(seed);
        return s.getBytes();
    }

}
