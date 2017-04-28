package com.huotu.mall.wenslimall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.receiver.MyBroadcastReceiver;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.huotu.partnermall.utils.BuyerPayUtil;

/**
 * 微信支付回调类
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private BaseApplication application;

    @Override
    public void onReq ( BaseReq baseReq ) {
    }

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.pay_result );
        application = ( BaseApplication ) this.getApplication ();
        api = WXAPIFactory.createWXAPI ( this, application.readWxpayAppId ( ) );
        api.handleIntent ( getIntent ( ), this );
    }

    @Override
    public
    void onResp ( BaseResp resp ) {

        Log.i ( "info","onPayFinish, errCode = " + resp.errCode );

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String msg = "";
            if( resp.errCode== 0){

                BuyerPayUtil.paySuccessCallback( this , resp);

                msg="支付成功";
                this.finish();
                ToastUtils.showLongToast (  msg );
                return;
            }else if( resp.errCode== -1){
                BuyerPayUtil.wxPayErrorCallback(resp);
                msg="支付失败";
                ToastUtils.showLongToast (  msg );
                this.finish();
                return;
            }else if(resp.errCode ==-2){
                BuyerPayUtil.wxPayCancelCallback(resp);
                msg="用户取消支付";
                ToastUtils.showLongToast( msg);
                this.finish();
                return;
            }

            PayResp payResp = (PayResp)resp;
            if(null==payResp){
                Log.i("wxpay>>>payResp=null","");
                msg="支付失败";
                ToastUtils.showLongToast( msg);
                this.finish();
                return;
            }else{
                Log.i("wxpay>>>extData", payResp.extData==null? "": payResp.extData );
                //Log.i("wxpay>>>prepayid",payResp.prepayId);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
}
