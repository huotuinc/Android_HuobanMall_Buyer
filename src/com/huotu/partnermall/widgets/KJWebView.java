package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.web.KJWebChromeClient;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.PreferenceHelper;

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
    private boolean isLoadProgress = true;//是否加载进度条

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
                        /*if ( newProgress == 100 ) {
                            progressBar_circle.setVisibility ( View.GONE );
                        }
                        else {
                            if ( ! isAdd ) {
                                progressBar_circle = ( RelativeLayout ) LayoutInflater.from (
                                        context
                                                                                            )
                                                                                      .inflate (
                                                                                              R.layout.progress_circle, null
                                                                                               );
                                KJWebView.this.addView (
                                        progressBar_circle, LayoutParams
                                                .FILL_PARENT, LayoutParams
                                                .FILL_PARENT
                                                       );
                                isAdd = true;
                            }
                            progressBar_circle.setVisibility ( View.VISIBLE );
                        }*/
                    }

                    @Override
                    public
                    boolean onJsAlert (
                            WebView view, String url, String message, JsResult result
                                      ) {
                        return super.onJsAlert ( view, url, message, result );
                    }

                    @Override
                    public
                    boolean onJsConfirm ( WebView view, String url, String message, JsResult
                            result ) {
                        return super.onJsConfirm ( view, url, message, result );
                    }

                    @Override
                    public
                    boolean onJsPrompt (
                            WebView view, String url, String message, String
                            defaultValue, JsPromptResult result
                                       ) {
                        return super.onJsPrompt ( view, url, message, defaultValue, result );
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
    public void setAllowFileAccess(boolean flag)
    {
        mWebView.getSettings ().setAllowFileAccess ( flag );
    }
    public void setLoadsImagesAutomatically(boolean flag)
    {
        mWebView.getSettings ().setLoadsImagesAutomatically ( flag );
    }
    public void setLoadWithOverviewMode(boolean flag)
    {
        mWebView.getSettings ().setLoadWithOverviewMode ( flag );
    }
    public void setSaveFormData(boolean flag)
    {
        mWebView.getSettings ().setSaveFormData ( flag );
    }
    public void setSavePassword(boolean flag)
    {
        mWebView.getSettings ().setSavePassword ( flag );
    }
    public void setPluginState(WebSettings.PluginState pluginState)
    {
        if( Build.VERSION.SDK_INT > 8)
        {
            mWebView.getSettings ().setPluginState ( pluginState );
        }

    }
    public void setSupportMultipleWindows(boolean flag)
    {
        mWebView.getSettings ().setSupportMultipleWindows ( flag );
    }
    public void setAppCacheEnabled(boolean flag)
    {
        mWebView.getSettings ().setAppCacheEnabled ( flag );
    }
    public void setDatabaseEnabled(boolean flag)
    {
        mWebView.getSettings ().setDatabaseEnabled ( flag );
    }
    public void setDomStorageEnabled(boolean flag)
    {
        mWebView.getSettings ().setDomStorageEnabled ( flag );
    }

    public void setCacheMode(int value){
        mWebView.getSettings().setCacheMode ( value );
    }

    public void setWebViewClient(WebViewClient value){
        mWebView.setWebViewClient ( value );
    }

    public void loadUrl(String url){
        mWebView.loadUrl ( url );
        PreferenceHelper.writeString ( context, Constants.BASE_INFO, Constants.CURRENT_URL, url );
        KJLoger.i ( url );
    }

    public void goBack()
    {
        mWebView.goBack ( );
    }

    public boolean canGoBack()
    {
        return  mWebView.canGoBack ();
    }

    public void setScrollBarStyle(int style)
    {
        mWebView.setScrollBarStyle ( style );
    }

}