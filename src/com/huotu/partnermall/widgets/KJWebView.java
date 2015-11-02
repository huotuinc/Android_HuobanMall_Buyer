package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
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
    private KJSubWebView mWebView = null;  //
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

        mWebView = new KJSubWebView(context);
        this.addView(mWebView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    public void setBarHeight(int height){
        barHeight = height;
    }

    public void setClickable(boolean value){
        mWebView.setClickable(value);
    }

    public void setUseWideViewPort(boolean value){
        mWebView.getSettings().setUseWideViewPort(value);
    }

    public void setSupportZoom(boolean value){
        mWebView.getSettings().setSupportZoom(value);
    }

    public void setBuiltInZoomControls(boolean value){
        mWebView.getSettings().setBuiltInZoomControls(value);
    }

    public void setJavaScriptEnabled(boolean value){
        mWebView.getSettings().setJavaScriptEnabled(value);
    }
    public void setAllowFileAccess(boolean flag)
    {
        mWebView.getSettings ().setAllowFileAccess(flag);
    }
    public void setLoadsImagesAutomatically(boolean flag)
    {
        mWebView.getSettings ().setLoadsImagesAutomatically(flag);
    }
    public void setLoadWithOverviewMode(boolean flag)
    {
        mWebView.getSettings ().setLoadWithOverviewMode(flag);
    }
    public void setSaveFormData(boolean flag)
    {
        mWebView.getSettings ().setSaveFormData(flag);
    }
    public void setSavePassword(boolean flag)
    {
        mWebView.getSettings ().setSavePassword(flag);
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
        mWebView.getSettings ().setSupportMultipleWindows(flag);
    }
    public void setAppCacheEnabled(boolean flag)
    {
        mWebView.getSettings ().setAppCacheEnabled(flag);
    }
    public void setDatabaseEnabled(boolean flag)
    {
        mWebView.getSettings ().setDatabaseEnabled(flag);
    }
    public void setDomStorageEnabled(boolean flag)
    {
        mWebView.getSettings ().setDomStorageEnabled(flag);
    }

    public void setCacheMode(int value){
        mWebView.getSettings().setCacheMode(value);
    }

    public void setWebViewClient(WebViewClient value){
        mWebView.setWebViewClient(value);
    }

    public void loadUrl( final Activity aty, final String url, final TextView titleView, final Handler mHandler, final BaseApplication application, final ScrollSwipeRefreshLayout swipeRefreshLayout){
        mWebView.loadUrl(url);
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
                                if(!"商品详情".equals ( title )) {
                                    PageInfoModel pageInfo = new PageInfoModel ( );
                                    pageInfo.setPageTitle ( title );
                                    pageInfo.setPageUrl ( url );
                                    application.titleStack.push ( pageInfo );
                                }if(null != mHandler)
                                {
                                    if(url.contains ( "&back" ) || url.contains ( "?back" ))
                                    {
                                        //application.titleStack.clear ();
                                        mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
                                    }
                                    else {

                                            if ( canGoBack ( ) ) {
                                            mHandler.sendEmptyMessage ( Constants.LEFT_IMG_BACK );
                                        }
                                        else {
                                            mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
                                        }
                                    }
                                }
                            }
                            else {
                                PreferenceHelper.writeString ( context, Constants.BASE_INFO,
                                                               Constants.CURRENT_URL, url );
                                KJLoger.i ( url );
                            }
                        }

                        @Override
                        public void onProgressChanged(WebView view, int newProgress) {

                            if (newProgress == 100) {
                                //隐藏进度条
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            super.onProgressChanged(view, newProgress);
                        }

                        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                            if(null != aty)
                            {
                                HomeActivity.mUploadMessage = uploadMsg;
                                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                i.addCategory(Intent.CATEGORY_OPENABLE);
                                i.setType("*/*");
                                aty.startActivityForResult(Intent.createChooser(i, "File Chooser"), HomeActivity.FILECHOOSER_RESULTCODE);
                            }
                        }

                        public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                            openFileChooser(uploadMsg);
                        }

                        //For Android 4.1
                        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){

                            openFileChooser(uploadMsg);

                        }
                    }
                                        );
        }

    }

    public void goBack(final TextView titleView, Handler mHandler, BaseApplication application)
    {
        mWebView.goBack();
        /*if(null != titleView && !"".equals ( titleView ))
        {
            //先移除栈顶标题
            if(!application.titleStack.isEmpty () && 1 == application.titleStack.size ())
            {
                PageInfoModel pageInfo = application.titleStack.peek ( );
                titleView.setText ( pageInfo.getPageTitle () );
                //mWebView.loadUrl ( pageInfo.getPageUrl ());
            }
            else if(!application.titleStack.isEmpty () && 1 < application.titleStack.size ())
            {
                application.titleStack.pop ();
                PageInfoModel pageInfo = application.titleStack.peek ( );
                titleView.setText ( pageInfo.getPageTitle () );
                //mWebView.loadUrl ( pageInfo.getPageUrl ());
            }
        }*/

        /*if(null != mHandler)
        {
                application.titleStack.pop ( );
                PageInfoModel pageInfo = application.titleStack.peek ( );
                String url =  pageInfo.getPageUrl ();
                if(url.contains ( "&back" ) || url.contains ( "?back" ))
                {
                    mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
                }
                else {
                    if ( canGoBack ( ) ) {
                        mHandler.sendEmptyMessage ( Constants.LEFT_IMG_BACK );
                    }
                    else {
                        mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
                    }
                }
        }*/

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
        mWebView.addJavascriptInterface(jsModel, jsFunc);
    }

    /**
     * 刷新
     */
    public void reload()
    {
        mWebView.reload();
    }


    public int getContentHeight()
    {
       return mWebView.getContentHeight ();
    }

    public float getScale()
    {
        return mWebView.getScale();
    }

    public int getWebHeight()
    {
        return mWebView.getHeight();
    }

    public int getWebScrollY()
    {
        return mWebView.getScrollY();
    }

    //检测web界面滑动界面
    public void setOnCustomScroolChangeListener(KJSubWebView.ScrollInterface scroll)
    {
        mWebView.setOnCustomScroolChangeListener ( scroll );
    }

    //
    public void setWebChromeClient(WebChromeClient webChromeClient)
    {
        mWebView.setWebChromeClient ( webChromeClient );
    }

    public float getWebScaleY()
    {
        return mWebView.getScaleY ();
    }

}