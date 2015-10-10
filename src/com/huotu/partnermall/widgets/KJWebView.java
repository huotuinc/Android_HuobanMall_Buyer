package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
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
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.PageInfoModel;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.web.KJWebChromeClient;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.PreferenceHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        init ( );
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
                                                                                              R
                                                                                              .layout.progress_circle, null
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
                    boolean onJsConfirm (
                            WebView view, String url, String message, JsResult
                            result
                                        ) {
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

    public String StringstringByEvaluatingJavaScriptFromString(String script)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try{
                Field mp = WebView.class.getDeclaredField("mProvider");
                mp.setAccessible(true);
                Object webViewObject = mp.get(this);
                Field wc = webViewObject.getClass().getDeclaredField("mWebViewCore");
                wc.setAccessible(true);
                Object webViewCore = wc.get(webViewObject);
                Field bf = webViewCore.getClass().getDeclaredField("mBrowserFrame");
                bf.setAccessible(true);
                Object browserFrame = bf.get(webViewCore);
                Method stringByEvaluatingJavaScriptFromString = browserFrame.getClass().getDeclaredMethod ( "stringByEvaluatingJavaScriptFromString", String.class );
                stringByEvaluatingJavaScriptFromString.setAccessible(true);
                Object obj_value = stringByEvaluatingJavaScriptFromString.invoke(browserFrame, script);
                return String.valueOf(obj_value);

            }catch(Exception e) {

                KJLoger.e ( e.getMessage () );
            }
            return null;

        }else{

            try{

                Field[]fields= WebView.class.getDeclaredFields();
                //由webview取到webviewcore
                Field field_webviewcore = WebView.class.getDeclaredField("mWebViewCore");
                field_webviewcore.setAccessible(true);
                Object obj_webviewcore = field_webviewcore.get(this);
                //由webviewcore取到BrowserFrame
                Field field_BrowserFrame = obj_webviewcore.getClass().getDeclaredField("mBrowserFrame");
                field_BrowserFrame.setAccessible(true);
                Object obj_frame = field_BrowserFrame.get(obj_webviewcore);
                //获取BrowserFrame对象的stringByEvaluatingJavaScriptFromString方法
                Method method_stringByEvaluatingJavaScriptFromString = obj_frame.getClass().getMethod ( "stringByEvaluatingJavaScriptFromString", String.class );
                //执行stringByEvaluatingJavaScriptFromString方法
                Object obj_value = method_stringByEvaluatingJavaScriptFromString.invoke( obj_frame, script);
                //返回执行结果
                return String.valueOf(obj_value);
            }catch(Exception e) {

                KJLoger.e ( e.getMessage () );
            }
            return null;
        }
    }

    public void setPageTitle(final TextView titleView)
    {
        mWebView.setWebChromeClient ( new WebChromeClient ()
                                      {
                                          @Override
                                          public
                                          void onReceivedTitle ( WebView view, String title ) {
                                              super.onReceivedTitle ( view, title );
                                              titleView.setText ( title );
                                          }
                                      });
    }

    public void setBarHeight(int height){
        barHeight = height;
    }

    public void setClickable(boolean value){
        mWebView.setClickable ( value );
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

    public void loadUrl( final String url, final TextView titleView, Handler mHandler, final BaseApplication application){
        mWebView.loadUrl ( url );
        if(null != titleView && !"".equals ( titleView ))
        {
            mWebView.setWebChromeClient (
                    new WebChromeClient ( ) {
                        @Override
                        public
                        void onReceivedTitle ( WebView view, String title ) {
                            super.onReceivedTitle ( view, title );
                            titleView.setText ( title );
                            if ( null != application ) {
                                //加入标题队列
                                PageInfoModel pageInfo = new PageInfoModel ( );
                                pageInfo.setPageTitle ( title );
                                pageInfo.setPageUrl ( url );
                                application.titleStack.push ( pageInfo );
                            }
                            else {
                                PreferenceHelper.writeString ( context, Constants.BASE_INFO,
                                                               Constants.CURRENT_URL, url );
                                KJLoger.i ( url );
                            }
                        }
                    }
                                        );
        }
        if(null != mHandler)
        {
            if(canGoBack())
            {
                mHandler.sendEmptyMessage ( Constants.LEFT_IMG_BACK );
            }
            else
            {
                mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
            }

        }

    }

    public void goBack(final TextView titleView, Handler mHandler, BaseApplication application)
    {
        mWebView.goBack ( );
        if(null != titleView && !"".equals ( titleView ))
        {
            //先移除栈顶标题
            if(!application.titleStack.isEmpty () && 1 == application.titleStack.size ())
            {
                PageInfoModel pageInfo = application.titleStack.peek ( );
                titleView.setText ( pageInfo.getPageTitle () );
            }
            else if(!application.titleStack.isEmpty () && 1 < application.titleStack.size ())
            {
                application.titleStack.pop ();
                PageInfoModel pageInfo = application.titleStack.peek ( );
                titleView.setText ( pageInfo.getPageTitle () );
            }
        }

        if(null != mHandler)
        {
            mHandler.sendEmptyMessage ( Constants.LEFT_IMG_BACK );
        }

    }

    public boolean canGoBack()
    {
        return  mWebView.canGoBack ();
    }

    public void setScrollBarStyle(int style)
    {
        mWebView.setScrollBarStyle ( style );
    }

    public void addJavascriptInterface(HomeActivity.JSModel jsModel, String jsFunc)
    {
        mWebView.addJavascriptInterface ( jsModel, jsFunc );
    }

}