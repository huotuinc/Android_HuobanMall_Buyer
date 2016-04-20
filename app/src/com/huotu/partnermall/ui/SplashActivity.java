package com.huotu.partnermall.ui;

import android.content.ComponentName;
import android.content.Intent;
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

import com.huotu.android.library.buyer.bean.AdBean.AdBannerConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.bean.PageConfig;
import com.huotu.android.library.buyer.bean.WidgetConfig;
import com.huotu.android.library.buyer.bean.WidgetTypeEnum;
import com.huotu.android.library.buyer.utils.GsonUtil;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.ColorBean;
import com.huotu.partnermall.model.MerchantBean;
import com.huotu.partnermall.service.LocationService;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.guide.GuideActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.PropertiesUtil;
import com.huotu.partnermall.utils.XMLParserUtils;
import com.huotu.partnermall.widgets.MsgPopWindow;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    @Bind(R.id.welcomeTips)
    RelativeLayout mSplashItem_iv;
    @Bind(R.id.splash_version)
    TextView tvVersion;
    private Intent locationI = null;
    private boolean isConnection = false;// 假定无网络连接
    private MsgPopWindow popWindow;

    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_DENSITY = metrics.density;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        Constants.SCREEN_WIDTH = metrics.widthPixels;
        //mHandler = new Handler(getMainLooper());
        initView();
    }

    @Override
    protected void initView() {
        String version = getString( R.string.app_name) + BaseApplication.getAppVersion(this);
        tvVersion.setText( version );

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(Constants.ANIMATION_COUNT);// 设置动画显示时间
        mSplashItem_iv.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(
                new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //检测网络
                        isConnection = BaseApplication.checkNet(SplashActivity.this);
                        if (!isConnection) {
                            application.isConn = false;
                            //无网络日志
                            popWindow = new MsgPopWindow(SplashActivity.this, new SettingNetwork(), new CancelNetwork(), "网络连接错误", "请打开你的网络连接！", false);
                            popWindow.showAtLocation(mSplashItem_iv, Gravity.CENTER, 0, 0);
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
                                //写入文件
                                if (null != merchant) {
                                    application.writeMerchantInfo(merchant);
                                } else {
                                    Log.e(TAG, "载入商户信息失败。");
                                }
                            }
                            //设置
                            //加载颜色配置信息
//                            if (!application.checkColorInfo()) {
//                            try {
//                                InputStream is = SplashActivity.this.getAssets().open("color.properties");
//                                ColorBean color = PropertiesUtil.getInstance().readProperties(is);
//                                is.close();
//                                application.writeColorInfo(color);
//                            } catch (IOException e) {
//                                Log.e(TAG, e.getMessage());
//                            }
//                            }else{
//                                String color = application.obtainMainColor();
//                            }

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
                            HttpUtil.getInstance().doVolleyPackage(application, packageUrls);

                            //获取商家域名
                            //获取商户站点
                            String rootUrl = Constants.getINTERFACE_PREFIX() + "mall/getmsiteurl";
                            rootUrl += "?customerId=" + application.readMerchantId();
                            AuthParamUtils paramUtil = new AuthParamUtils(application, System.currentTimeMillis(), rootUrl, SplashActivity.this);
                            final String rootUrls = paramUtil.obtainUrls();
                            HttpUtil.getInstance().doVolleySite(application, rootUrls);
                            //获取商户logo信息
                            String logoUrl = Constants.getINTERFACE_PREFIX() + "mall/getConfig";
                            logoUrl += "?customerId=" + application.readMerchantId();
                            AuthParamUtils paramLogo = new AuthParamUtils(application, System.currentTimeMillis(), logoUrl, SplashActivity.this);
                            final String logoUrls = paramLogo.obtainUrls();
                            HttpUtil.getInstance().doVolleyLogo(application, logoUrls);
                            //获取商户支付信息
                            String targetUrl = Constants.getINTERFACE_PREFIX() + "PayConfig?customerid=";
                            targetUrl += application.readMerchantId();//动态获取商户编号
                            AuthParamUtils paramUtils = new AuthParamUtils(application, System.currentTimeMillis(), targetUrl, SplashActivity.this);
                            final String url = paramUtils.obtainUrls();
                            HttpUtil.getInstance().doVolley(application, url);
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

                                //TESTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT

                                //---------------------------------------------------------

                                //判断是否登录
                                if (application.isLogin()) {
                                    ActivityUtils.getInstance().skipActivity(SplashActivity.this, HomeActivity.class);
                                } else {
                                    ActivityUtils.getInstance().skipActivity(SplashActivity.this, LoginActivity.class);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy ( ) {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (null != locationI){
            stopService(locationI);
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

    protected void Demo(){
        PageConfig pageConfig=new PageConfig();
        //pageConfig.setVersion(2);
        pageConfig.setWidgets(null);
        List<WidgetConfig> widgetConfigs = new ArrayList<>();

        AdBannerConfig config = new AdBannerConfig();
        config.setIsStatic(true);
        config.setAutoPlay(true);
        config.setHeight(60);
        config.setWidth(60);
        List<AdImageBean> urls = new ArrayList<>();
        config.setImages(urls);

        AdImageBean item = new AdImageBean();
        item.setLinkType("");
        item.setLinkUrl("http://res.olquan.cn/resource/images/photo/4471/20160120101325.jpg");
        item.setLinkName("sdf");
        item.setImageUrl("http://res.olquan.cn/resource/images/photo/4471/20160120101325.jpg");
        item.setTitle("sdf");
        urls.add(item);
        item = new AdImageBean();
        item.setLinkType("");
        item.setLinkUrl("http://res.olquan.cn/resource/images/photo/4471/20160120101325.jpg");
        item.setLinkName("sdf");
        item.setImageUrl("http://res.olquan.cn/resource/images/photo/4471/20160120101325.jpg");
        item.setTitle("sdf");
        urls.add(item);
        item = new AdImageBean();
        item.setLinkType("");
        item.setLinkUrl("http://res.olquan.cn/resource/images/photo/4471/20160120101325.jpg");
        item.setLinkName("sdf");
        item.setImageUrl("http://res.olquan.cn/resource/images/photo/4471/20160120101325.jpg");
        item.setTitle("sdf");
        GsonUtil<AdBannerConfig> gsonUtil=new GsonUtil<>();
        String j = gsonUtil.toJson(config);

        WidgetConfig widgetConfig = new WidgetConfig();
        widgetConfig.setType(WidgetTypeEnum.AD_ADBANNER.getIndex());
        //widgetConfig.setProperties( j );

        widgetConfigs.add( widgetConfig);


        if(judgeNativeUIConfigVersion(pageConfig))  return;
    }

    /**
     * 通过服务端返回的原生APP配置版本号，与本地支持的版本号进行比较，
     * 如果本地版本号>=服务器端版本号，则更新本地的配置信息。
     * 如果本地版本号<服务端版本号，则在判断本地配置信息是否存在，
     * 如果存在本地配置信息，则继续使用本地配置信息，否则强制APP升级
     */
    private boolean judgeNativeUIConfigVersion( PageConfig serverUIConfig){
        int localUIVersion = NativeConstants.Version();
        int serverUIVersion = 0;//serverUIConfig.getVersion();
        if( localUIVersion>= serverUIVersion){
            JSONUtil<PageConfig> jsonUtil = new JSONUtil<>();
            String json = jsonUtil.toJson(serverUIConfig);
            PreferenceHelper.writeString( SplashActivity.this , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_KEY , json);
        }else{
            String config = PreferenceHelper.readString(SplashActivity.this, NativeConstants.UI_CONFIG_FILE, NativeConstants.UI_CONFIG_SELF_KEY);
            if( TextUtils.isEmpty( config )){
                forceUpdateApp();
                return true;
            }
            JSONUtil<PageConfig> jsonUtil = new JSONUtil<>();
            PageConfig pageConfig = new PageConfig();
            jsonUtil.toBean(config, pageConfig);
            if(pageConfig.getWidgets()==null|| pageConfig.getWidgets().size()<1){
                forceUpdateApp();
                return true;
            }
        }
        return false;
    }

    /**
     * 强制APP升级
     */
    protected void forceUpdateApp( ){
            boolean isForce = true;
            AppUpdateActivity.UpdateType type = com.huotu.partnermall.ui.AppUpdateActivity.UpdateType.FullUpate;
            String md5 = "";//model.getUpdateMD5();
            String url = "http://dldir1.qq.com/weixin/android/weixin6313android740.apk"; //model.getUpdateUrl();
            String tips = "";// model.getUpdateTips();

            Intent intent = new Intent(SplashActivity.this, com.huotu.partnermall.ui.AppUpdateActivity.class);
            intent.putExtra("isForce", isForce);
            intent.putExtra("type", type);
            intent.putExtra("md5", md5);
            intent.putExtra("url", url);
            intent.putExtra("tips", tips);
            startActivityForResult(intent, NativeConstants.REQUEST_CODE_CLIENT_DOWNLOAD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NativeConstants.REQUEST_CODE_CLIENT_DOWNLOAD
                && resultCode == NativeConstants.RESULT_CODE_CLIENT_DOWNLOAD_FAILED) {
//            Bundle bd = data.getExtras();
//            if (bd != null) {
//                boolean isForce = bd.getBoolean("isForce");
//                if (isForce) {
//                    finish();
//                }
//            }
            finish();
        }
        if (requestCode == NativeConstants.REQUEST_CODE_CLIENT_DOWNLOAD && resultCode == 0) {
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}

