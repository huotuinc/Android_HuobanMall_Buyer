package com.huotu.partnermall.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/12/20.
 */

public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return super.canScrollVertically(direction);
    }
}
