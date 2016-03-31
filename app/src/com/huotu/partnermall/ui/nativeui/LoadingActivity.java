package com.huotu.partnermall.ui.nativeui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.android.library.buyer.Jlibrary;
import com.huotu.android.library.buyer.bean.PageConfig;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.image.ImageUtils;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.MSiteModel;
import com.huotu.partnermall.model.MerchantInfoModel;
import com.huotu.partnermall.model.MerchantPayInfo;
import com.huotu.partnermall.model.Native.FindIndexConfig;
import com.huotu.partnermall.service.ApiService;
import com.huotu.partnermall.service.ZRetrofitUtil;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.guide.GuideActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.BuyerSignUtil;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 启动界面
 */
public class LoadingActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.loading_root)
    RelativeLayout loading_root;
    @Bind(R.id.loading_tryagain)
    Button loading_tryagain;
    @Bind(R.id.loading_pbar)
    ProgressBar loading_pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        //VolleyUtil.getRequestQueue().cancelAll(GsonRequest.TAG);
    }

    @Override
    protected void initView() {
        loading_tryagain.setOnClickListener(this);
        //if(!canConnect()) return;
        //if(startGuideUi()) return;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_DENSITY = metrics.density;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        Constants.SCREEN_WIDTH = metrics.widthPixels;

        BitmapDrawable bitmapDrawable = (BitmapDrawable)ContextCompat.getDrawable( this , R.drawable.login_bg ) ;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap bitmap2 = ImageUtils.resizeImageByWidth ( bitmap , Constants.SCREEN_WIDTH );
        SystemTools.loadBackground( loading_root , new BitmapDrawable(getResources(), bitmap2));

        getInitData();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.loading_tryagain){
            getInitData();
        }
    }

    protected void getInitData() {
        if (!canConnect()) {
            loading_pbar.setVisibility(View.GONE);
            loading_tryagain.setVisibility(View.VISIBLE);
            return;
        }

        getSiteUrl();
        //String hrefUrl = PreferenceHelper.readString( BaseApplication.single , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_HREF );
        //if( TextUtils.isEmpty(hrefUrl)) {
        //getFindIndexConfigHref();
        //}else{
        //    getIndexConfig();
        //}
    }

    /**
     * 判断是否第一次打开App，如果是，则显示引导页面
     */
    protected boolean startGuideUi() {
        String txt = PreferenceHelper.readString(LoadingActivity.this, Constants.SYS_INFO, Constants.FIRST_OPEN);
        if (TextUtils.isEmpty(txt)) {
            ActivityUtils.getInstance().skipActivity(LoadingActivity.this, GuideActivity.class);
            //写入初始化数据
            application.writeInitInfo("inited");
            return true;
        }
        //判断是否登录
//        if (application.isLogin()) {
//            ActivityUtils.getInstance().skipActivity(LoadingActivity.this, NativeActivity.class);
//        } else {
//            ActivityUtils.getInstance().skipActivity(LoadingActivity.this, LoginActivity.class);
//        }
        return false;
    }

    /**
     * 通过 api接口获得 商城首页的 组件配置信息
     */
    protected void getFindIndexConfigHref(){
        String url = NativeConstants.FINDINDEX_URL;
        url +="?merchantId=" + NativeConstants.CUSTOMERID();

        Map<String,String> headers = new HashMap();
        headers.put(NativeConstants.HEADER_USER_KEY, NativeConstants.NATIVIE_KEY());
        String random = String.valueOf(System.currentTimeMillis());
        headers.put(NativeConstants.HEADER_USER_RANDOM, random );
        String secure = SignUtil.getSecure(NativeConstants.NATIVIE_KEY(), NativeConstants.Native_security(), random);
        headers.put(NativeConstants.HEADER_USER_SECURE, secure );

        GsonRequest<FindIndexConfig> findIndexConfigGsonRequest = new GsonRequest<>(
                Request.Method.GET,
                url,
                FindIndexConfig.class,
                headers,
                new findListener(LoadingActivity.this),
                new findErrorListener(LoadingActivity.this)
        );

        loading_pbar.setVisibility(View.VISIBLE);
        loading_tryagain.setVisibility(View.GONE);

        VolleyUtil.getRequestQueue().add(findIndexConfigGsonRequest );
    }

//    private void getIndexConfig(){
//        String rootUrl = PreferenceHelper.readString( BaseApplication.single , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_HREF );
//        if( !rootUrl.endsWith("/") ) rootUrl+="/";
//        String url = rootUrl + NativeConstants.NATIVECODE_URL;
//        url +="?platform=Android&version=000000000000000&osVersion=";
//        Map<String,String> headers = new HashMap();
//        headers.put(NativeConstants.HEADER_USER_KEY, NativeConstants.NATIVIE_KEY());
//        String random = String.valueOf(System.currentTimeMillis());
//        headers.put(NativeConstants.HEADER_USER_RANDOM, random );
//        String secure = SignUtil.getSecure(NativeConstants.NATIVIE_KEY(), NativeConstants.Native_security(), random);
//        headers.put(NativeConstants.HEADER_USER_SECURE, secure );
//
//        GsonRequest<PageConfig> request = new GsonRequest<PageConfig>(
//                Request.Method.GET,
//                url,
//                PageConfig.class,
//                headers,
//                new nativeCodeListener(LoadingActivity.this , rootUrl ),
//                new findErrorListener(LoadingActivity.this)
//        );
//
//        loading_pbar.setVisibility(View.VISIBLE);
//        loading_tryagain.setVisibility(View.GONE);
//
//        VolleyUtil.getRequestQueue().add(request);
//    }

    static class findListener implements Response.Listener<FindIndexConfig>{
        WeakReference<LoadingActivity> ref;
        public findListener(LoadingActivity act){
            ref =new WeakReference<>(act);
        }
        @Override
        public void onResponse(FindIndexConfig findIndexConfig) {
            if (ref.get() == null) return;
            //ref.get().dismissProgress();
            if (findIndexConfig == null) {
                ToastUtils.showLongToast(ref.get(), "请求失败！");
                ref.get().loading_pbar.setVisibility(View.GONE);
                ref.get().loading_tryagain.setVisibility(View.VISIBLE);
                return;
            }

            if (findIndexConfig.get_links() == null ||
                    findIndexConfig.get_links().getSelf() == null ||
                    TextUtils.isEmpty(findIndexConfig.get_links().getSelf().getHref())) {
                ToastUtils.showLongToast(ref.get(), "首页配置地址空！");
                ref.get().loading_pbar.setVisibility(View.GONE);
                ref.get().loading_tryagain.setVisibility(View.VISIBLE);
                return;
            }
            String selfhref = findIndexConfig.get_links().getSelf().getHref();
            PreferenceHelper.writeString(BaseApplication.single, NativeConstants.UI_CONFIG_FILE, NativeConstants.UI_CONFIG_SELF_HREF, selfhref);

            //ref.get().getIndexConfig();
            if(ref.get().startGuideUi()) return;
            //判断是否登录
            if (BaseApplication.single.isLogin()) {
                Bundle bd = new Bundle();
                String url = selfhref;
                bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, url);
                bd.putBoolean(NativeConstants.KEY_ISMAINUI, true);
                ActivityUtils.getInstance().skipActivity(ref.get(), NativeActivity.class, bd);
            } else {
                ActivityUtils.getInstance().skipActivity(ref.get(), LoginActivity.class);
            }
        }
    }

    static class findErrorListener implements Response.ErrorListener {
        WeakReference<LoadingActivity> ref;

        public findErrorListener(LoadingActivity act) {
            ref = new WeakReference<>(act);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

            if (ref.get() == null) return;
            //ref.get().dismissProgress();
            ToastUtils.showLongToast(ref.get(), "请求异常,请重试");
            Logger.e("请求异常");
            ref.get().loading_pbar.setVisibility(View.GONE);
            ref.get().loading_tryagain.setVisibility(View.VISIBLE);
        }
    }

//    static class nativeCodeListener implements Response.Listener<PageConfig>{
//        WeakReference<LoadingActivity> ref;
//        String smartuiconfigurl;
//        public nativeCodeListener(LoadingActivity act , String url ){
//            ref =new WeakReference<>(act);
//            smartuiconfigurl = url;
//        }
//        @Override
//        public void onResponse(PageConfig pageConfig ) {
//            if (ref.get() == null) return;
//            if (pageConfig == null) {
//                ToastUtils.showLongToast(ref.get(), "请求失败！");
//                ref.get().loading_pbar.setVisibility(View.GONE);
//                ref.get().loading_tryagain.setVisibility(View.VISIBLE);
//                return;
//            }
//
//            JSONUtil<PageConfig> jsonUtil = new JSONUtil<>();
//            String json = jsonUtil.toJson(pageConfig);
//            PreferenceHelper.writeString( ref.get() , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_KEY , json );
//
//            if(ref.get().startGuideUi()) return;
//            //判断是否登录
//            if (BaseApplication.single.isLogin()) {
//                Bundle bd = new Bundle();
//                String url = smartuiconfigurl;
//                bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, url);
//                ActivityUtils.getInstance().skipActivity(ref.get(), NativeActivity.class, bd);
//            } else {
//                ActivityUtils.getInstance().skipActivity(ref.get(), LoginActivity.class);
//            }
//        }
//    }

    /**
     * 获取商家站点域名地址
     */
    protected void getSiteUrl(){
        String rootUrl = Constants.getINTERFACE_PREFIX() + "mall/getmsiteurl";
        rootUrl += "?customerId=" + application.readMerchantId();
        AuthParamUtils paramUtil = new AuthParamUtils(application, System.currentTimeMillis(), rootUrl, LoadingActivity.this);
        String url = paramUtil.obtainUrls();
        GsonRequest<MSiteModel> request = new GsonRequest<MSiteModel>(
                Request.Method.GET,
                url,
                MSiteModel.class,
                null,
                new getSiteListener(LoadingActivity.this),
                new findErrorListener(LoadingActivity.this)
        );

        loading_pbar.setVisibility(View.VISIBLE);
        loading_tryagain.setVisibility(View.GONE);

        VolleyUtil.getRequestQueue().add(request);
    }

    static class getSiteListener implements Response.Listener<MSiteModel>{
        WeakReference<LoadingActivity> ref;
        public getSiteListener(LoadingActivity act){
            ref =new WeakReference<>(act);
        }
        @Override
        public void onResponse(MSiteModel  mSiteModel ) {
            if (ref.get() == null) return;
            if (mSiteModel == null || mSiteModel.getCode()!=200) {
                ToastUtils.showLongToast(ref.get(), "请求失败！");
                ref.get().loading_pbar.setVisibility(View.GONE);
                ref.get().loading_tryagain.setVisibility(View.VISIBLE);
                return;
            }

            BaseApplication.single.writeDomain(mSiteModel.getData().getMsiteUrl());
            Jlibrary.initSiteUrl( mSiteModel.getData().getMsiteUrl() );
            //ref.get().getFindIndexConfigHref();
            //ref.get().getPayConfig();
            ref.get().getMallInfo();
        }
    }

    /**
     * 获得 商城配置信息
     */
    protected void getMallInfo(){
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String version = BaseApplication.getAppVersion(LoadingActivity.this);
        String operation = Constants.OPERATION_CODE;
        String timestamp = String.valueOf(System.currentTimeMillis());
        String appid="";
        String customerId = BaseApplication.single.readMerchantId();
        try {
            appid = URLEncoder.encode(Constants.getAPP_ID(), "UTF-8");
        }catch (UnsupportedEncodingException ex){}

        Map<String,String> map = new HashMap<>();
        map.put("version", version);
        map.put("operation",operation);
        map.put("timestamp",timestamp);
        map.put("appid",appid);
        map.put("customerid",customerId);
        String sign = BuyerSignUtil.getSign(map);
        Call<MerchantInfoModel> call = apiService.getMallConfig(version, operation, timestamp, appid, sign, customerId);
        call.enqueue(new Callback<MerchantInfoModel>() {
            @Override
            public void onResponse(retrofit2.Response<MerchantInfoModel> response) {
                if (response == null || response.code() != 200 || response.body() == null) {
                    ToastUtils.showLongToast(LoadingActivity.this, "请求失败！");
                    loading_pbar.setVisibility(View.GONE);
                    loading_tryagain.setVisibility(View.VISIBLE);
                    return;
                }

                String logo;
                if ( null != response.body().getMall_logo() && null != response.body().getMall_name() ) {
                    if(!TextUtils.isEmpty ( application.obtainMerchantUrl () ))
                    {
                        logo =  application.obtainMerchantUrl () + response.body().getMall_logo();
                    }
                    else
                    {
                        logo = response.body().getMall_logo();
                    }

                    String name = response.body().getMall_name();
                    application.writeMerchantLogo(logo);
                    application.writeMerchantName(name);
                }

                getPayConfig();
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t.getMessage());
                ToastUtils.showLongToast(LoadingActivity.this , "请求异常,请重试");
                loading_pbar.setVisibility(View.GONE);
                loading_tryagain.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 获取商户支付信息
    */
    protected void getPayConfig(){
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String version = BaseApplication.getAppVersion(LoadingActivity.this);
        String operation = Constants.OPERATION_CODE;
        String timestamp = String.valueOf(System.currentTimeMillis());
        String appid="";
        String customerId = BaseApplication.single.readMerchantId();
        try {
            appid = URLEncoder.encode(Constants.getAPP_ID(), "UTF-8");
        }catch (UnsupportedEncodingException ex){}

        Map<String,String> map = new HashMap<>();
        map.put("version", version);
        map.put("operation",operation);
        map.put("timestamp",timestamp);
        map.put("appid",appid);
        map.put("customerid",customerId);

        String sign = BuyerSignUtil.getSign(map);
        Call<MerchantPayInfo> call = apiService.payConfig(version , operation , timestamp ,appid , sign , customerId );
        call.enqueue(new Callback<MerchantPayInfo>() {
            @Override
            public void onResponse(retrofit2.Response<MerchantPayInfo> response) {
                if (response == null || response.code() != 200 || response.body() == null) {
                    ToastUtils.showLongToast(LoadingActivity.this, "请求失败！");
                    loading_pbar.setVisibility(View.GONE);
                    loading_tryagain.setVisibility(View.VISIBLE);
                    return;
                }

                List<MerchantPayInfo.MerchantPayModel> merchantPays = response.body().getData();
                if (!merchantPays.isEmpty()) {
                    for (MerchantPayInfo.MerchantPayModel merchantPay : merchantPays) {
                        if (400 == merchantPay.getPayType()) {
                            //支付宝信息
                            application.writeAlipay(merchantPay.getPartnerId(), merchantPay.getAppKey(), merchantPay.getNotify(), merchantPay.isWebPagePay());
                        } else if (300 == merchantPay.getPayType()) {
                            //微信支付
                            application.writeWx(merchantPay.getPartnerId(), merchantPay.getAppId(), merchantPay.getAppKey(), merchantPay.getNotify(), merchantPay.isWebPagePay());
                        }
                    }
                }

                getFindIndexConfigHref();
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t.getMessage());
            }
        });
    }
}
