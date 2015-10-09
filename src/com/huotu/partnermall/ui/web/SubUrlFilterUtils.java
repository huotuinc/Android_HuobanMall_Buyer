package com.huotu.partnermall.ui.web;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.ui.WebViewActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.widgets.KJWebView;

/**
 * 过滤UI类
 */
public
class SubUrlFilterUtils {

    private
    Context  context;
    private
    Activity aty;
    TextView titleView;
    private Handler mHandler;
    private
    BaseApplication application;

    public
    SubUrlFilterUtils ( Activity aty, Context context, TextView titleView, Handler mHandler,
                     BaseApplication application ) {
        this.context = context;
        this.titleView = titleView;
        this.mHandler = mHandler;
        this.application = application;
        this.aty = aty;
    }

    /**
     * webview拦截url作相应的处理
     * @param view
     * @param url
     * @return
     */
    public
    boolean shouldOverrideUrlBySFriend ( KJWebView view, String url) {
        if(url.contains( Constants.WEB_TAG_NEWFRAME)){

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
            //跳转到新界面
            view.loadUrl ( url, titleView, null, application );
            return false;
        }
        return false;
    }
}
