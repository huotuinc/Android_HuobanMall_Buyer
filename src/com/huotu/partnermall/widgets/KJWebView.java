package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.huotu.partnermall.inner.R;

/**
 * 自定义webview控件
 */
public
class KJWebView extends RelativeLayout {

    public static int Circle = 0x01;
    public static int Horizontal = 0x02;

    private Context context;
    private boolean isAdd = false;  //判断是否已经加入进度条
    private WebView mWebView = null;  //
    private RelativeLayout progressBar_circle = null;  //包含圆形进度条的布局
    private int barHeight = 8;  //水平进度条的高



    public KJWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public KJWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public KJWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    private void init(){
        mWebView = new WebView(context);
        this.addView(mWebView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        mWebView.setWebChromeClient (
                new WebChromeClient ( ) {

                    @Override
                    public
                    void onProgressChanged ( WebView view, int newProgress ) {
                        // TODO Auto-generated method stub
                        super.onProgressChanged ( view, newProgress );
                        if ( newProgress == 100 ) {
                            progressBar_circle.setVisibility ( View.GONE );
                        }
                        else {
                            if ( ! isAdd ) {
                                progressBar_circle = ( RelativeLayout ) LayoutInflater.from (
                                        context ).inflate ( R.layout.progress_circle, null );
                                KJWebView.this.addView ( progressBar_circle, LayoutParams
                                                                 .FILL_PARENT, LayoutParams
                                                                 .FILL_PARENT );
                                isAdd = true;
                            }
                            progressBar_circle.setVisibility ( View.VISIBLE );
                        }
                    }
                }
                                    );
    }

    public void setBarHeight(int height){
        barHeight = height;
    }

    public void setClickable(boolean value){
        mWebView.setClickable(value);
    }

    public void setUseWideViewPort(boolean value){
        mWebView.getSettings().setUseWideViewPort ( value );
    }

    public void setSupportZoom(boolean value){
        mWebView.getSettings().setSupportZoom ( value );
    }

    public void setBuiltInZoomControls(boolean value){
        mWebView.getSettings().setBuiltInZoomControls ( value );
    }

    public void setJavaScriptEnabled(boolean value){
        mWebView.getSettings().setJavaScriptEnabled ( value );
    }

    public void setCacheMode(int value){
        mWebView.getSettings().setCacheMode ( value );
    }

    public void setWebViewClient(WebViewClient value){
        mWebView.setWebViewClient ( value );
    }

    public void loadUrl(String url){
        mWebView.loadUrl ( url );
    }

    public void goBack()
    {
        mWebView.goBack ();
    }

    public boolean canGoBack()
    {
        return  mWebView.canGoBack ();
    }

}