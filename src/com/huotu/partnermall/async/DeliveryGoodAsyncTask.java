package com.huotu.partnermall.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonSyntaxException;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.model.FMDeliveryGood;
import com.huotu.partnermall.model.PayGoodBean;
import com.huotu.partnermall.utils.LoadingUtil;

import java.util.Map;

/**
 * 商品支付任务
 */
public class DeliveryGoodAsyncTask extends AsyncTask<Void,Void, FMDeliveryGood > {
    String      orderNo;
    int         productType;
    long        productId;
    Context     context;
    Handler     handler;
    LoadingUtil util;
    public static final int PAY_ERROR = 3003;
    public static final int PAY_OK    = 3004;

    public
    DeliveryGoodAsyncTask (
            Context context, Handler handler, String orderNo, int productType,
            long productId
                          ) {
        this.context = context;
        this.orderNo = orderNo;
        this.productType = productType;
        this.productId = productId;
        this.handler = handler;
        this.util = new LoadingUtil((Activity )this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        util.showProgress();
    }

    @Override
    protected FMDeliveryGood doInBackground(Void... params) {
        FMDeliveryGood result = null;
        try {
            return null;
        }
        catch (JsonSyntaxException jsex){

            return null;
        }
    }

    @Override
    protected void onPostExecute(FMDeliveryGood fmDeliveryGood) {
        super.onPostExecute(fmDeliveryGood);

        util.dismissProgress();
    }

}
