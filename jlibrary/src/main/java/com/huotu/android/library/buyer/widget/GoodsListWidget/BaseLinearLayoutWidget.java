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
