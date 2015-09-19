package com.huotu.partnermall.listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;
import android.view.View;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.EncryptUtil;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.ToastUtils;
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
    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI ( context, null );
    Map< String, String > resultunifiedorder;
    StringBuffer          buffer;


    public
    WXPayListener ( Context context ) {

        this.context = context;
        req = new PayReq ();
        buffer = new StringBuffer (  );
        msgApi.registerApp ( Constants.WXPAY_ID );
    }

    @Override
    public
    void onClick ( View v ) {

        ToastUtils.showShortToast ( context, "开始微信支付." );
        //生成签名参数
        genPayReq ( );
        //生成prepay_id
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute ( );
    }

    private void genPayReq() {

        req.appId = Constants.WXPAY_ID;
        req.partnerId = Constants.wxpayApikey;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr ( );
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair > signParams = new LinkedList<NameValuePair> ();
        signParams.add(new BasicNameValuePair ("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        buffer.append ( "sign\n" + req.sign + "\n\n" );

        KJLoger.i ( signParams.toString ( ) );

    }

    private String genNonceStr() {
        Random random = new Random();
        return EncryptUtil.getInstance().encryptMd532 ( String.valueOf ( random.nextInt ( 10000 ) ) );
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.WXPAY_ID);

        this.buffer.append ( "sign str\n" + sb.toString ( ) + "\n\n" );
        String appSign = EncryptUtil.getInstance().encryptMd532 ( sb.toString ( ) ).toUpperCase ( );
        KJLoger.i( appSign );
        return appSign;
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {

        private ProgressDialog dialog;


        @Override
        protected
        void onPreExecute ( ) {
            dialog = ProgressDialog.show ( context, context.getString ( R.string.app_tip ),
                                           context.getString ( R.string.getting_prepayid ) );
        }

        @Override
        protected
        void onPostExecute ( Map< String, String > result ) {
            if ( dialog != null ) {
                dialog.dismiss ( );
            }
            buffer.append ( "prepay_id\n" + result.get ( "prepay_id" ) + "\n\n" );
            resultunifiedorder = result;

            //调用微信支付
            sendPayReq();

        }

        @Override
        protected
        void onCancelled ( ) {
            super.onCancelled ( );
        }

        @Override
        protected
        Map< String, String > doInBackground ( Void... params ) {

            String url    = String.format ( Constants.WX_URL );
            String entity = genProductArgs ( );

            KJLoger.i ( entity );

            byte[] buf = HttpUtil.getInstance ().httpPost ( url, entity );
            String content = new String(buf);
            KJLoger.i ( content );
            Map< String, String > xml = decodeXml ( content );

            return xml;
        }
    }

    public Map<String,String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String> ();
            XmlPullParser parser = Xml.newPullParser ( );
            parser.setInput(new StringReader (content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if("xml".equals(nodeName)==false){
                            //实例化student对象
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            KJLoger.e ( e.toString ( ) );
        }
        return null;

    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String	nonceStr = genNonceStr();


            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.WXPAY_ID));
            packageParams.add(new BasicNameValuePair("body", "weixin"));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.wxpayApikey));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
            packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));


            String sign = genPackageSign ( packageParams );
            packageParams.add ( new BasicNameValuePair ( "sign", sign ) );


            String xmlstring =toXml ( packageParams );

            return xmlstring;

        } catch (Exception e) {
            KJLoger.e("genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }

    private String genOutTradNo() {
        Random random = new Random();
        return EncryptUtil.getInstance ( ).encryptMd532 ( String.valueOf ( random.nextInt ( 10000
                                                                                          ) ) );
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");


            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
        }
        sb.append("</xml>");

        KJLoger.i(sb.toString());
        return sb.toString ( );
    }

    /**
     生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append ( Constants.WXPAY_ID);


        String packageSign = EncryptUtil.getInstance ().encryptMd532 (buffer.toString ( )).toUpperCase();
        KJLoger.i( packageSign );
        return packageSign;
    }

    private void sendPayReq() {


        msgApi.registerApp(Constants.WXPAY_ID);
        msgApi.sendReq(req);
    }
}
