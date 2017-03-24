package com.huotu.partnermall.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.AuthMallModel;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.MerchantInfoModel;
import com.huotu.partnermall.model.MerchantPayInfo;
import com.huotu.partnermall.model.OrderInfoModel;
import com.huotu.partnermall.model.OrderModel;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.SwitchUserModel;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.widgets.NoticePopWindow;
import com.huotu.partnermall.widgets.PayPopWindow;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.AccessControlContext;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class HttpUtil{

    private static class Holder{
        private static final HttpUtil instance = new HttpUtil();
    }

    private HttpUtil(){
    }

    public static final HttpUtil getInstance() {
        return Holder.instance;
    }

    /**
     * 获取商户logo
     * @param application
     * @param url
     */
//    public void doVolleyLogo( final BaseApplication application, String url)
//    {
//        final KJJsonObjectRequest re = new KJJsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject >(){
//
//            @Override
//            public void onResponse(JSONObject response) {
//
//                JSONUtil<MerchantInfoModel > jsonUtil = new JSONUtil<MerchantInfoModel>();
//                MerchantInfoModel merchantInfo = new MerchantInfoModel();
//                merchantInfo = jsonUtil.toBean(response.toString (), merchantInfo);
//                if(null != merchantInfo) {
//                    String site = merchantInfo.getMall_site();
//                    application.writeDomain(site);
//                    String logo = null;
//                    if ( null != merchantInfo.getMall_logo ( ) && null != merchantInfo.getMall_name () ) {
//                        if(!TextUtils.isEmpty ( application.obtainMerchantUrl () ))
//                        {
//                            logo =  application.obtainMerchantUrl () + merchantInfo.getMall_logo ( );
//                        }
//                        else
//                        {
//                            logo = merchantInfo.getMall_logo ( );
//                        }
//
//                        String name = merchantInfo.getMall_name ( );
//                        application.writeMerchantLogo ( logo );
//                        application.writeMerchantName ( name );
//
//                        //记录登录配置方式
//                        application.writeLoginMethod( merchantInfo.getAccountModel() );
//                        //记录服务器app最新版本信息
//                        application.writeNewVersion( merchantInfo.getVersionnumber() );
//                        application.writeAppUrl( merchantInfo.getApplinkurl() );
//
//                        //记录客服地址
//                        String webChannel = merchantInfo.getWebchannel();
//                        String webChannelStr="";
//                        if(!TextUtils.isEmpty( webChannel)) {
//                            try {
//                                webChannelStr = URLDecoder.decode(webChannel);
//                            }catch ( Exception ex){
//                                Log.e( "httpUtil" , ex.getMessage() );
//                            }
//                        }
//                        application.writeMerchanntWebChannel( webChannelStr );
//
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        VolleyUtil.getRequestQueue().add( re);
//    }
    /**
     * 获取支付信息
     * @param application
     * @param url
     */
    public void doVolley( final BaseApplication application, String url ) {
        final KJJsonObjectRequest re = new KJJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                JSONUtil<MerchantPayInfo> jsonUtil = new JSONUtil<>();
                MerchantPayInfo merchantPayInfo = new MerchantPayInfo();
                merchantPayInfo = jsonUtil.toBean(response.toString(), merchantPayInfo);
                if (null != merchantPayInfo) {
                    if (merchantPayInfo.getCode() != 200) {
                        BaseApplication.cleanAliPayInfo();
                        BaseApplication.cleanWeixinPayInfo();
                        BaseApplication.cleanWebAliPayInfo();
                        //ToastUtils.showLongToast(merchantPayInfo.getMsg());
                        return;
                    }

                    BaseApplication.cleanAliPayInfo();
                    BaseApplication.cleanWeixinPayInfo();
                    BaseApplication.cleanWebAliPayInfo();

                    List<MerchantPayInfo.MerchantPayModel> merchantPays = merchantPayInfo.getData();
                    if (merchantPays != null && !merchantPays.isEmpty()) {
                        for (MerchantPayInfo.MerchantPayModel merchantPay : merchantPays) {
                            //if (400 == merchantPay.getPayType()) {
                            if(1 == merchantPay.getPayType()){
                                //支付宝网页支付信息
                                application.writeWebAlipay( merchantPay.getAppId() , merchantPay.getPartnerId(), merchantPay.getAppKey(), merchantPay.getNotify(), merchantPay.isWebPagePay() , merchantPay.getPayCenterDomain() );
                            } else if (300 == merchantPay.getPayType()) {
                                //微信支付
                                application.writeWx(merchantPay.getPartnerId(), merchantPay.getAppId(), merchantPay.getAppKey(), merchantPay.getNotify(), merchantPay.isWebPagePay() , merchantPay.getPayCenterDomain());
                            } else if( 11 == merchantPay.getPayType()){
                                //支付宝原生支付信息
                                application.writeAlipay( merchantPay.getAppId(), merchantPay.getPartnerId(), merchantPay.getAppKey(), merchantPay.getNotify(), merchantPay.isWebPagePay() , merchantPay.getPayCenterDomain());
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                BaseApplication.cleanAliPayInfo();
//                BaseApplication.cleanWeixinPayInfo();
            }

        });
        VolleyUtil.getRequestQueue().add(re);
    }

    public void doVolley(final Activity aty, final Handler mHandler, final BaseApplication application,
                         String url, Map param, final AccountModel account , final Bundle bundlePush ){
        final GsonRequest re = new GsonRequest (
                Request.Method.POST, url, AuthMallModel.class, null, param,
                new Response.Listener<AuthMallModel >(){
            @Override
            public void onResponse(AuthMallModel response) {
                if( aty ==null )return;

                AuthMallModel authMallModel = new AuthMallModel();
                authMallModel = response;
                if( authMallModel !=null && 200 == authMallModel.getCode ())
                {

                    AuthMallModel.AuthMall mall = authMallModel.getData ();
                    if(null != mall)
                    {
                        //写入userID
                        account.setAccountId(String.valueOf(mall.getUserid()));
                        account.setAccountName(mall.getNickName());
                        account.setAccountIcon(mall.getHeadImgUrl());

                        //和商城用户系统交互
                        application.writeMemberInfo (
                                account.getAccountName ( ), account.getAccountId ( ),
                                account.getAccountIcon ( ), account.getAccountToken ( ),
                                account.getAccountUnionId ( ), account.getOpenid()
                        );
                        application.writeMemberLevel(mall.getLevelName());
                        //记录登录类型(1:微信登录，2：手机登录)
                        application.writeMemberLoginType(1);
                        //记录微信关联类型（0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号）
                        application.writeMemberRelatedType( mall.getRelatedType() );

                        //设置侧滑栏菜单
                        List<MenuBean > menus = new ArrayList< MenuBean > (  );
                        MenuBean menu = null;
                        List<AuthMallModel.MenuModel > home_menus = mall.getHome_menus ();
                        for(AuthMallModel.MenuModel home_menu:home_menus)
                        {
                            menu = new MenuBean ();
                            menu.setMenuGroup ( String.valueOf ( home_menu.getMenu_group () ) );
                            menu.setMenuIcon ( home_menu.getMenu_icon ( ) );
                            menu.setMenuName ( home_menu.getMenu_name ( ) );
                            menu.setMenuUrl ( home_menu.getMenu_url ( ) );
                            menus.add ( menu );
                        }
                        if(null != menus && !menus.isEmpty ())
                        {
                            application.writeMenus ( menus );


                            //绑定极光推送别名
                            UIUtils.bindPush();

                            Intent intent = new Intent();
                            intent.setClass( aty , HomeActivity.class);
                            //传递推送信息
                            if(null != bundlePush){
                                intent.putExtra( Constants.HUOTU_PUSH_KEY ,  bundlePush );
                            }

                            //跳转到首页
                            ActivityUtils.getInstance ().skipActivity ( aty, intent );
                        }
                        else
                        {
                            mHandler.sendEmptyMessage ( Constants.INIT_MENU_ERROR );
                        }
                    }
                    else
                    {
                        Message msg = new Message();
                        msg.what = Constants.LOGIN_AUTH_ERROR;
                        msg.obj = authMallModel.getMsg ();
                        mHandler.sendMessage ( msg );
                    }

                }
                else if( authMallModel !=null && 403 == authMallModel.getCode ())
                {
                    //授权失败
                    Message msg = new Message();
                    msg.what = Constants.LOGIN_AUTH_ERROR;
                    msg.obj = authMallModel.getMsg ();
                    mHandler.sendMessage ( msg );
                }
                else
                {
                    //授权失败
                    Message msg = new Message();
                    msg.what = Constants.LOGIN_AUTH_ERROR;
                    msg.obj = authMallModel.getMsg ();
                    mHandler.sendMessage ( msg );
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Message msg = new Message();
                msg.what = Constants.LOGIN_AUTH_ERROR;
                msg.obj = "调用授权接口失败，请确认！";
                mHandler.sendMessage ( msg );
            }


        });
        VolleyUtil.getRequestQueue().add(re);
    }

    public void doVolleyObtainUser(final Activity aty,  final BaseApplication application, String url, final View view, final WindowManager wManager, final Handler mHandler) {
        final KJJsonObjectRequest re = new KJJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                if (aty == null) return;

                JSONUtil<SwitchUserModel> jsonUtil = new JSONUtil<SwitchUserModel>();
                SwitchUserModel switchUser = new SwitchUserModel();
                switchUser = jsonUtil.toBean(response.toString(), switchUser);

                if (null != switchUser) {
                    List<SwitchUserModel.SwitchUser> userList = switchUser.getData();
                    if ((null != userList) && (!userList.isEmpty()) && (userList.size() > 1)) {
                        //去重复数据
                        List<SwitchUserModel.SwitchUser> sourceList = new ArrayList<SwitchUserModel.SwitchUser>();
                        sourceList = clearData(userList);
                        //关闭载入数据条
                        mHandler.sendEmptyMessage(Constants.LOAD_SWITCH_USER_OVER);
                        //弹出切换用户面板
                        SwitchUserPopWin userPop = new SwitchUserPopWin(aty, sourceList, application, wManager, mHandler);
                        userPop.initView();
                        userPop.showAtLocation(view, Gravity.CENTER, 0, 0);
                        userPop.setOnDismissListener(new PoponDismissListener(aty));
                    } else if ((null != userList) && (!userList.isEmpty()) && (userList.size() == 1)) {
                        //关闭载入数据条
                        mHandler.sendEmptyMessage(Constants.LOAD_SWITCH_USER_OVER);

                        NoticePopWindow noticePop = new NoticePopWindow(aty, "无其他账户，请绑定其他账户。");
                        noticePop.showNotice();
                        noticePop.showAtLocation(
                                view,
                                Gravity.CENTER, 0, 0
                        );
                    } else {
                        //关闭载入数据条
                        mHandler.sendEmptyMessage(Constants.LOAD_SWITCH_USER_OVER);
                        NoticePopWindow noticePop = new NoticePopWindow(aty, "无其他账户。");
                        noticePop.showNotice();
                        noticePop.showAtLocation(view, Gravity.CENTER, 0, 0);
                    }
                } else {
                    //关闭载入数据条
                    mHandler.sendEmptyMessage(Constants.LOAD_SWITCH_USER_OVER);

                    NoticePopWindow noticePop = new NoticePopWindow(aty, "未检测到你的账户信息，请确认。");
                    noticePop.showNotice();
                    noticePop.showAtLocation(
                            view,
                            Gravity.CENTER, 0, 0
                    );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        });
        VolleyUtil.getRequestQueue().add(re);
    }

    /**
     * 清楚重复数据
     * @param dataList
     * @return
     */
    private List<SwitchUserModel.SwitchUser> clearData(List<SwitchUserModel.SwitchUser> dataList)
    {
        if(!dataList.isEmpty()){
            for(int i=0;i<dataList.size();i++){
                for(int j=dataList.size()-1;j>i;j--){
                    if(dataList.get(i).getUserid () == dataList.get(j).getUserid ()){
                        dataList.remove(j);
                    }
                }
            }
        }

        return dataList;
    }

//    //保留2位小数
//    private double format2Decimal(double d)
//    {
//        BigDecimal bg = new BigDecimal ( d );
//        return bg.setScale ( 2,   BigDecimal.ROUND_HALF_UP).doubleValue();
//    }

    public static int formatToDecimal(String d){
        try {
            BigDecimal bg = new BigDecimal(d);
            BigDecimal bg2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP);
            return bg2.multiply( new BigDecimal(100) ).intValue();
        }catch (NumberFormatException ex){
            throw  ex;
        }
    }

    public void getOrderInfo(String orderId , Response.Listener<OrderInfoModel> listener , Response.ErrorListener errorListener){
        String url = Constants.getINTERFACE_PREFIX() + "order/getpayinfo?orderid="+ orderId;

        AuthParamUtils param = new AuthParamUtils ( BaseApplication.single , System.currentTimeMillis (), url );
        String orderUrl = param.obtainUrlOrder();

        GsonRequest<OrderInfoModel> request = new GsonRequest<OrderInfoModel>(Request.Method.GET, orderUrl , OrderInfoModel.class, null,  listener , errorListener);

        VolleyUtil.getRequestQueue().add(request);
    }
}
