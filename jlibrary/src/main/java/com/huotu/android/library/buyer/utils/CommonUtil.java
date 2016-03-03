package com.huotu.android.library.buyer.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/1/11.
 */
public class CommonUtil {
    /*
    判断类是否实现指定接口
     */
    public static boolean isInterface( Class c , String interfaceName){
        Class[] infs = c.getInterfaces();
        if( infs ==null ) return false;
        for( int i=0;i< infs.length;i++){
            if( infs[i].getName().equals( interfaceName )){
                return true;
            }else{
                Class[]face1=infs[i].getInterfaces();
                for(int x=0;x<face1.length;x++)
                {
                    if(face1[x].getName().equals(interfaceName))
                    {
                        return true;
                    }
                    else if(isInterface(face1[x],interfaceName)) {
                        return true;
                    }
                }
            }
        }
        if(null!=c.getSuperclass()) {
            return isInterface(c.getSuperclass(), interfaceName );
        }
        return false;
    }

    public static String FormatDouble(double value ){
        DecimalFormat format = new DecimalFormat("0.00");
        String str = format.format( value );
        return str;
    }
}
