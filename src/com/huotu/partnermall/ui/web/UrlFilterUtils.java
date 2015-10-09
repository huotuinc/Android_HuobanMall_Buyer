package com.huotu.partnermall.ui.web;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.ui.WebViewActivity;
import com.huotu.partnermall.widgets.KJWebView;

/**
 * 拦截页面操作类
 */
public
class UrlFilterUtils {

    private
    Context context;
    TextView titleView;
    private Handler mHandler;
    private
    BaseApplication application;

    public UrlFilterUtils(Context context, TextView titleView, Handler mHandler, BaseApplication application)
    {
        this.context = context;
        this.titleView = titleView;
        this.mHandler = mHandler;
        this.application = application;
    }

    /**
     * webview拦截url作相应的处理
     * @param view
     * @param url
     * @return
     */
    public boolean shouldOverrideUrlBySFriend(KJWebView view, String url) {
        if(url.contains( Constants.WEB_TAG_NEWFRAME)){
            Intent intentWeb = new Intent(context, WebViewActivity.class);
            intentWeb.putExtra ( Constants.INTENT_URL, url );
            context.startActivity ( intentWeb );
            return false;
        }else if(url.contains(Constants.WEB_TAG_USERINFO)){
            //修改用户信息
            //判断修改信息的类型
            String type = url.substring(url.indexOf("=", 1)+1, url.indexOf("&", 1));
            if(Constants.MODIFY_PSW.equals ( type ))
            {
                //弹出修改密码框
            }
            return false;
        }else if(url.contains(Constants.WEB_TAG_LOGOUT)){
            //处理登出操作

            return false;
        }else if(url.contains(Constants.WEB_TAG_INFO)){
            //处理信息保护
            return false;
        }else if(url.contains(Constants.WEB_TAG_FINISH)){
            if(view.canGoBack())
                view.goBack(null, null, null);

        }else
        {
            view.loadUrl ( url, titleView, mHandler, application );
            return false;
        }
        return false;
    }

}
