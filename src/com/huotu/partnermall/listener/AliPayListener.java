package com.huotu.partnermall.listener;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.huotu.partnermall.model.FMPrepareBuy;
import com.huotu.partnermall.model.PayBodyModel;
import com.huotu.partnermall.model.Purchase;
import com.huotu.partnermall.utils.AliPayUtil;
import com.huotu.partnermall.utils.ToastUtils;

/**
 * 支付宝支付点击事件
 */
public
class AliPayListener implements View.OnClickListener {

    private
    Context context;
    private
    Activity aty;
    private
    Handler handler;
    private
    PayBodyModel body;
    private
    FMPrepareBuy result;
    private Purchase selectedPurchase = null;

    public
    AliPayListener ( Activity aty,Context context, PayBodyModel body, FMPrepareBuy result, Purchase selectedPurchase, Handler handler  ) {
        this.context = context;
        this.body = body;
        this.result = result;
        this.selectedPurchase = selectedPurchase;
        this.handler = handler;
        this.aty = aty;
    }

    @Override
    public
    void onClick ( View v ) {
        ToastUtils.showShortToast ( context, "开始支付宝支付." );

        if (null == selectedPurchase)
        {
            ToastUtils.showLongToast(context, "请选择需要购买的流量套餐");
            return;
        }
        if (result == null)
        {
            ToastUtils.showLongToast(context, "支付信息空,无法进行支付操作");
            return;
        }
        if( null == result.getResultData().getAlipayNotifyUri() || result.getResultData().getWxpayNotifyUri().length()<1 ){
            ToastUtils.showLongToast(context, "支付通知地址空,无法进行支付操作");
            return;
        }

        AliPayUtil aliPay = new AliPayUtil(aty, handler);

        aliPay.pay(body.getSubject (), body.getBody (), body.getPrice (), body.getNotifyurl (), body.getProductType (), body.getProductId ());
    }
}
