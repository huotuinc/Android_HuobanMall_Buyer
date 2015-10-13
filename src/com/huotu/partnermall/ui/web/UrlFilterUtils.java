package com.huotu.partnermall.ui.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.ui.WebViewActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.widgets.KJWebView;
import com.huotu.partnermall.widgets.PayPopWindow;

/**
 * 拦截页面操作类
 */
public
class UrlFilterUtils {

    private
    Context context;
    private
    Activity aty;
    TextView titleView;
    private Handler mHandler;
    private
    BaseApplication application;

    public UrlFilterUtils(Activity aty, Context context, TextView titleView, Handler mHandler, BaseApplication application)
    {
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
    public boolean shouldOverrideUrlBySFriend(KJWebView view, String url) {
        if(url.contains( Constants.WEB_TAG_NEWFRAME)){
            /*String urlStr = url.substring ( 0, url.indexOf ( Constants.WEB_TAG_NEWFRAME ) );
            view.loadUrl ( urlStr, titleView, null, null );
            return false;*/
            String urlStr = url.substring ( 0, url.indexOf ( Constants.WEB_TAG_NEWFRAME ) );
            Bundle bundle = new Bundle (  );
            bundle.putString ( Constants.INTENT_URL, urlStr );
            ActivityUtils.getInstance ().showActivity ( aty,  WebViewActivity.class, bundle);
            return true;
        }
        else if ( url.contains ( Constants.WEB_CONTACT ) )
        {
            //拦截客服联系
            //获取QQ号码
            String qq = url.substring ( 0, url.indexOf ( "&version=" ));
            //调佣本地的QQ号码
            context.startActivity ( new Intent ( Intent.ACTION_VIEW, Uri.parse ( qq ) ) );
            return true;
        }
        else if(url.contains(Constants.WEB_TAG_USERINFO)){
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
            if(view.canGoBack())
                view.goBack(null, null, null);

        }
        else if(url.contains ( Constants.WEB_PAY ) )
        {
            //支付模块

        }
        else
        {
            //跳转到新界面
            /*Bundle bundle = new Bundle (  );
            bundle.putString ( Constants.INTENT_URL, url );
            ActivityUtils.getInstance ().showActivity ( aty,  WebViewActivity.class, bundle);
            return true;*/
            view.loadUrl ( url, titleView, null, null );
            return false;
        }
        return false;
    }

}
