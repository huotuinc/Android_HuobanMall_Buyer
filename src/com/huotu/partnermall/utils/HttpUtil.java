package com.huotu.partnermall.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.model.MemberModel;
import com.huotu.partnermall.model.MerchantPayInfo;
import com.huotu.partnermall.model.OrderModel;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.ui.pay.PayFunc;
import com.mob.tools.network.SSLSocketFactoryEx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpUtil
{

    private static class Holder
    {
        private static final HttpUtil instance = new HttpUtil();
    }

    private HttpUtil()
    {

    }

    public static final HttpUtil getInstance()
    {
        return Holder.instance;
    }

    /**
     *
     * @方法描述：post请求
     * @方法名：doPost
     * @参数：@param url
     * @参数：@param params
     * @参数：@return
     * @返回：InputStream
     * @exception
     * @since
     */
    public String doPost(String url, final Map<String, String> params)
    {
        // POST方式
        URL post_url;
        String jsonStr = null;
        HttpURLConnection conn = null;
        InputStream inStream = null;
        OutputStream os = null;
        try
        {
            post_url = new URL(url);
            conn = (HttpURLConnection) post_url.openConnection();
            conn.setRequestMethod("POST");

            // 准备数据
            String data = this.potParams(params);
            byte[] data_bytes = data.getBytes();
            
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;");
            conn.setRequestProperty("Content-Length", data_bytes.length + "");
            // POST方式：浏览器将数据以流的方式写入服务器
            conn.setDoOutput(true);// 允许向外部写入数据
            conn.setDoInput(true);
            conn.setUseCaches(true);

            os = conn.getOutputStream();
            os.write(data_bytes);
            conn.setConnectTimeout(10000);
            int statusCode = conn.getResponseCode();
            if (200 == statusCode )
            {
                inStream = conn.getInputStream();
                byte[] dataByte = SystemTools.readInputStream(inStream);
                jsonStr = new String(dataByte);

                //Log.i("HttpUtil Post",url);
                //Log.i("HttpUtil Post", jsonStr);
            } else
            {
                // 获取数据失败
                jsonStr = "{\"resultCode\":50601,\"systemResultCode\":1}";
            }
        } catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            KJLoger.e(e.getMessage());
            // 服务无响应
            jsonStr = "{\"resultCode\":50001,\"systemResultCode\":1}";
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            KJLoger.e(e.getMessage());
            // 服务无响应
            jsonStr = "{\"resultCode\":50001,\"systemResultCode\":1}";
        } finally
        {

            try
            {
                if (null != os)
                {
                    os.close();
                    if (null != inStream)
                    {
                        inStream.close();
                    }
                }
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (null != conn)
            {
                conn.disconnect();
            }

        }

        return jsonStr;
    }

    public byte[] httpPost(String url, String entity) {
        if (url == null || url.length() == 0) {
            KJLoger.i( "httpPost, url is null" );
            return null;
        }

        HttpClient httpClient = getNewHttpClient();

        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new StringEntity (entity));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                KJLoger.i (
                        "httpGet fail, status code = " + resp.getStatusLine ( )
                                                             .getStatusCode ( )
                          );
                return null;
            }

            return EntityUtils.toByteArray ( resp.getEntity ( ) );
        } catch (Exception e) {
            KJLoger.e( "httpPost exception, e = " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx (trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams ();
            HttpProtocolParams.setVersion ( params, HttpVersion.HTTP_1_1 );
            HttpProtocolParams.setContentCharset ( params, HTTP.UTF_8 );

            SchemeRegistry registry = new SchemeRegistry();
            registry.register ( new Scheme ( "http", PlainSocketFactory.getSocketFactory ( ), 80 ) );
            registry.register ( new Scheme ( "https", sf, 443 ) );

            ClientConnectionManager ccm = new ThreadSafeClientConnManager (params, registry);

            return new DefaultHttpClient (ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /**
     * 
     * @方法描述：get请求
     * @方法名：getByHttpConnection
     * @参数：@param url
     * @参数：@return
     * @返回：InputStream
     * @exception
     * @since
     */
    public String doGet(String url)
    {
        HttpURLConnection conn = null;
        InputStream inStream = null;
        String jsonStr = null;
        URL get_url;
        try
        {                   
            get_url = new URL(url);
            conn = (HttpURLConnection) get_url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(10000);
            int statusCode = conn.getResponseCode();
            if (200 == statusCode )
            {
                inStream = conn.getInputStream();
                byte[] dataByte = SystemTools.readInputStream(inStream);
                jsonStr = new String(dataByte);
                
                //Log.i("HttpUtil",url);
                //Log.i("HttpUtil", jsonStr);
            } else
            {
                // 获取数据失败
                jsonStr = "{\"resultCode\":50601,\"systemResultCode\":1}";
            }
        }catch( ConnectTimeoutException ctimeoutex){            
            jsonStr = "{\"resultCode\":50001,\"resultDescription\":\"网络请求超时，请稍后重试\",\"systemResultCode\":1}";
        }catch (SocketTimeoutException stimeoutex) {
            jsonStr = "{\"resultCode\":50001,\"resultDescription\":\"网络请求超时，请稍后重试\",\"systemResultCode\":1}";
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            KJLoger.e(e.getMessage());
            // 服务无响应
            jsonStr = "{\"resultCode\":50001,\"resultDescription\":\"系统请求失败\",\"systemResultCode\":1}";
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            KJLoger.e(e.getMessage());
            // 服务无响应
            jsonStr = "{\"resultCode\":50001,\"resultDescription\":\"系统请求失败\",\"systemResultCode\":1}";
        } finally
        {
            try
            {
                if (null != inStream)
                {
                    inStream.close();
                }
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (null != conn)
            {
                conn.disconnect();
            }
        }

        return jsonStr;
    }

    private String potParams(Map<String, String> map)
    {
        StringBuffer buffer = new StringBuffer();
        Iterator mapI = map.entrySet().iterator();
        while (mapI.hasNext())
        {
            Map.Entry entry = (Map.Entry) mapI.next();

//            buffer.append("&" + entry.getKey() + "=" + entry.getValue());
            try
            {
               String eee = URLEncoder.encode(entry.getValue().toString() , "UTF-8");
                buffer.append("&" + entry.getKey() +"=" + eee );
                
                //Log.i("dedd", eee);
                
            } catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        return buffer.toString().substring(1, buffer.length());
    }

    public void doVolley(Context context, final BaseApplication application, String url ){
        final JsonObjectRequest re = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject >(){


            @Override
            public void onResponse(JSONObject response) {
                JSONUtil<MerchantPayInfo > jsonUtil = new JSONUtil<MerchantPayInfo>();
                MerchantPayInfo merchantPayInfo = new MerchantPayInfo();
                merchantPayInfo = jsonUtil.toBean(response.toString (), merchantPayInfo);
                if(null != merchantPayInfo) {
                    List<MerchantPayInfo.MerchantPayModel> merchantPays = merchantPayInfo.getData ();
                    if ( ! merchantPays.isEmpty ( ) ) {
                        for ( MerchantPayInfo.MerchantPayModel merchantPay : merchantPays) {

                            if(400 == merchantPay.getPayType ())
                            {
                                //支付宝信息
                                application.writeAlipay( merchantPay.getPartnerId (),  merchantPay.getAppKey () );
                            }
                            else if(300 == merchantPay.getPayType ())
                            {
                                //微信支付
                                application.writeWx( merchantPay.getPartnerId (), merchantPay.getAppId (),  merchantPay.getAppKey () );
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        });
        Volley.newRequestQueue ( context ).add( re);
    }

    public void doVolleyName(Context context, final BaseApplication application, String url, final TextView userType ){
        final JsonObjectRequest re = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject >(){


            @Override
            public void onResponse(JSONObject response) {

                JSONUtil<MemberModel > jsonUtil = new JSONUtil<MemberModel>();
                MemberModel memberIfo = new MemberModel();
                memberIfo = jsonUtil.toBean(response.toString (), memberIfo);
                if(null != memberIfo) {
                    MemberModel.MemberInfo member = memberIfo.getData ();
                    if ( null != member ) {
                        //记录会员等级
                        userType.setText ( member.getLevelName () );
                        application.writeMemberLevel ( member.getLevelName () );
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                application.writeMemberLevel ( "普通会员" );
            }


        });
        Volley.newRequestQueue ( context ).add( re);
    }

    public void doVolleyPay(final Activity aty, final Context context, final Handler mHandler, final BaseApplication application, String url, final PayModel payModel ){
        final JsonObjectRequest re = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject >(){


            @Override
            public void onResponse(JSONObject response) {

                JSONUtil<OrderModel > jsonUtil = new JSONUtil<OrderModel>();
                OrderModel orderInfo = new OrderModel();
                orderInfo = jsonUtil.toBean(response.toString (), orderInfo);
                if(null != orderInfo) {
                    OrderModel.OrderData order = orderInfo.getData ();
                    payModel.setAmount ( order.getFinal_Amount () );
                    payModel.setDetail ( order.getTostr () );

                    if ( null != order ) {
                        //支付
                        if("1".equals ( payModel.getPaymentType () ) || "7".equals ( payModel.getPaymentType () ))
                        {
                            //添加支付宝回调路径
                            payModel.setNotifyurl ( application.obtainMerchantUrl ()+"Alipay/Notify.aspx" );
                            //alipay
                            PayFunc payFunc = new PayFunc ( context, payModel, application, mHandler, aty );
                            payFunc.aliPay ();
                        }
                        else if("2".equals ( payModel.getPaymentType () ) || "9".equals ( payModel.getPaymentType () ))
                        {
                            //添加微信回调路径
                            payModel.setAmount ( 100 * payModel.getAmount () );
                            payModel.setNotifyurl ( application.obtainMerchantUrl ()+"Weixin/Notify/PaymentNotifyV3.aspx" );
                            PayFunc payFunc = new PayFunc ( context, payModel, application, mHandler, aty );
                            payFunc.wxPay ( );
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        });
        Volley.newRequestQueue ( context ).add( re);
    }


}
