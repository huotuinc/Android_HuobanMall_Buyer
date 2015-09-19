package com.huotu.partnermall.ui.web;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;

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
    private KJWebView viewPage;

    public UrlFilterUtils(Context context, KJWebView viewPage)
    {
        this.context = context;
        this.viewPage = viewPage;
    }

    /**
     * webview拦截url作相应的处理
     * @param view
     * @param url
     * @return
     */
    public boolean shouldOverrideUrlBySFriend(WebView view, String url) {
        if(url.contains( Constants.WEB_TAG_NEWFRAME)){
            Intent intentWeb = new Intent(context, WebViewActivity.class);
            intentWeb.putExtra ( Constants.INTENT_URL, url );
            context.startActivity ( intentWeb );
            return true;
        }else if(url.contains(Constants.WEB_TAG_USERINFO)){
            //修改用户信息
            //判断修改信息的类型
            String type = url.substring(url.indexOf("=", 1)+1, url.indexOf("&", 1));
            if(Constants.MODIFY_PSW.equals ( type ))
            {
                //弹出修改密码框

            }
            return true;
        }else if(url.contains(Constants.WEB_TAG_LOGOUT)){
            //处理登出操作

            return true;
        }else if(url.contains(Constants.WEB_TAG_INFO)){
            //处理信息保护
            return true;
        }else if(url.contains(Constants.WEB_TAG_FINISH)){
            if(viewPage.canGoBack())
                viewPage.goBack();
        }else
        {
            viewPage.loadUrl ( url );
        }
        return false;
    }

}
