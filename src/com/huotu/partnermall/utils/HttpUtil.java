package com.huotu.partnermall.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.AuthMallModel;
import com.huotu.partnermall.model.MSiteModel;
import com.huotu.partnermall.model.MemberModel;
import com.huotu.partnermall.model.MerchantInfoModel;
import com.huotu.partnermall.model.MerchantPayInfo;
import com.huotu.partnermall.model.OrderModel;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.SwitchUserModel;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.pay.PayFunc;
import com.huotu.partnermall.widgets.NoticePopWindow;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.mob.tools.network.SSLSocketFactoryEx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.AccessControlContext;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.StringUtils;
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
                conn.disconnect ( );
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
            httpPost.setEntity ( new StringEntity ( entity ) );
            httpPost.setHeader ( "Accept", "application/json" );
            httpPost.setHeader ( "Content-type", "application/json" );

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

    /**
     * 根据商户编号获取商户域名
     * @param context
     * @param application
     * @param url
     */
    public void doVolleySite( Context context, final BaseApplication application, String url )
    {
        final JsonObjectRequest re = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject >(){


            @Override
            public void onResponse(JSONObject response) {
                JSONUtil<MSiteModel > jsonUtil = new JSONUtil<MSiteModel>();
                MSiteModel mSite = new MSiteModel();
                mSite = jsonUtil.toBean(response.toString (), mSite);
                if(null != mSite) {
                    MSiteModel.MSiteData mSiteData = mSite.getData ();
                    if ( null != mSiteData.getMsiteUrl () ) {
                        String domain = mSiteData.getMsiteUrl ( );
                        application.writeDomain ( domain );
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

    /**
     * 获取商户logo
     * @param context
     * @param application
     * @param url
     */
    public void doVolleyLogo(Context context, final BaseApplication application, String url)
    {
        final JsonObjectRequest re = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject >(){


            @Override
            public void onResponse(JSONObject response) {

                JSONUtil<MerchantInfoModel > jsonUtil = new JSONUtil<MerchantInfoModel>();
                MerchantInfoModel merchantInfo = new MerchantInfoModel();
                merchantInfo = jsonUtil.toBean(response.toString (), merchantInfo);
                if(null != merchantInfo) {
                    String logo = null;
                    if ( null != merchantInfo.getMall_logo ( ) && null != merchantInfo.getMall_name () ) {
                        if(!TextUtils.isEmpty ( application.obtainMerchantUrl () ))
                        {
                            logo =  application.obtainMerchantUrl () + merchantInfo.getMall_logo ( );
                        }
                        else
                        {
                            logo = merchantInfo.getMall_logo ( );
                        }

                        String name = merchantInfo.getMall_name ( );
                        application.writeMerchantLogo ( logo );
                        application.writeMerchantName ( name );
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
    /**
     * 获取支付信息
     * @param context
     * @param application
     * @param url
     */
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
                                application.writeAlipay( merchantPay.getPartnerId (),  merchantPay.getAppKey (), merchantPay.getNotify () );
                            }
                            else if(300 == merchantPay.getPayType ())
                            {
                                //微信支付
                                application.writeWx( merchantPay.getPartnerId (), merchantPay.getAppId ( ),  merchantPay.getAppKey ( ), merchantPay.getNotify ( ) );
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

    public void doVolley( final Activity aty, final Context context, final Handler mHandler, final BaseApplication application, String url, Map param, final AccountModel account ){
        final GsonRequest re = new GsonRequest (Request.Method.POST, url, AuthMallModel.class, null, param, new Response.Listener<AuthMallModel >(){


            @Override
            public void onResponse(AuthMallModel response) {

                AuthMallModel authMallModel = new AuthMallModel();
                authMallModel = response;
                if(200 == authMallModel.getCode ())
                {
                    AuthMallModel.AuthMall mall = authMallModel.getData ();
                    if(null != mall)
                    {
                        //写入userID
                        account.setAccountId ( String.valueOf ( mall.getUserid ( ) ) );
                        account.setAccountName ( mall.getNickName ( ) );
                        account.setAccountIcon ( mall.getHeadImgUrl ( ) );

                        //和商城用户系统交互
                        application.writeMemberInfo (
                                account.getAccountName ( ), account.getAccountId ( ),
                                account.getAccountIcon ( ), account.getAccountToken ( ),
                                account.getAccountUnionId ( )
                                                    );
                        application.writeMemberLevel ( mall.getLevelName () );
                        //跳转到首页
                        ActivityUtils.getInstance ().skipActivity ( aty, HomeActivity.class );
                    }
                    else
                    {
                        Message msg = new Message();
                        msg.what = Constants.LOGIN_AUTH_ERROR;
                        msg.obj = authMallModel.getMsg ();
                        mHandler.sendMessage ( msg );
                    }

                }
                else if(403 == authMallModel.getCode ())
                {
                    //授权失败
                    Message msg = new Message();
                    msg.what = Constants.LOGIN_AUTH_ERROR;
                    msg.obj = authMallModel.getMsg ();
                    mHandler.sendMessage ( msg );
                }
                else
                {
                    //授权失败
                    Message msg = new Message();
                    msg.what = Constants.LOGIN_AUTH_ERROR;
                    msg.obj = authMallModel.getMsg ();
                    mHandler.sendMessage ( msg );
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Message msg = new Message();
                msg.what = Constants.LOGIN_AUTH_ERROR;
                msg.obj = "调用授权接口失败，请确认！";
                mHandler.sendMessage ( msg );
            }


        });
        Volley.newRequestQueue ( context ).add( re );
    }

    public void doVolleyObtainUser(final Activity aty, final Context context, final BaseApplication application, String url, final View view, final WindowManager wManager, final Handler mHandler)
    {
        final JsonObjectRequest re = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject >(){


            @Override
            public void onResponse(JSONObject response) {
                JSONUtil<SwitchUserModel > jsonUtil = new JSONUtil<SwitchUserModel>();
                SwitchUserModel switchUser = new SwitchUserModel();
                switchUser = jsonUtil.toBean(response.toString (), switchUser);

                if(null != switchUser)
                {
                    List<SwitchUserModel.SwitchUser> userList = switchUser.getData ();
                    if( (null != userList) && (!userList.isEmpty ()) && (userList.size () > 1) )
                    {
                        //去重复数据
                        List<SwitchUserModel.SwitchUser> sourceList = new ArrayList< SwitchUserModel.SwitchUser > (  );
                        sourceList = clearData(userList);
                        //关闭载入数据条
                        mHandler.sendEmptyMessage ( Constants.LOAD_SWITCH_USER_OVER );
                        //弹出切换用户面板
                        SwitchUserPopWin userPop = new SwitchUserPopWin ( aty, sourceList,  application, wManager, mHandler, view );
                        userPop.initView ( );
                        userPop.showAtLocation (
                                view,
                                Gravity.CENTER, 0, 0
                                               );
                        userPop.setOnDismissListener ( new PoponDismissListener ( aty ) );
                    }
                    else if((null != userList) && (!userList.isEmpty ()) && (userList.size () == 1))
                    {
                        //关闭载入数据条
                        mHandler.sendEmptyMessage ( Constants.LOAD_SWITCH_USER_OVER );

                        NoticePopWindow noticePop = new NoticePopWindow ( context, aty, wManager, "无其他账户，请绑定其他账户。");
                        noticePop.showNotice ();
                        noticePop.showAtLocation (
                                view,
                                Gravity.CENTER, 0, 0
                                                 );
                    }
                } else
                {
                    //关闭载入数据条
                    mHandler.sendEmptyMessage ( Constants.LOAD_SWITCH_USER_OVER );

                    NoticePopWindow noticePop = new NoticePopWindow ( context, aty, wManager, "未检测到你的账户信息，请确认。");
                    noticePop.showNotice ();
                    noticePop.showAtLocation (
                            view,
                            Gravity.CENTER, 0, 0
                                             );
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
                        if( TextUtils.isEmpty ( member.getLevelName () ))
                        {
                            userType.setText ( "未设置会员等级" );
                            application.writeMemberLevel ( "未设置会员等级" );
                        }
                        else
                        {
                            userType.setText ( member.getLevelName () );
                            application.writeMemberLevel ( member.getLevelName () );
                        }
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

    public void doVolleyPay(final Activity aty, final Context context, final Handler mHandler, final BaseApplication application, String url, final PayModel payModel, final ProgressPopupWindow payProgress ){
        final JsonObjectRequest re = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject >(){


            @Override
            public void onResponse(JSONObject response) {

                JSONUtil<OrderModel > jsonUtil = new JSONUtil<OrderModel>();
                OrderModel orderInfo = new OrderModel();
                orderInfo = jsonUtil.toBean(response.toString (), orderInfo);
                if(null != orderInfo) {
                    OrderModel.OrderData order = orderInfo.getData ();
                    payModel.setAmount ( (int)(100 * format2Decimal ( order.getFinal_Amount () )) );
                    payModel.setDetail ( order.getTostr () );


                    if ( null != order ) {
                        //支付
                        if("1".equals ( payModel.getPaymentType () ) || "7".equals ( payModel.getPaymentType () ))
                        {
                            //添加支付宝回调路径
                            payModel.setNotifyurl ( application.obtainMerchantUrl () + application.readAlipayNotify ( ) );
                            //alipay
                            PayFunc payFunc = new PayFunc ( context, payModel, application, mHandler, aty, payProgress );
                            payFunc.aliPay ( );

                        }
                        else if("2".equals ( payModel.getPaymentType () ) || "9".equals ( payModel.getPaymentType () ))
                        {
                            payModel.setAttach ( payModel.getCustomId ()+"_0" );
                            //添加微信回调路径
                            payModel.setNotifyurl ( application.obtainMerchantUrl ( ) + application.readWeixinNotify() );
                            PayFunc payFunc = new PayFunc ( context, payModel, application, mHandler, aty, payProgress );
                            payFunc.wxPay ( );

                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                payProgress.dismissView ();
            }


        });
        Volley.newRequestQueue ( context ).add( re);
    }

    /**
     * 清楚重复数据
     * @param dataList
     * @return
     */
    private List<SwitchUserModel.SwitchUser> clearData(List<SwitchUserModel.SwitchUser> dataList)
    {
        if(!dataList.isEmpty()){
            for(int i=0;i<dataList.size();i++){
                for(int j=dataList.size()-1;j>i;j--){
                    if(dataList.get(i).getUserid () == dataList.get(j).getUserid ()){
                        dataList.remove(j);
                    }
                }
            }
        }

        return dataList;
    }

    //保留2位小数
    private double format2Decimal(double d)
    {
        BigDecimal bg = new BigDecimal ( d );
        return bg.setScale ( 2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
