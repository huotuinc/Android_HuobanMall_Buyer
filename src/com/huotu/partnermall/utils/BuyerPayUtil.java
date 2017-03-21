package com.huotu.partnermall.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.webkit.URLUtil;

import com.huotu.android.library.libpay.alipay.AliOrderInfo;
import com.huotu.android.library.libpay.alipayV2.AliPayInfoV2;
import com.huotu.android.library.libpay.alipayV2.AliPayUtilV2;
import com.huotu.android.library.libpay.weixin.WeiXinOrderInfo;
import com.huotu.android.library.libpay.weixin.WeiXinPayInfo;
import com.huotu.android.library.libpay.weixin.WeiXinPayUtil;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.WxPaySuccessCallbackModel;
import com.huotu.partnermall.receiver.MyBroadcastReceiver;
import com.huotu.partnermall.widgets.NoticePopWindow;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/1/11.
 */

public class BuyerPayUtil {

    public static void paySuccessCallback(Context context, BaseResp resp) {
        PayResp payResp = (PayResp) resp;
        Bundle bundle = new Bundle();
        if (payResp != null && payResp.extData != null) {
            WxPaySuccessCallbackModel extData = JSONUtil.getGson().fromJson(payResp.extData, WxPaySuccessCallbackModel.class);
            bundle.putSerializable(Constants.HUOTU_PAY_CALLBACK_KEY, extData);
        }
        MyBroadcastReceiver.sendBroadcast( BaseApplication.single, MyBroadcastReceiver.ACTION_PAY_SUCCESS, bundle);
    }

    public static void wxPayCancelCallback( BaseResp resp){
        PayResp payResp = (PayResp) resp;
        Bundle bundle = new Bundle();
        if (payResp != null && payResp.extData != null) {
            WxPaySuccessCallbackModel extData = JSONUtil.getGson().fromJson(payResp.extData, WxPaySuccessCallbackModel.class);
            bundle.putSerializable(Constants.HUOTU_PAY_CALLBACK_KEY, extData);
        }
        MyBroadcastReceiver.sendBroadcast(BaseApplication.single , MyBroadcastReceiver.ACTION_WX_PAY_CANCEL_CALLBACK, bundle);
    }

    public static void wxPayErrorCallback(BaseResp resp){
        PayResp payResp = (PayResp) resp;
        Bundle bundle = new Bundle();
        if (payResp != null && payResp.extData != null) {
            WxPaySuccessCallbackModel extData = JSONUtil.getGson().fromJson(payResp.extData, WxPaySuccessCallbackModel.class);
            bundle.putSerializable(Constants.HUOTU_PAY_CALLBACK_KEY, extData);
        }
        MyBroadcastReceiver.sendBroadcast(BaseApplication.single , MyBroadcastReceiver.ACTION_WX_PAY_ERROR_CALLBACK, bundle);
    }


    public void aliNativePay(Activity aty , Handler mHandler , PayModel payModel){
        if (!BaseApplication.single.scanAliPay()) {//缺少支付信息
            //NoticePopWindow noticePop = new NoticePopWindow(aty, "缺少支付信息");
            //noticePop.showNotice();
            //noticePop.showAtLocation(aty.findViewById(R.id.titleText), Gravity.CENTER, 0, 0);
            ToastUtils.showLongToast("缺少支付信息");
        }else{
            payModel.setAttach(payModel.getCustomId() + "_0");
            //payModel.setNotifyurl(application.obtainMerchantUrl() + application.readAlipayNotify());
            payModel.setNotifyurl( getNotifyUrl( BaseApplication.single.readAlipayDomain() , BaseApplication.single.readAlipayNotify() , BaseApplication.single.obtainMerchantUrl() ) );
            AliPayInfoV2 aliPayInfoV2 = new AliPayInfoV2();
            aliPayInfoV2.setAppId( BaseApplication.single.readAlipayAppId() );
            aliPayInfoV2.setpId(BaseApplication.single.readAlipayParentId());
            aliPayInfoV2.setRsa_private(EncryptUtil.getInstance().decryptDES( BaseApplication.single.readAlipayAppKey() , Constants.getDES_KEY() ));
            //aliPayInfoV2.setRsa2_private( EncryptUtil.getInstance().decryptDES(application.readAlipayAppKey(), Constants.getDES_KEY()) );
            aliPayInfoV2.setNotifyUrl( payModel.getNotifyurl() );

            AliOrderInfo aliOrderInfo = new AliOrderInfo();
            aliOrderInfo.setTotalfee( new BigDecimal( payModel.getAliAmount()));
            aliOrderInfo.setSubject(payModel.getTradeNo());
            aliOrderInfo.setOrderNo(payModel.getTradeNo());
            aliOrderInfo.setBody(payModel.getDetail());

            AliPayUtilV2 aliPayUtilV2  = new AliPayUtilV2(aty , mHandler , aliPayInfoV2);
            aliPayUtilV2.payV2(aliOrderInfo);
        }
    }


    public void wxPay(Activity aty ,Handler mHandler , PayModel payModel ){
        if (!BaseApplication.single.scanWx()) {//缺少支付信息
            ToastUtils.showLongToast("缺少支付信息");
//            NoticePopWindow noticePop = new NoticePopWindow(aty, "缺少支付信息");
//            noticePop.showNotice();
//            noticePop.showAtLocation(aty.findViewById(R.id.titleText), Gravity.CENTER, 0, 0);
        } else {
            //progress.showProgress("正在加载支付信息");
            //progress.showAtLocation( aty.findViewById(R.id.titleText), Gravity.CENTER, 0, 0 );
            payModel.setAttach(payModel.getCustomId() + "_0");
            //添加微信回调路径
            payModel.setNotifyurl( getNotifyUrl( BaseApplication.single.readWeixinDomain() , BaseApplication.single.readWeixinNotify() , BaseApplication.single.obtainMerchantUrl() ) );


            WeiXinOrderInfo weiXinOrderInfo = new WeiXinOrderInfo();
            weiXinOrderInfo.setBody(payModel.getDetail());
            weiXinOrderInfo.setOrderNo(payModel.getTradeNo());
            weiXinOrderInfo.setTotal_fee(payModel.getAmount());
            weiXinOrderInfo.setAttach(payModel.getAttach());

            String wxAppId = BaseApplication.single.readWxpayAppId();
            String wxAppSecret = EncryptUtil.getInstance().decryptDES( BaseApplication.single.readWxpayAppKey() , Constants.getDES_KEY());
            String wxPartner=BaseApplication.single.readWxpayParentId();
            String notifyUrl = BaseApplication.single.obtainMerchantUrl() + BaseApplication.single.readWeixinNotify();

            WeiXinPayInfo weiXinPayInfo = new WeiXinPayInfo( wxAppId , wxPartner , wxAppSecret , notifyUrl);
            WeiXinPayUtil weiXinPayUtil = new WeiXinPayUtil(aty , mHandler , weiXinPayInfo);
            weiXinPayUtil.pay(weiXinOrderInfo);
        }
    }


    protected String getNotifyUrl( String domain , String notifyUrl , String mallUrl){
        if( domain == null || domain.trim().isEmpty() ){
            return mallUrl + notifyUrl;
        }else{
            return domain+notifyUrl;
        }
    }
}
