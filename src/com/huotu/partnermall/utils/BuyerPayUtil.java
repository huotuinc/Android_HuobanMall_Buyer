package com.huotu.partnermall.utils;

import android.content.Context;
import android.os.Bundle;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.model.WxPaySuccessCallbackModel;
import com.huotu.partnermall.receiver.MyBroadcastReceiver;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;

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
}
