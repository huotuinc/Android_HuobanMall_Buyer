package com.huotu.partnermall.ui.web;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.ui.WebViewActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import java.lang.ref.WeakReference;

/**
 * 过滤UI类
 */
public class SubUrlFilterUtils {

    private Context  context;
    private WeakReference<Activity> ref;
    //private Activity aty;
    TextView titleView;
    private Handler mHandler;
    private BaseApplication     application;
    public ProgressPopupWindow payProgress;
    public WindowManager wManager;

    public SubUrlFilterUtils (
            Activity aty, Context context, TextView titleView, Handler mHandler,
            BaseApplication application
                      ) {
        this.context = context;
        this.titleView = titleView;
        this.mHandler = mHandler;
        this.application = application;
        this.ref = new WeakReference<>(aty);
        wManager = aty.getWindowManager ( );
        payProgress = new ProgressPopupWindow ( context, aty, wManager );
    }

    /**
     * webview拦截url作相应的处理
     * @param view
     * @param url
     * @return
     */
    public boolean shouldOverrideUrlBySFriend ( WebView view, String url ) {
        if( ref.get()==null) return true;

        if ( url.contains ( Constants.WEB_TAG_NEWFRAME ) ) {
            String urlStr = url.substring ( 0, url.indexOf ( Constants.WEB_TAG_NEWFRAME ) );
            Bundle bundle = new Bundle ( );
            bundle.putString ( Constants.INTENT_URL, urlStr );
            ActivityUtils.getInstance ( ).showActivity ( ref.get() , WebViewActivity.class, bundle );
            return true;
        }
        else if ( url.contains ( Constants.WEB_TAG_USERINFO ) ) {
            //修改用户信息
            //判断修改信息的类型
            String type = url.substring(url.indexOf("=", 1)+1, url.indexOf("&", 1));
            if(Constants.MODIFY_PSW.equals ( type )){
                //弹出修改密码框
            }
            return true;
        }else if(url.contains(Constants.WEB_TAG_LOGOUT)){
            //处理登出操作
            //鉴权失效
            //清除登录信息
            application.logout ();
            //跳转到登录界面
            ActivityUtils.getInstance ().skipActivity ( ref.get() , LoginActivity.class );
        }else if(url.contains(Constants.WEB_TAG_INFO)){
            //处理信息保护
            return true;
        }else if(url.contains(Constants.WEB_TAG_FINISH)){
            if(view.canGoBack())
                view.goBack();

        } else if(url.contains ( Constants.WEB_PAY ) )
        {
            //支付进度
            payProgress.showProgress ( "正在启用支付模块" );
            payProgress.showAtLocation (titleView, Gravity.CENTER, 0, 0 );
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
                    KJLoger.i ( "支付参数出错." );
                }
            }
            //获取用户等级
            StringBuilder builder = new StringBuilder (  );
            builder.append ( Constants.INTERFACE_PREFIX + "order/GetOrderInfo" );
            builder.append ( "?orderid="+tradeNo );
            AuthParamUtils param = new AuthParamUtils ( application, System.currentTimeMillis (), builder.toString (), context );
            String orderUrl = param.obtainUrlOrder ( );
            HttpUtil.getInstance ( ).doVolleyPay ( ref.get() , context, mHandler, application, orderUrl, payModel, payProgress, titleView, wManager );
            return true;

        }
        else if(url.contains ( Constants.AUTH_FAILURE ))
        {
            //鉴权失效
            //清除登录信息
            application.logout ();
            //跳转到登录界面
            ActivityUtils.getInstance ().skipActivity ( ref.get() , LoginActivity.class );
        }
        else
        {
            //跳转界面
            view.loadUrl ( url );
            return false;
        }
        return false;
    }
}
