package com.huotu.android.library.buyer.widget;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

/**
 * 字符串格式化工具
 * Created by Administrator on 2016/1/8.
 */
public class SpanningUtil {
//    public static void setSpanning(String txt ){
//    }
    /**
     * 格式化价格样式1
     * ￥100.11
     * ￥120.22
     * @param tv
     * @param priceStr
     * @param zPriceStr
     * @param color1
     * @param color2
     */
    public static void set_Price_Format1( TextView tv , String priceStr , String zPriceStr , int color1 , int color2){
        String text = "￥" + zPriceStr + "\r\n￥" + priceStr;
        SpannableString spannableString = new SpannableString(text);
        int startIndex = 0;
        int endIndex = 1 + priceStr.length();
        spannableString.setSpan(new ForegroundColorSpan( color1 ), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        startIndex = endIndex + 2;
        endIndex = text.length();
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan( color2 );
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(strikethroughSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 格式化价格样式2
     * ￥100.11 ￥120.22
     * @param tv
     * @param priceStr
     * @param zPriceStr
     * @param color1
     * @param color2
     */
    public static void set_Price_Format2( TextView tv , String priceStr , String zPriceStr , int color1 , int color2){
        String text = "￥" + zPriceStr + " ￥" + priceStr;
        SpannableString spannableString = new SpannableString(text);
        int startIndex = 0;
        int endIndex = 1 + priceStr.length();
        spannableString.setSpan(new ForegroundColorSpan( color1 ), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        startIndex = endIndex + 1;
        endIndex = text.length();
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan( color2 );
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(strikethroughSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }
}
