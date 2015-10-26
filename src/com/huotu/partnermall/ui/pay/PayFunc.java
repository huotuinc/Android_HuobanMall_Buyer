package com.huotu.partnermall.ui.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.async.WXPayAsyncTask;
import com.huotu.partnermall.model.FMPrepareBuy;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AliPayUtil;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

/**
 * 支付总览
 */
public
class PayFunc {

    private PayModel payModel;
    private
    BaseApplication application;
    private
    FMPrepareBuy    prepareBuy;
    private
    Handler         handler;
    private
    Context         context;
    private
    Activity        aty;
    private ProgressPopupWindow payProgress;

    public
    PayFunc ( Context context, PayModel payModel, BaseApplication application, Handler handler,
              Activity aty, ProgressPopupWindow payProgress ) {
        this.payModel = payModel;
        this.application = application;
        this.handler = handler;
        this.context = context;
        this.aty = aty;
        this.payProgress = payProgress;
    }

    public
    void wxPay ( ) {
        //根据订单号获取支付信息
        String body        = payModel.getDetail ( );
        String price       = String.valueOf ( payModel.getAmount ( ) );
        int    productType = 0;
        long   productId   = 0;
        prepareBuy = new FMPrepareBuy ();
        payProgress.dismissView ();
        //调用微信支付模块
        new WXPayAsyncTask (handler, body, price, productType, productId, context, prepareBuy, application, payModel.getNotifyurl ()).execute();
    }

    public void aliPay()
    {
        AliPayUtil aliPay = new AliPayUtil(aty, handler, application);
        //根据订单号获取订单信息
        String body = payModel.getDetail ( );
        String price = String.valueOf ( payModel.getAmount () );
        String subject = payModel.getDetail ();
        int productType= 0;
        long productId= 0;
        prepareBuy = new FMPrepareBuy ();
        payProgress.dismissView ();
        aliPay.pay(subject, body, price, payModel.getNotifyurl (), productType, productId);
    }
}

