package com.huotu.partnermall.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/3/10.
 */
public class SignUtil {
    /**
     *
     * @param map
     * @return
     */
//    public static String Sign(Map<String,String> map){
//        Map<String, String> resultMap = new TreeMap<String, String>();
//        for (Object key : map.keySet()) {
//            resultMap.put(key.toString(), map.get(key));
//        }
//
//        StringBuilder strB = new StringBuilder();
//        for (String key : resultMap.keySet()) {
//            if (!"sign".equals(key) && !TextUtils.isEmpty(resultMap.get(key))) {
//                strB.append("&" + key + "=" + resultMap.get(key));
//            }
//        }
//
//        String toSign = (strB.toString().length() > 0 ? strB.toString().substring(1) : "") + Jlibrary.BizApiSecurity;
//        Logger.i(toSign);
//        String sign="";
//        try {
//            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
//            messageDigest.update(toSign.getBytes("utf-8"));
//            byte[] sign1 = messageDigest.digest();//加密
//            sign = bytesToHexString(sign1).toLowerCase();
//            Logger.i(sign);
//        }catch (NoSuchAlgorithmException ex2) {
//            Logger.e(ex2.getMessage());
//        }catch (UnsupportedEncodingException ex1){
//            Logger.e(ex1.getMessage());
//        }
//        return sign;
//    }

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

}
