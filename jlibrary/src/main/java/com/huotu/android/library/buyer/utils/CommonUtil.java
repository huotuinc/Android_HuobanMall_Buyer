package com.huotu.android.library.buyer.utils;

import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.huotu.android.library.buyer.bean.Data.ScrollEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static String formatDouble(double value ){
        DecimalFormat format = new DecimalFormat("0.##");
        String str = format.format(value);
        return str;
    }

    /**
     * 格式化 会员价 数组 ,取第一个值
     * @param prices
     * @return
     */
    public static String formatPrice(List<Double> prices){
        if( prices==null|| prices.size()<1) return "";
        int count = prices.size();
        if(count>=1) return String.valueOf(prices.get(0));
        return "";
    }

    /**
     * 格式化 积分 数组
     * @param scores
     * @return
     */
    public static String formatJiFen(List<Integer> scores){
        if( scores==null|| scores.size()<1) return "0";
        int count = scores.size();
        if(count==1) return String.valueOf(scores.get(0));
        if(count>1) {
            if( scores.get(0).equals( scores.get(1) )){
                String s1 = String.valueOf(scores.get(0));
                return s1;
            }else {
                return scores.toString();
            }
        }
        return "0";
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     */
    public static <T> T convertMap(T thisObj, Map map) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        return (T) gson.fromJson( jsonString , thisObj.getClass() );
    }

    /**
     * 设置 SimpleDraweeView 控件的默认宽度和高度
     * @param width
     */
    public static void setSimpleDraweeViewWidthHeight( SimpleDraweeView iv , int width){
        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        iv.setLayoutParams(layoutParams);
    }
}
