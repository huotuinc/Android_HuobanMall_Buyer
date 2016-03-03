package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/1/6.
 */
public class BaseLinearLayoutWidget extends LinearLayout {
    protected int screenWidth;

    public BaseLinearLayoutWidget(Context context) {
        super(context);

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }



    protected void RefreshUI(Message msg){

    }


//    protected void set_Price_Format1( TextView tv , String priceStr , String zPriceStr ){
//        String text = "￥" + priceStr + "\r\n￥" + zPriceStr;
//        SpannableString spannableString = new SpannableString(text);
//        int startIndex = 0;
//        int endIndex = 1 + priceStr.length();
//        spannableString.setSpan(new ForegroundColorSpan(Color.RED), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        startIndex = endIndex + 2;
//        endIndex = text.length();
//        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
//        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.GRAY);
//        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        spannableString.setSpan(strikethroughSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        tv.setText(spannableString);
//    }
//    protected void set_Price_Format2( TextView tv , String priceStr , String zPriceStr , int color1 , int color2){
//        String text = "￥" + priceStr + " ￥" + zPriceStr;
//        SpannableString spannableString = new SpannableString(text);
//        int startIndex = 0;
//        int endIndex = 1 + priceStr.length();
//        spannableString.setSpan(new ForegroundColorSpan( color1 ), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        startIndex = endIndex + 1;
//        endIndex = text.length();
//        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
//        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan( color2 );
//        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        spannableString.setSpan(strikethroughSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        tv.setText(spannableString);
//    }

    public static class ListHandler extends Handler{
        private WeakReference<BaseLinearLayoutWidget> ref;

        public ListHandler(BaseLinearLayoutWidget context) {
            super();
            this.ref = new WeakReference<BaseLinearLayoutWidget>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if( ref.get() == null  ) return;

            ref.get().RefreshUI( msg );

            //super.handleMessage(msg);

        }
    }
}
