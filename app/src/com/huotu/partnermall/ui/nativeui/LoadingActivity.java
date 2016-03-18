package com.huotu.partnermall.ui.nativeui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import com.huotu.partnermall.model.Native.FindIndexConfig;
import com.huotu.partnermall.service.ApiService;
import com.huotu.partnermall.service.ZRetrofitUtil;
import com.huotu.partnermall.ui.SplashActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.guide.GuideActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
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
        VolleyUtil.getRequestQueue().cancelAll(GsonRequest.TAG);
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


        //Test
        //Intent intent = new Intent(this,NativeActivity.class);
        //this.startActivity(intent);

        getInitData();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.loading_tryagain){
            getInitData();
        }
    }

    protected void getInitData() {
        if (!canConnect()) return;

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
     *
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

    private void getIndexConfig(){
        String rootUrl = PreferenceHelper.readString( BaseApplication.single , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_HREF );
        if( !rootUrl.endsWith("/") ) rootUrl+="/";
        String url = rootUrl + NativeConstants.NATIVECODE_URL;
        url +="?platform=Android&version=000000000000000&osVersion=";
        Map<String,String> headers = new HashMap();
        headers.put(NativeConstants.HEADER_USER_KEY, NativeConstants.NATIVIE_KEY());
        String random = String.valueOf(System.currentTimeMillis());
        headers.put(NativeConstants.HEADER_USER_RANDOM, random );
        String secure = SignUtil.getSecure(NativeConstants.NATIVIE_KEY(), NativeConstants.Native_security(), random);
        headers.put(NativeConstants.HEADER_USER_SECURE, secure );

        GsonRequest<PageConfig> request = new GsonRequest<PageConfig>(
                Request.Method.GET,
                url,
                PageConfig.class,
                headers,
                new nativeCodeListener(LoadingActivity.this),
                new findErrorListener(LoadingActivity.this)
        );

        loading_pbar.setVisibility(View.VISIBLE);
        loading_tryagain.setVisibility(View.GONE);

        VolleyUtil.getRequestQueue().add(request);
    }

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

            ref.get().getIndexConfig();
            //判断是否登录
//            if (BaseApplication.single.isLogin()) {
//                ActivityUtils.getInstance().skipActivity(ref.get(), NativeActivity.class);
//            } else {
//                ActivityUtils.getInstance().skipActivity(ref.get(), LoginActivity.class);
//            }
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
            ToastUtils.showLongToast(ref.get(), "请求异常");
            Logger.e("请求异常");
            ref.get().loading_pbar.setVisibility(View.GONE);
            ref.get().loading_tryagain.setVisibility(View.VISIBLE);
        }
    }

    static class nativeCodeListener implements Response.Listener<PageConfig>{
        WeakReference<LoadingActivity> ref;
        public nativeCodeListener(LoadingActivity act){
            ref =new WeakReference<>(act);
        }
        @Override
        public void onResponse(PageConfig pageConfig ) {
            if (ref.get() == null) return;
            if (pageConfig == null) {
                ToastUtils.showLongToast(ref.get(), "请求失败！");
                ref.get().loading_pbar.setVisibility(View.GONE);
                ref.get().loading_tryagain.setVisibility(View.VISIBLE);
                return;
            }

            JSONUtil<PageConfig> jsonUtil = new JSONUtil<>();
            String json = jsonUtil.toJson(pageConfig);
            PreferenceHelper.writeString( ref.get() , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_KEY , json );

            if(ref.get().startGuideUi()) return;
            //判断是否登录
            if (BaseApplication.single.isLogin()) {
                ActivityUtils.getInstance().skipActivity(ref.get(), NativeActivity.class);
            } else {
                ActivityUtils.getInstance().skipActivity(ref.get(), LoginActivity.class);
            }
        }
    }

    protected void getSiteUrl(){
        //获取商家站点域名地址
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

            BaseApplication.single.writeDomain( mSiteModel.getData().getMsiteUrl() );
            Jlibrary.initSiteUrl( mSiteModel.getData().getMsiteUrl() );
            ref.get().getFindIndexConfigHref();
        }
    }

}
