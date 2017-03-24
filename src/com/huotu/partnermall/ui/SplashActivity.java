package com.huotu.partnermall.ui;


import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.Advertise;
import com.huotu.partnermall.model.AdvertiseModel;
import com.huotu.partnermall.model.AuthMallModel;
import com.huotu.partnermall.model.ColorBean;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.MerchantBean;
import com.huotu.partnermall.model.MerchantInfoModel;
import com.huotu.partnermall.model.UpdateLeftInfoModel;
import com.huotu.partnermall.service.BussinessBiz;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.guide.GuideActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PropertiesUtil;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.UIUtils;
import com.huotu.partnermall.utils.XMLParserUtils;
import com.huotu.partnermall.widgets.MsgPopWindow;
import com.huotu.partnermall.widgets.custom.FooterOneConfig;
import com.huotu.partnermall.widgets.custom.PageConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.huotu.partnermall.widgets.custom.FooterOneWidget.convertMap;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    @Bind(R.id.welcomeTips)
    RelativeLayout rlSplashItem;
    @Bind(R.id.splash_version)
    TextView tvVersion;
    @Bind(R.id.ivGif)
    SimpleDraweeView ivGif;

    private boolean isConnection = false;
    private MsgPopWindow popWindow;
    //推送信息
    Bundle bundlePush;
    Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mHandler = new Handler(getMainLooper());
        initView();
    }

    @Override
    protected void initView() {
        //获得推送信息
        if (null != getIntent() && getIntent().hasExtra(Constants.HUOTU_PUSH_KEY)) {
            bundlePush = getIntent().getBundleExtra(Constants.HUOTU_PUSH_KEY);
        }

        String appVersionShow = getString(R.string.appVersion_show);
        if (Boolean.parseBoolean(appVersionShow)) {
            String version = getString(R.string.app_name) + BaseApplication.getAppVersion();
            tvVersion.setText(version);
        }

        initGif();

        getFooterConfig();//获得底部导航栏配置信息

        AlphaAnimation anima = new AlphaAnimation(0.0f, 1.0f);
        anima.setDuration(Constants.ANIMATION_COUNT);// 设置动画显示时间
        rlSplashItem.setAnimation(anima);
        anima.setAnimationListener(
                new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //检测网络
                        isConnection = BaseApplication.checkNet(SplashActivity.this);
                        if (!isConnection) {
                            //无网络日志
                            popWindow = new MsgPopWindow(SplashActivity.this, new SettingNetwork(), new CancelNetwork(), "网络连接错误", "请打开你的网络连接！", false);
                            popWindow.showAtLocation(rlSplashItem, Gravity.CENTER, 0, 0);
                            popWindow.setOnDismissListener(new PoponDismissListener(SplashActivity.this));
                        } else {
                            //加载商家信息
                            //判断
                            //if (!application.checkMerchantInfo()) {
                                //设置商户信息
                                MerchantBean merchant = XMLParserUtils.getInstance().readMerchantInfo(SplashActivity.this);
                                //Log.i( TAG , "商户信息获取成功。");
                                //写入文件
                                if (null != merchant) {
                                    application.writeMerchantInfo(merchant);
                                } else {
                                    Log.e(TAG, "载入商户信息失败。");
                                }
                            //}

                            //加载颜色配置信息
                            try {
                                InputStream is = SplashActivity.this.getAssets().open("color.properties");
                                ColorBean color = PropertiesUtil.getInstance().readProperties(is);
                                application.writeColorInfo(color);
                                //记录颜色值
                                //Log.i(TAG,"记录颜色值.");
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                            }

                            getLeftMenu();

                            //获取商户支付信息
                            //String targetUrl = Constants.getINTERFACE_PREFIX() + "PayConfig?customerid=";
                            String targetUrl = Constants.getINTERFACE_PREFIX() + "payconfig/IndexMall?customerid=";
                            //String targetUrl = Constants.getINTERFACE_PREFIX() + "mall/InitMall?customerid=";

                            targetUrl += application.readMerchantId();
                            AuthParamUtils paramUtils = new AuthParamUtils(application, System.currentTimeMillis(), targetUrl);
                            final String url = paramUtils.obtainUrls();
                            HttpUtil.getInstance().doVolley(application, url);
                            //当用户登录状态时，则重新获得用户信息。
                            initUserInfo();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        //必须重新方法
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (isConnection) {
                            getConfigData();
                        }
                    }
                });
    }

    void initGif() {
        Uri uri = Uri.parse("res://" + this.getPackageName() + "/" + R.drawable.login_bg);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        ivGif.setController(controller);
    }

    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    //设置网络点击事件
    private class SettingNetwork implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent;
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

            if(intent.resolveActivity( getPackageManager() ) !=null) {
                SplashActivity.this.startActivity(intent);
            }

            if (popWindow != null) {
                popWindow.dismiss();
                popWindow = null;
            }
            SplashActivity.this.finish();
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
    protected void initUserInfo() {
        if (application.isLogin()) {
            String url = Constants.getINTERFACE_PREFIX() + "Account/getAppUserInfo";
            url += "?userid=" + application.readMemberId() + "&customerid=" + application.readMerchantId();
            AuthParamUtils authParamUtils = new AuthParamUtils(application, System.currentTimeMillis(), url);
            url = authParamUtils.obtainUrl();

            GsonRequest<AuthMallModel> request = new GsonRequest<>(
                    Request.Method.GET,
                    url,
                    AuthMallModel.class,
                    null,
                    null,
                    new Response.Listener<AuthMallModel>() {
                        @Override
                        public void onResponse(AuthMallModel authMallModel) {

                            if (authMallModel == null || authMallModel.getCode() != 200 || authMallModel.getData() == null) {
                                Log.e(TAG, "请求出错。");
                                BaseApplication.single.logout();
                                return;
                            }

                            AuthMallModel.AuthMall mall = authMallModel.getData();
                            BaseApplication.single.writeMemberId(String.valueOf(mall.getUserid()));
                            BaseApplication.single.writeUserName(mall.getNickName());
                            BaseApplication.single.writeUserIcon(mall.getHeadImgUrl());
                            BaseApplication.single.writeUserUnionId(mall.getUnionId());
                            BaseApplication.single.writeOpenId(mall.getOpenId());
                            BaseApplication.single.writeMemberLevel(mall.getLevelName());
                            BaseApplication.single.writeMemberLevelId(mall.getLevelId());
                            //记录微信关联类型（0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号）
                            BaseApplication.single.writeMemberRelatedType(mall.getRelatedType());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }
            );

            VolleyUtil.getRequestQueue().add(request);

        } else {
            //未登录状态绑定设备
            UIUtils.bindPushDevice();
        }
    }

    private void getLeftMenu() {
        String url = Constants.getINTERFACE_PREFIX() + "weixin/UpdateLeftInfo";
        url += "?customerId=" + application.readMerchantId();
        url += "&userId=" + (TextUtils.isEmpty(application.readMemberId()) ? "0" : application.readMemberId());
        url += "&clientUserType=" + application.readMemberType();
        AuthParamUtils authParamUtils = new AuthParamUtils(application, System.currentTimeMillis(), url);
        url = authParamUtils.obtainUrlName();
        GsonRequest<UpdateLeftInfoModel> request = new GsonRequest<>(
                Request.Method.GET,
                url,
                UpdateLeftInfoModel.class,
                null,
                new MyRefreshMenuListener(SplashActivity.this),
                new MyRefreshMenuErrorListener(SplashActivity.this));

        VolleyUtil.getRequestQueue().add(request);
    }

    static class MyRefreshMenuListener implements Response.Listener<UpdateLeftInfoModel> {
        WeakReference<SplashActivity> ref;

        public MyRefreshMenuListener(SplashActivity aty) {
            ref = new WeakReference<>(aty);
        }

        @Override
        public void onResponse(UpdateLeftInfoModel updateLeftInfoModel) {
            if (ref.get() == null) return;
            if (updateLeftInfoModel == null) return;

            if (updateLeftInfoModel.getCode() != 200) {
                ToastUtils.showShortToast(ref.get().application, updateLeftInfoModel.getMsg());
                return;
            }

            if (updateLeftInfoModel.getData() == null) return;
            if (updateLeftInfoModel.getData().getMenusCode() == 0) return;

            BaseApplication.single.writeMemberLevel(updateLeftInfoModel.getData().getLevelName());

            //设置侧滑栏菜单
            List<MenuBean> menus = new ArrayList<>();
            MenuBean menu;
            List<UpdateLeftInfoModel.MenuModel> homeMenus = updateLeftInfoModel.getData().getHome_menus();
            for (UpdateLeftInfoModel.MenuModel home_menu : homeMenus) {
                menu = new MenuBean();
                menu.setMenuGroup(String.valueOf(home_menu.getMenu_group()));
                menu.setMenuIcon(home_menu.getMenu_icon());
                menu.setMenuName(home_menu.getMenu_name());
                menu.setMenuUrl(home_menu.getMenu_url());
                menus.add(menu);
            }
            if (!menus.isEmpty()) {
                BaseApplication.single.writeMenus(menus);
            }
        }
    }

    static class MyRefreshMenuErrorListener implements Response.ErrorListener {
        WeakReference<SplashActivity> ref;

        public MyRefreshMenuErrorListener(SplashActivity aty) {
            ref = new WeakReference<>(aty);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (ref.get() == null) return;
        }
    }

    /***
     * 获得广告数据
     */
    void getAdData() {
        String url = Constants.getINTERFACE_PREFIX() + "advert/get?customerId=" + application.readMerchantId() + "&type=2&count=5";
        AuthParamUtils authParamUtils = new AuthParamUtils(application, System.currentTimeMillis(), url);
        url = authParamUtils.obtainUrl();
        GsonRequest<AdvertiseModel> request = new GsonRequest<>(
                Request.Method.GET,
                url,
                AdvertiseModel.class,
                null,
                null,
                new Response.Listener<AdvertiseModel>() {
                    @Override
                    public void onResponse(AdvertiseModel advertiseModel) {

                        if (advertiseModel == null || advertiseModel.getCode() != 200 || advertiseModel.getData() == null || advertiseModel.getData().size() < 1) {
                            Log.e(TAG, "请求出错。");
                            gotoHome();
                            return;
                        }

                        gotoAdvertise(advertiseModel.getData());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        gotoHome();
                    }
                }
        );

        VolleyUtil.getRequestQueue().add(request);
    }

    void gotoAdvertise(List<Advertise> model) {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, AdActivity.class);
        if (null != bundlePush) {
            intent.putExtra(Constants.HUOTU_PUSH_KEY, bundlePush);
        }
        intent.putExtra(Constants.HUOTU_AD_KEY, (Serializable) model);
        ActivityUtils.getInstance().skipActivity(SplashActivity.this, intent);
    }

    void gotoHome() {
        //是否首次安装
        if (application.isFirst()) {
            ActivityUtils.getInstance().skipActivity(SplashActivity.this, GuideActivity.class);
            //写入初始化数据
            application.writeInitInfo("inited");
        } else {
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, HomeActivity.class);
            if (null != bundlePush) {
                intent.putExtra(Constants.HUOTU_PUSH_KEY, bundlePush);
            }
            ActivityUtils.getInstance().skipActivity(SplashActivity.this, intent);
        }
    }

    void getConfigData() {
        String logoUrl = Constants.getINTERFACE_PREFIX() + "mall/getConfig";
        logoUrl += "?customerId=" + application.readMerchantId()+"&merchantId="+ application.readMerchantSubId();
        AuthParamUtils paramLogo = new AuthParamUtils(application, System.currentTimeMillis(), logoUrl);
        final String logoUrls = paramLogo.obtainUrls();
        GsonRequest<MerchantInfoModel> request = new GsonRequest<>(
                Request.Method.GET,
                logoUrls,
                MerchantInfoModel.class,
                null,
                null,
                new Response.Listener<MerchantInfoModel>() {
                    @Override
                    public void onResponse(MerchantInfoModel merchantInfoModel) {

                        if (merchantInfoModel == null) {
                            Log.e(TAG, "请求出错。");
                            gotoHome();
                            return;
                        }

                        String site = merchantInfoModel.getMall_site();
                        application.writeDomain(site);
                        String logo = null;
                        if (null != merchantInfoModel.getMall_logo() && null != merchantInfoModel.getMall_name()) {
                            if (!TextUtils.isEmpty(application.obtainMerchantUrl())) {
                                logo = application.obtainMerchantUrl() + merchantInfoModel.getMall_logo();
                            } else {
                                logo = merchantInfoModel.getMall_logo();
                            }

                            String name = merchantInfoModel.getMall_name();
                            application.writeMerchantLogo(logo);
                            application.writeMerchantName(name);

                            //记录登录配置方式
                            application.writeLoginMethod(merchantInfoModel.getAccountModel());
                            //记录服务器app最新版本信息
                            BaseApplication.writeNewVersion(merchantInfoModel.getVersionnumber());
                            BaseApplication.writeAppUrl(merchantInfoModel.getApplinkurl());

                            //记录客服地址
                            String webChannel = merchantInfoModel.getWebchannel();
                            String webChannelStr = "";
                            if (!TextUtils.isEmpty(webChannel)) {
                                try {
                                    webChannelStr = URLDecoder.decode(webChannel);
                                } catch (Exception ex) {
                                    Log.e("getconfig", ex.getMessage());
                                }
                            }
                            application.writeMerchanntWebChannel(webChannelStr);
                        }

                        if (merchantInfoModel.getAppad() == null || merchantInfoModel.getAppad().size() < 1) {
                            gotoHome();
                            return;
                        }
                        gotoAdvertise(merchantInfoModel.getAppad());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        gotoHome();
                    }
                }
        );

        VolleyUtil.getRequestQueue().add(request);
    }

    /***
     * 获得底部导航栏配置信息
     */
    void getFooterConfig() {
        try {
            BussinessBiz bussinessBiz = new BussinessBiz();
            MyGetFooterConfigListener myGetFooterConfigListener = new MyGetFooterConfigListener();
            MyGetFooterConfigErrorListener myGetFooterConfigErrorListener = new MyGetFooterConfigErrorListener();
            bussinessBiz.getFooterConfig(myGetFooterConfigListener, myGetFooterConfigErrorListener);
        }catch (Exception ex){
            Log.e(SplashActivity.TAG , "getFooterConfig Error!");
        }
    }

    public static class MyGetFooterConfigListener implements Response.Listener<PageConfig> {
        @Override
        public void onResponse(PageConfig pageConfig) {
            String text = JSONUtil.getGson().toJson(pageConfig);
            BaseApplication.single.writeBottomMenuConfig(text);
        }
    }

    public static class MyGetFooterConfigErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e(SplashActivity.TAG, volleyError.getMessage() == null ? "MyGetFooterConfig Error" : volleyError.getMessage());
        }
    }
}

