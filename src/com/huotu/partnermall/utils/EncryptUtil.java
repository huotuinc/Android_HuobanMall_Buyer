package com.huotu.partnermall.utils;


import android.util.Base64;
import android.util.Log;

import com.facebook.common.util.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import static android.R.attr.key;
import static android.R.id.message;

public class EncryptUtil {

    private static class Holder {
        private static final EncryptUtil instance = new EncryptUtil();
    }

    private EncryptUtil() {
    }

    public static final EncryptUtil getInstance() {
        return Holder.instance;
    }

    public String encryptMd532(String source) {
        if (null == source || "".equals(source.trim())) {
            return null;
        } else {
            //String t2 = new String(Hex.encodeHex(DigestUtils.md5(source)));
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(source.getBytes("utf-8"));
                byte[] s1 = messageDigest.digest();
                String tem = new String(Hex.encodeHex(s1, false)).toLowerCase();
                Log.i("test>>>>>>>>", tem);
                return tem;
            } catch (UnsupportedEncodingException ex) {
                return "";
            } catch (NoSuchAlgorithmException ex2) {
                return "";
            }
        }
    }


    public String decryptDES(String content , String keyString ){
        try {
            //byte[] bytesrc = convertHexString(content);
            byte[] bytesrc = Base64.decode( content , Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); //cipher = Cipher.getInstance("DES");

            String key = keyString.length()>8? keyString.substring(0,8): keyString;
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte);
        }catch ( Exception ex){
            Log.e(getClass().getName(), ex.getMessage());
            return null;
        }
    }

    private byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

}
