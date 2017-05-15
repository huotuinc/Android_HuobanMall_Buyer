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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.BuildConfig;
import com.huotu.partnermall.model.BindEvent;
import com.huotu.partnermall.model.OrderInfoModel;
import com.huotu.partnermall.model.OrderModel;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.SwitchUserByUserIDEvent;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.WebViewActivity;

import com.huotu.partnermall.ui.login.PhoneLoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.BuyerPayUtil;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.UIUtils;
import com.huotu.partnermall.widgets.NoticePopWindow;
import com.huotu.partnermall.widgets.PayPopWindow;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Map;

import static android.R.attr.order;

/**
 * 拦截页面操作类
 */
public class UrlFilterUtils {
    private WeakReference<Activity> ref;
    private Handler mHandler;
    private BaseApplication application;
    public ProgressPopupWindow payProgress;
    private boolean isOpenKeFuInNewPage=false;

    public boolean isOpenKeFuInNewPage() {
        return isOpenKeFuInNewPage;
    }

    public void setOpenKeFuInNewPage(boolean openKeFuInNewPage) {
        isOpenKeFuInNewPage = openKeFuInNewPage;
    }

    public UrlFilterUtils (Activity aty, Handler mHandler, BaseApplication application  ) {
        this.mHandler = mHandler;
        this.application = application;
        this.ref = new WeakReference<>(aty);
    }

    /**
     * webview拦截url作相应的处理
     * @param view
     * @param url
     * @return
     */
    public boolean shouldOverrideUrlBySFriend ( WebView view, String url ) {
        if( ref.get() ==null) return false;

        if(url.startsWith("alipays://")){//处理支付宝
            try {
                // 以下固定写法
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ref.get().startActivity(intent);
            } catch (Exception e) {
                // 防止没有安装的情况
                e.printStackTrace();
                //ToastUtils.showLongToast("您所打开的支付宝App未安装！");
            }
            return true;
        }

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
        }else if(url.contains(Constants.WEB_TAG_USERINFO)){
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
        }else if(url.contains ( Constants.WEB_PAY ) ){
            getPayInfo(url , ref.get() );
            return true;
        }else if(url.contains ( Constants.AUTH_FAILURE ) || url.contains( Constants.AUTH_FAILURE_PHONE) || url.contains( Constants.AUTH_FAILURE2 ) || url.toLowerCase().contains( Constants.AUTH_FAILURE3.toLowerCase() ) ){
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
        }else if(url.toLowerCase().contains(Constants.URL_APPACCOUNTSWITCHER.toLowerCase())){//切换帐号 url
            String userId= Uri.parse(url).getQueryParameter("u");
            EventBus.getDefault().post(new SwitchUserByUserIDEvent(userId));
            return true;
        }else if(UIUtils.isIndexPage( url )){//当用户点击商品详情的首页按钮时，需要处理判断
            ActivityUtils.getInstance().showActivity( ref.get() , HomeActivity.class ,"redirecturl", url);
            return true;
        }else if( url.toLowerCase().contains( Constants.URL_PERSON_INDEX ) ){//当用户点击商品详情的个人中心按钮时，需要处理判断
            ActivityUtils.getInstance().showActivity( ref.get() , HomeActivity.class ,"redirecturl", url);
            return true;
        }else if( url.toLowerCase().contains( Constants.URL_KEFU_1.toLowerCase() ) ){//
            if(isOpenKeFuInNewPage){
                Bundle bundle = new Bundle ( );
                bundle.putString ( Constants.INTENT_URL,  url );
                ActivityUtils.getInstance ( ).showActivity ( ref.get() , WebViewActivity.class, bundle );
                return true;
            }else{
                view.loadUrl(url , SignUtil.signHeader());
            }
        }
        else{
            //跳转到新界面
            view.loadUrl(url , SignUtil.signHeader());
            return true;
        }
        return false;
    }


    private void getPayInfo( String url , final Activity aty ){
        if( aty ==null) return;

        if(payProgress==null){
            payProgress = new ProgressPopupWindow(aty);
        }
        payProgress.showProgress ( "正在加载支付信息" );
        payProgress.showAtLocation ( aty.getWindow().getDecorView(),  Gravity.CENTER, 0, 0 );

        final PayModel payModel = new PayModel ();

        Uri uri = Uri.parse(url);
        final String orderId = uri.getQueryParameter("trade_no");
        String customerId = uri.getQueryParameter("customerID");
        String payType = uri.getQueryParameter("paymentType");
        if( orderId ==null || orderId.isEmpty() ){
            payProgress.dismissView();
            ToastUtils.showLongToast("支付缺少订单信息");
            return;
        }

        payModel.setCustomId(customerId);
        payModel.setPaymentType(payType);
        payModel.setTradeNo(orderId);

        HttpUtil.getInstance().getOrderInfo( orderId , new Response.Listener<OrderInfoModel>() {
            @Override
            public void onResponse(OrderInfoModel orderInfoModel) {
                if( payProgress!=null) payProgress.dismissView();
                if( aty == null  ) return;

                if(200 == orderInfoModel.getCode ()) {
                    Map<String, String> data = orderInfoModel.getData();
                    if (null == data) {
                        //支付信息获取错误
                        ToastUtils.showLongToast("获取订单信息失败。");
                        return;
                    } else {
                        String sign = data.get("sign");
                        String orderId_2 = data.get("orderid");
                        data.remove("sign");
                        String sign_2 = new AuthParamUtils("").getSign(data , Constants.getAPP_SECRET() );
                        if( sign == null || orderId_2 == null || !sign.equals(sign_2) || !orderId_2.equals(orderId) ){
                            ToastUtils.showLongToast("订单信息验证失败！");
                            return;
                        }
                        String name = data.get("name");

                        payModel.setAmount(HttpUtil.formatToDecimal(data.get("finalamount")));
                        payModel.setAliAmount( data.get("finalamount") );
                        payModel.setDetail( name );
                        //PayPopWindow payPopWindow = new PayPopWindow(aty, mHandler, payModel);
                        //payPopWindow.showAtLocation(aty.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        if(payModel.getPaymentType().equals("11")){
                            BuyerPayUtil buyerPayUtil = new BuyerPayUtil();
                            buyerPayUtil.aliNativePay( aty , mHandler , payModel );
                        }else if(payModel.getPaymentType().equals("300")){
                            BuyerPayUtil buyerPayUtil = new BuyerPayUtil();
                            buyerPayUtil.wxPay( aty , mHandler , payModel );
                        }

                    }
                } else{
                    ToastUtils.showLongToast("获取订单信息失败。");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if( ref.get() ==null) return;
                if(payProgress==null) return;
                payProgress.dismissView();
            }
        });
    }
}
