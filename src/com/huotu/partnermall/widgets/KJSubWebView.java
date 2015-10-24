package com.huotu.partnermall.widgets;

import android.content.Context;
import android.renderscript.ScriptIntrinsic;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 自定义webView控件
 */
public
class KJSubWebView extends WebView {

    ScrollInterface scrollWeb;

    public KJSubWebView(Context context)
    {
        super(context);
    }

    public KJSubWebView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }


    public KJSubWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected
    void onScrollChanged ( int l, int t, int oldl, int oldt ) {
        super.onScrollChanged ( l, t, oldl, oldt );
        scrollWeb.onSChanged ( l, t, oldl, oldt );
    }

    public void setOnCustomScroolChangeListener(ScrollInterface scrollWeb){

        this.scrollWeb = scrollWeb;
    }


    public interface ScrollInterface
    {
        public void onSChanged(int l, int t, int oldl, int oldt) ;
    }
}


