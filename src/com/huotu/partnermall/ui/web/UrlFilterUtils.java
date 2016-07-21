package com.huotu.partnermall.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.model.BindEvent;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.SwitchUserByUserIDEvent;
import com.huotu.partnermall.ui.WebViewActivity;

import com.huotu.partnermall.ui.login.PhoneLoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.widgets.NoticePopWindow;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * 拦截页面操作类
 */
public class UrlFilterUtils {
    private WeakReference<Activity> ref;
    private Handler mHandler;
    private BaseApplication application;
    public ProgressPopupWindow payProgress;

    public UrlFilterUtils ( Activity aty, Handler mHandler, BaseApplication application  ) {
        this.mHandler = mHandler;
        this.application = application;
        this.ref = new WeakReference<>(aty);
        payProgress = new ProgressPopupWindow ( aty );
    }

    /**
     * webview拦截url作相应的处理
     * @param view
     * @param url
     * @return
     */
    public boolean shouldOverrideUrlBySFriend ( WebView view, String url ) {
        if( ref.get() ==null) return false;

        if ( url.contains ( Constants.WEB_TAG_NEWFRAME ) ) {
            String urlStr = url.substring ( 0, url.indexOf ( Constants.WEB_TAG_NEWFRAME ) );
            Bundle bundle = new Bundle ( );
            bundle.putString ( Constants.INTENT_URL, urlStr );
            ActivityUtils.getInstance ( ).showActivity ( ref.get() , WebViewActivity.class, bundle );
            return true;
        } else if ( url.contains ( Constants.WEB_CONTACT ) ){
            //拦截客服联系
            //获取QQ号码
            String qq = url.substring ( 0, url.indexOf ( "&version=" ));
            //调佣本地的QQ号码
            try{
                ref.get().startActivity ( new Intent ( Intent.ACTION_VIEW, Uri.parse ( qq ) ) );
            } catch ( Exception e ){
                if(e.getMessage ().contains ( "No Activity found to handle Intent" )){
                    NoticePopWindow noticePop = new NoticePopWindow ( ref.get() , "请安装QQ客户端");
                    noticePop.showNotice ();
                    noticePop.showAtLocation ( ref.get().getWindow().getDecorView() , Gravity.CENTER, 0, 0 );
                }
            }
            return true;
        }
        else if(url.contains(Constants.WEB_TAG_USERINFO)){
            //修改用户信息
            //判断修改信息的类型
            return true;
        }else if(url.contains(Constants.WEB_TAG_LOGOUT)){
            //处理登出操作
            //鉴权失效
            //清除登录信息
            application.logout ();

            Uri uri = Uri.parse(url.toLowerCase());
            String redirectURL = uri.getQueryParameter("redirecturl");

            if(!TextUtils.isEmpty( redirectURL )){
                redirectURL = Uri.decode( redirectURL );
            }


            if( !TextUtils.isEmpty( redirectURL ) && !redirectURL.toLowerCase().startsWith("http://") ){
                if( !redirectURL.startsWith("/") ) redirectURL = "/"+ redirectURL;
                redirectURL = uri.getHost() + redirectURL;
            }
            //跳转到登录界面
            Bundle bd = new Bundle();
            bd.putString("redirecturl", redirectURL);
            //跳转到登录界面
            ActivityUtils.getInstance ().showActivity( ref.get() , PhoneLoginActivity.class , bd );
        }else if(url.contains(Constants.WEB_TAG_INFO)){
            //处理信息保护
            return true;
        }else if(url.contains(Constants.WEB_TAG_FINISH)){
            if(view.canGoBack())
                view.goBack();
        }
        else if(url.contains ( Constants.WEB_PAY ) )
        {
            //支付进度
            payProgress.showProgress ( "正在加载支付信息" );
            payProgress.showAtLocation ( ref.get().getWindow().getDecorView(),  Gravity.CENTER, 0, 0 );
            //支付模块
            //获取信息
            //截取问号后面的
            //订单号
            String tradeNo = null;
            String customerID = null;
            String paymentType = null;
            PayModel payModel = new PayModel ();
            url = url.substring ( url.indexOf ( ".aspx?" )+6, url.length () );
            String[] str = url.split ( "&" );
            for(String map : str)
            {
                String[] values = map.split ( "=" );
                if(2 == values.length)
                {
                    if("trade_no".equals ( values[0] ))
                    {
                        tradeNo = values[1];
                        payModel.setTradeNo ( tradeNo );
                    }
                    else if("customerID".equals ( values[0] ))
                    {
                        customerID = values[1];
                        payModel.setCustomId ( customerID );
                    }
                    else if("paymentType".equals ( values[0] ))
                    {
                        paymentType = values[1];
                        payModel.setPaymentType ( paymentType );
                    }
                }
                else
                {
                    Log.i( UrlFilterUtils.class.getName() ,"支付参数出错.");
                }
            }
            //获取用户等级
            StringBuilder builder = new StringBuilder (  );
            builder.append ( Constants.getINTERFACE_PREFIX() + "order/GetOrderInfo" );
            builder.append ( "?orderid="+tradeNo );
            AuthParamUtils param = new AuthParamUtils ( application, System.currentTimeMillis (), builder.toString (), ref.get() );
            String orderUrl = param.obtainUrlOrder ( );
            HttpUtil.getInstance ( ).doVolleyPay ( ref.get() ,  mHandler, application, orderUrl, payModel, payProgress );
            return true;

        }
        else if(url.contains ( Constants.AUTH_FAILURE ) || url.contains( Constants.AUTH_FAILURE_PHONE) )
        {
            //鉴权失效
            //清除登录信息
            application.logout ();


            Uri uri = Uri.parse(url.toLowerCase());
            String redirectURL = uri.getQueryParameter("redirecturl");

            if(!TextUtils.isEmpty( redirectURL )){
                redirectURL = Uri.decode( redirectURL );
            }

            if( !TextUtils.isEmpty( redirectURL ) && !redirectURL.toLowerCase().startsWith("http://") ){
                if( !redirectURL.startsWith("/") ) redirectURL = "/"+ redirectURL;
                redirectURL = uri.getHost() + redirectURL;

                if( !redirectURL.toLowerCase().startsWith("http://") ){
                    redirectURL = "http://"+ redirectURL;
                }
            }
            //跳转到登录界面

            Bundle bd = new Bundle();
            bd.putString("redirecturl", redirectURL);

            ActivityUtils.getInstance ().showActivity ( ref.get() , PhoneLoginActivity.class , bd);
            return true;
        }else if( url.toLowerCase().contains( Constants.URL_BINDINGWEIXING.toLowerCase() )){//拦截绑定微信url

            Uri uri = Uri.parse(url.toLowerCase());
            String redirectURL = uri.getQueryParameter("redirecturl");

            if(!TextUtils.isEmpty( redirectURL )){
                redirectURL = Uri.decode( redirectURL );
            }

            if( !TextUtils.isEmpty( redirectURL ) && !redirectURL.toLowerCase().startsWith("http://") ){
                if( !redirectURL.startsWith("/") ) redirectURL = "/"+ redirectURL;
                redirectURL = uri.getHost() + redirectURL;

                if( !redirectURL.toLowerCase().startsWith("http://") ){
                    redirectURL = "http://"+ redirectURL;
                }

            }

            EventBus.getDefault().post(new BindEvent(true , redirectURL ));
            return true;
        }
        else if(url.toLowerCase().contains(Constants.URL_APPACCOUNTSWITCHER.toLowerCase())){//切换帐号 url
            String userId= Uri.parse(url).getQueryParameter("u");
            EventBus.getDefault().post(new SwitchUserByUserIDEvent(userId));
            return true;
        }
        else{

            //跳转到新界面
            view.loadUrl(url , SignUtil.signHeader());
            return true;
        }
        return false;
    }

}
