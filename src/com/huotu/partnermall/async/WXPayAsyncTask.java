package com.huotu.partnermall.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.model.FMPrepareBuy;
import com.huotu.partnermall.model.WXPayResult;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.WXPayUtilEx;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

/**
 * 异步执行微信支付
 */
public class WXPayAsyncTask extends AsyncTask<Void, Void, WXPayResult > {

    private Handler handler;
    private String body;
    private String price;
    private int productType;
    private long productId;
    private Context context;
    private BaseApplication application;
    private String notifyUrl;
    private String attach;
    private String orderId;

    public WXPayAsyncTask(Handler handler, String body, String price, int productType, long productId,
                          Context context, BaseApplication application,
                          String notifyUrl, String attach, String orderId) {

        this.handler = handler;
        this.body = body;
        this.price = price;
        this.productType = productType;
        this.productId = productId;
        this.context = context;
        //this.prepareBuy = prepareBuy;
        this.application = application;
        this.notifyUrl = notifyUrl;
        this.attach = attach;
        this.orderId = orderId;
    }

    @Override
    protected WXPayResult doInBackground(Void... params) {
        WXPayResult payResult = new WXPayResult();
        try {
            WXPayUtilEx wxPay = new WXPayUtilEx(context, handler, notifyUrl, application);

            payResult = wxPay.pay(this.body, this.price, productType, productId, attach, orderId);

        } catch (Exception ex) {
            payResult.setCode(0);
            payResult.setMessage(ex.getMessage());
        }
        return payResult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(WXPayResult wxPayResult) {
        super.onPostExecute(wxPayResult);
        if (wxPayResult != null && wxPayResult.getCode() != 1) {
            ToastUtils.showLongToast(context, wxPayResult.getMessage());
        }
    }
}
