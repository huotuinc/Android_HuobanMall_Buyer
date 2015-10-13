package com.huotu.partnermall.listener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Xml;
import android.view.View;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.FMPrepareBuy;
import com.huotu.partnermall.model.PayBodyModel;
import com.huotu.partnermall.model.Purchase;
import com.huotu.partnermall.model.WXPayResult;
import com.huotu.partnermall.utils.EncryptUtil;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.WXPayUtilEx;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付出发点击事件
 */
public
class WXPayListener implements View.OnClickListener {

    private
    Context context;
    private
    Handler handler;
    private PayBodyModel body;
    private
    FMPrepareBuy result;
    private Purchase selectedPurchase = null;


    public
    WXPayListener (
            Context context, Handler handler, PayBodyModel body, FMPrepareBuy result, Purchase selectedPurchase
                  ) {

        this.context = context;
        this.handler = handler;
        this.body = body;
        this.result = result;
        this.selectedPurchase = selectedPurchase;
    }

    @Override
    public
    void onClick ( View v ) {

        ToastUtils.showShortToast ( context, "开启微信支付" );

        if ( null == selectedPurchase ) {
            ToastUtils.showLongToast ( context, "请选择需要购买的流量套餐" );
            return;
        }
        if ( result == null ) {
            ToastUtils.showLongToast ( context, "支付信息空,无法进行支付操作" );
            return;
        }
        if ( null == result.getResultData ( ).getWxpayNotifyUri ( ) || result.getResultData ( )
                                                                             .getWxpayNotifyUri (
                                                                                                )
                                                                             .length ( ) < 1 ) {
            ToastUtils.showLongToast ( context, "支付通知地址空，无法进行支付操作" );
            return;
        }
        new WXPayAsyncTask ( handler, body.getBody (), body.getPrice (), body.getProductType (), body.getProductId () )
                .execute ( );
    }

    class WXPayAsyncTask extends AsyncTask< Void, Void, WXPayResult > {
        private Handler handler;
        private String  body;
        private String  price;
        private int     productType;
        private long    productId;

        public
        WXPayAsyncTask ( Handler handler, String body, String price, int productType, long
                productId ) {
            this.handler = handler;
            this.body = body;
            this.price = price;
            this.productType = productType;
            this.productId = productId;
        }

        @Override
        protected
        void onPreExecute ( ) {

        }

        @Override
        protected
        void onPostExecute ( WXPayResult result ) {

        }

        @Override
        protected WXPayResult doInBackground(Void... params)
        {
            WXPayResult payResult = new WXPayResult();
            try
            {
               /* WXPayUtilEx wxPay = new WXPayUtilEx(context,
                                                    handler, result.getResultData().getWxpayNotifyUri()  );

                payResult =  wxPay.pay( this.body , this.price , productType , productId );*/

            } catch (Exception ex)
            {
                payResult.setCode(0);
                payResult.setMessage(ex.getMessage());
            }
            return payResult;
        }
    }

}
