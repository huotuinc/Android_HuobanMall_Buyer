package com.huotu.partnermall.utils;

import android.text.TextUtils;
import android.webkit.WebView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/10.
 */
public class SignUtil {

    public static String getSecure(String app_key , String app_security , String random) {

        String temp = app_key + random;
        String secure = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(temp.getBytes("utf-8"));
            byte[] sign1 = messageDigest.digest();//加密
            byte[] sign2 = hexStringToByte(app_security);
            byte[] sign = new byte[sign1.length + sign2.length];
            System.arraycopy(sign1, 0, sign, 0, sign1.length);
            System.arraycopy(sign2, 0, sign, sign1.length, sign2.length);
            messageDigest.update(sign);
            byte[] sign3 = messageDigest.digest();
            secure = bytesToHexString( sign3 );
        } catch (UnsupportedEncodingException ex) {
        } catch (NoSuchAlgorithmException ex2) {
        }
        return  secure;
    }

    public static String bytesToHexString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
//        return sb.toString();

        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if(i<0)
                i+= 256;
            if(i<16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    public static byte[] hexStringToByte(String t){
        byte[]buffer= new byte[t.length()/2];
        int m = 0;
        int n = 0;
        int iLen = t.length()/2;
        for (int i = 0; i < iLen; i++){
            m=i*2+1;
            n=m+1;
            buffer[i] = (byte)(Integer.decode("0x"+ t.substring(i*2, m) + t.substring(m,n)) & 0xFF);
        }
        return buffer;
    }

    public static Map<String,String> signHeader(){
        String userid= BaseApplication.single.readMemberId();
        String unionid = BaseApplication.single.readUserUnionId();
        String openId = BaseApplication.single.readOpenId();
        String sign = ObtainParamsMap.SignHeaderString(userid, unionid , openId );

        String hotString= "mobile;"+sign;

        Map<String,String> header= new HashMap<>();
        header.put("Hot", hotString);

        header.put("appversion","2.0.0");

        return header;
    }

    public static String signHeader( String userAgentString ){
        String userid= BaseApplication.single.readMemberId();
        String unionid = BaseApplication.single.readUserUnionId();
        String openId = BaseApplication.single.readOpenId();
        String sign = ObtainParamsMap.SignHeaderString(userid, unionid , openId);
        String userAgent =userAgentString;
        if( TextUtils.isEmpty(userAgent) ) {
            userAgent = "mobile;"+sign;
        }else{
            int idx = userAgent.lastIndexOf(";mobile;hottec:");
            if(idx>=0){
                userAgent = userAgent.substring(0,idx);
            }
            userAgent +=";mobile;"+sign;
        }
        //webView.getSettings().setUserAgentString(userAgent);

        String appversion = BuildConfig.WEBAPPVERSION;
        userAgent +="hotappver="+appversion+";";

        return userAgent;
    }


}
