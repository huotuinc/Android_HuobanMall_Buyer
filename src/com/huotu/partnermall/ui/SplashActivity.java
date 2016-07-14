package com.huotu.partnermall.ui;

import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.ImageUtil;
import com.huotu.partnermall.image.ImageUtils;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.AuthMallModel;
import com.huotu.partnermall.model.ColorBean;
import com.huotu.partnermall.model.MerchantBean;
import com.huotu.partnermall.service.LocationService;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.guide.GuideActivity;
import com.huotu.partnermall.ui.login.PhoneLoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.PropertiesUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.XMLParserUtils;
import com.huotu.partnermall.widgets.MsgPopWindow;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    @Bind(R.id.welcomeTips)
    RelativeLayout mSplashItem_iv;
    @Bind(R.id.splash_version)
    TextView tvVersion;
    private Intent locationI = null;
    private boolean isConnection = false;
    private MsgPopWindow popWindow;
    //推送信息
    Bundle bundlePush;
    Bitmap bitmap;

    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_DENSITY = metrics.density;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        Constants.SCREEN_WIDTH = metrics.widthPixels;
        mHandler = new Handler(getMainLooper());

        //initView();

        loadBackground();
    }

    protected void loadBackground(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.login_bg, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap!=null) {
                                mSplashItem_iv.setBackgroundDrawable(new BitmapDrawable(bitmap));
                            }
                            initView();
                        }
                    });
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSplashItem_iv.setBackgroundColor(SystemTools.obtainColor( BaseApplication.single.obtainMainColor() ));
                            initView();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void initView() {
        //获得推送信息
        if(null!=getIntent() && getIntent().hasExtra( Constants.HUOTU_PUSH_KEY)){
            bundlePush = getIntent().getBundleExtra(Constants.HUOTU_PUSH_KEY);
        }

        String version = getString( R.string.app_name) + BaseApplication.getAppVersion();
        tvVersion.setText( version );

        AlphaAnimation anima = new AlphaAnimation(0.0f, 1.0f);
        anima.setDuration(Constants.ANIMATION_COUNT);// 设置动画显示时间
        mSplashItem_iv.setAnimation(anima);
        anima.setAnimationListener(
                new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //检测网络
                        isConnection = application.checkNet(SplashActivity.this);
                        if (!isConnection) {
                            application.isConn = false;
                            //无网络日志
                            popWindow = new MsgPopWindow(SplashActivity.this, new SettingNetwork(), new CancelNetwork(), "网络连接错误", "请打开你的网络连接！", false);
                            popWindow.showAtLocation( mSplashItem_iv , Gravity.CENTER, 0, 0);
                            popWindow.setOnDismissListener(new PoponDismissListener(SplashActivity.this));
                        } else {
                            application.isConn = true;
                            //定位
                            locationI = new Intent(SplashActivity.this, LocationService.class);
                            SplashActivity.this.startService(locationI);
                            //加载商家信息
                            //判断
                            if (!application.checkMerchantInfo()) {
                                //设置商户信息
                                MerchantBean merchant = XMLParserUtils.getInstance().readMerchantInfo(SplashActivity.this);
                                //Log.i( TAG , "商户信息获取成功。");
                                //写入文件
                                if (null != merchant) {
                                    application.writeMerchantInfo(merchant);
                                } else {
                                    Log.e(TAG,"载入商户信息失败。");
                                }
                            }
                            //设置
                            //加载颜色配置信息
                            if (!application.checkColorInfo()) {
                                try {
                                    InputStream is = SplashActivity.this.getAssets().open("color.properties");
                                    ColorBean color = PropertiesUtil.getInstance().readProperties(is);
                                    application.writeColorInfo(color);
                                    //记录颜色值
                                    //Log.i(TAG,"记录颜色值.");
                                } catch (IOException e) {
                                    Log.e(TAG,e.getMessage());
                                }
                            }

                            //获取数据包更新信息
                            String packageUrl = Constants.getINTERFACE_PREFIX() + "mall/CheckDataPacket";
                            String packageVersion = application.readPackageVersion();
                            if (TextUtils.isEmpty(packageVersion)) {
                                packageVersion = "0.0.1";
                                application.writePackageVersion(packageVersion);
                            }
                            packageUrl += "?customerId=" + application.readMerchantId() + "&dataPacketVersion=" + packageVersion;
                            AuthParamUtils paramPackage = new AuthParamUtils(application, System.currentTimeMillis(), packageUrl, SplashActivity.this);
                            final String packageUrls = paramPackage.obtainUrls();
                            HttpUtil.getInstance().doVolleyPackage( application, packageUrls);

                            //获取商家域名
                            //获取商户站点
//                            String rootUrl = Constants.getINTERFACE_PREFIX() + "mall/getmsiteurl";
//                            rootUrl += "?customerId=" + application.readMerchantId();
//                            AuthParamUtils paramUtil = new AuthParamUtils(application, System.currentTimeMillis(), rootUrl, SplashActivity.this);
//                            final String rootUrls = paramUtil.obtainUrls();
//                            HttpUtil.getInstance().doVolleySite( application, rootUrls);
                            //获取商户logo信息
                            String logoUrl = Constants.getINTERFACE_PREFIX() + "mall/getConfig";
                            logoUrl += "?customerId=" + application.readMerchantId();
                            AuthParamUtils paramLogo = new AuthParamUtils(application, System.currentTimeMillis(), logoUrl, SplashActivity.this);
                            final String logoUrls = paramLogo.obtainUrls();
                            HttpUtil.getInstance().doVolleyLogo(  application, logoUrls);
                            //获取商户支付信息
                            String targetUrl = Constants.getINTERFACE_PREFIX() + "PayConfig?customerid=";
                            targetUrl += application.readMerchantId();
                            AuthParamUtils paramUtils = new AuthParamUtils(application, System.currentTimeMillis(), targetUrl, SplashActivity.this);
                            final String url = paramUtils.obtainUrls();
                            HttpUtil.getInstance().doVolley( application, url);
                            //当用户登录状态时，则重新获得用户信息。
                            initUserInfo();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (application.isConn) {
                            //是否首次安装
                            if (application.isFirst()) {
                                ActivityUtils.getInstance().skipActivity(SplashActivity.this, GuideActivity.class);
                                //写入初始化数据
                                application.writeInitInfo("inited");
                            } else {
                                //判断是否登录
//                                if (application.isLogin()) {
                                    Intent intent = new Intent();
                                    intent.setClass( SplashActivity.this , HomeActivity.class);
                                    if(null!= bundlePush) {
                                        intent.putExtra( Constants.HUOTU_PUSH_KEY , bundlePush);
                                    }
                                    ActivityUtils.getInstance().skipActivity(SplashActivity.this, intent );
//                                } else {
//                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                                    if(null!= bundlePush) {
//                                        intent.putExtra( Constants.HUOTU_PUSH_KEY , bundlePush);
//                                    }
//                                    ActivityUtils.getInstance().skipActivity(SplashActivity.this, intent);
//                                }

                            }
                        }
                    }
                });
    }

    @Override
    protected
    void onResume ( ) {
        super.onResume ( );
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy ( ) {
        super.onDestroy ( );
        ButterKnife.unbind(this);
        if (null != locationI)
        {
            stopService(locationI);
        }
        if( bitmap !=null){
            bitmap.recycle();
        }
    }

    //设置网络点击事件
    private class SettingNetwork implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            // 判断手机系统的版本 即API大于10 就是3.0或以上版本
            if (android.os.Build.VERSION.SDK_INT > 10) {
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName(
                        "com.android.settings",
                        "com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            SplashActivity.this.startActivity(intent);
        }
    }

    //取消设置网络
    private class CancelNetwork implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            popWindow.dismiss();
            // 未设置网络，关闭应用
            closeSelf(SplashActivity.this);
        }
    }

    /**
     * 如果已经登录状态，则重新获得用户信息
     */
    protected void initUserInfo(){
        if( application.isLogin() ) {
            String url = Constants.getINTERFACE_PREFIX() + "Account/getAppUserInfo";
            url += "?userid="+ application.readMemberId()+"&customerid="+ application.readMerchantId();
            AuthParamUtils authParamUtils = new AuthParamUtils(application,  System.currentTimeMillis() , url , this);
            url = authParamUtils.obtainUrl();

            GsonRequest<AuthMallModel> request = new GsonRequest<AuthMallModel>(
                    Request.Method.GET,
                    url,
                    AuthMallModel.class,
                    null,
                    null,
                    new Response.Listener<AuthMallModel>() {
                        @Override
                        public void onResponse(AuthMallModel authMallModel) {

                            if( authMallModel ==null || authMallModel.getCode() !=200 || authMallModel.getData()==null ){
                                //ToastUtils.showLongToast("请求出错。");
                                Log.e(TAG, "请求出错。");
                                return;
                            }

                            AuthMallModel.AuthMall mall = authMallModel.getData();
                            BaseApplication.single.writeMemberId( String.valueOf( mall.getUserid() ));
                            BaseApplication.single.writeUserName( mall.getNickName() );
                            BaseApplication.single.writeUserIcon( mall.getHeadImgUrl() );
                            BaseApplication.single.writeUserUnionId( mall.getUnionId() );
                            BaseApplication.single.writeOpenId( mall.getOpenId());
                            BaseApplication.single.writeMemberLevel(mall.getLevelName());
                            BaseApplication.single.writeMemberLevelId(mall.getLevelId());
                            //记录微信关联类型（0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号）
                            BaseApplication.single.writeMemberRelatedType(mall.getRelatedType());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e( TAG ,  volleyError.getMessage()  );
                            //ToastUtils.showLongToast("啊哦,请求失败了!");
                        }
                    }
            );

            VolleyUtil.getRequestQueue().add(request);

        }else{
        }
    }

}

