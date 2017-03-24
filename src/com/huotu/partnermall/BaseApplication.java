package com.huotu.partnermall;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.android.volley.Response;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.huotu.android.library.libpush.PushHelper;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.BuildConfig;
import com.huotu.partnermall.model.ColorBean;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.MerchantBean;
import com.huotu.partnermall.model.RefreshHttpHeaderEvent;
//import com.huotu.partnermall.ui.sis.SisConstant;
import com.huotu.partnermall.utils.CrashHandler;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.widgets.custom.FooterOneConfig;
import com.huotu.partnermall.widgets.custom.PageConfig;
import com.huotu.partnermall.widgets.custom.WidgetConfig;
//import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.eventbus.EventBus;

import java.security.DomainCombiner;
import java.util.ArrayList;
import java.util.List;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static com.huotu.partnermall.config.Constants.MERCHANT_INFO;
import static com.huotu.partnermall.config.Constants.MERCHANT_NAME;
import static com.huotu.partnermall.config.Constants.MERCHANT_WEIXIN_ID;
import static com.huotu.partnermall.config.Constants.WEIXIN_KEY;
import static com.huotu.partnermall.utils.PreferenceHelper.readString;

/**
 * 系统级别的变量、方法
 * Application
 */
public class BaseApplication extends Application {
    //定位类型
    public int localType;
    //地址
    public String address;
    //纬度
    public double latitude;
    //经度
    public double Longitude;
    //是否有网络连接
    //public boolean isConn = false;
    //城市
    public String city;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    /**
     * 是否是左划或者返回
     * true 左划
     * false 返回
     */
    public boolean isLeftImg = true;

    public static BaseApplication single;

    //未读客服消息数量
    public int unReadMessageCount=0;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        single = this;

        //加载异常处理模块
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        if (BuildConfig.DEBUG) {
            //LeakCanary.install(this);//内存检测工具
        }

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        // 初始化Volley实例
        VolleyUtil.init(this);
        // 极光初始化
        // JPushInterface.setDebugMode(true);// 日志，生产环境关闭
        //JPushInterface.init ( this );
        //初始化shareSDK参数
        ShareSDK.initSDK(getApplicationContext());
        solveAsyncTaskOnPostExecuteBug();

        // 极光初始化
        PushHelper.init(this, BuildConfig.DEBUG, BuildConfig.Push_Url);

        //初始化 fresco
        Fresco.initialize(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 解决有些android版本 AsyncTask 无法执行  onPostExecute方法的问题
     *
     * @throws
     * @创建人：jinxiangdong
     * @修改时间：2015年7月7日 上午11:23:32
     * @方法描述：
     * @方法名：solveAsyncTaskOnPostExecuteBug
     * @参数：
     * @返回：void
     */
    protected void solveAsyncTaskOnPostExecuteBug() {
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI() {
        TelephonyManager tm = (TelephonyManager) single.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public String readMemberId() {
        return readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID,"0");
    }

    public void writeMemberId(String userId) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID, userId);
    }

    /**
     * 判断网络是否连接
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;// 网络是否连接
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static String getAppVersion() {
        String version = "0";
        try {
            version = single.getPackageManager().getPackageInfo(single.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(BaseApplication.class.getName(), e.getMessage());
        }
        return version;
    }

    /**
     * 检测APP端是否已经设置商户信息
     *
     * @return
     */
    public boolean checkMerchantInfo() {
        //商户ID
        String merchantId = readString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_INFO_ID);
        String merchantSubId = readString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_INFO_SUBID);
        //商户支付宝key信息
//        String merchantAlipayKey = PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_ALIPAY_KEY);
//        //商户微信支付KEY信息
//        String merchantWeixinKey = PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_WEIXIN_KEY);
//        //商户菜单
//        String merchantMenus = PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_MENUS);
//        //商户类别菜单
//        String merChantCatagory = PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_CATAGORY);

        if ((null == merchantId && null == merchantSubId )  ) {
            //app端未设置商户信息
            return false;
        } else {
            return true;
        }
    }

    public boolean scanWx() {
        String parentId = readString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_MERCHANT_ID);
        String appid = readString(getApplicationContext(), MERCHANT_INFO, MERCHANT_WEIXIN_ID);
        String appKey = readString(getApplicationContext(), MERCHANT_INFO, WEIXIN_KEY);
        String notify = readString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_NOTIFY);

        if (!TextUtils.isEmpty(parentId) && !TextUtils.isEmpty(appid) && !TextUtils.isEmpty(appKey) && !TextUtils.isEmpty(notify)) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 检测支付宝支付信息是否齐全
     * @return
     */
    public boolean scanAliPay() {
        String parentId = readString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_MERCHANT_ID);
        String appid = readString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_APP_ID);
        String appKey = readString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_KEY);
        String notify = readString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_NOTIFY);

        if ( !TextUtils.isEmpty(appid) && !TextUtils.isEmpty(parentId) && !TextUtils.isEmpty(appKey) && !TextUtils.isEmpty(notify)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 写入商户信息到文件
     *
     * @param merchant
     */
    public void writeMerchantInfo(MerchantBean merchant) {
        //商户ID
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_INFO_ID, merchant.getMerchantId());
        //商户subid
        PreferenceHelper.writeString(getApplicationContext(),MERCHANT_INFO,Constants.MERCHANT_SUBID,merchant.getMerchantSubId());
        //版本号
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.APP_VERSION, merchant.getAppVersion());
        //APP名称
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.APP_NAME, merchant.getAppName());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_ALIPAY_ID, merchant.getMerchantAlipayId());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.LOCATION_KEY, merchant.getLocationKey());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.U_MENG_KEY, merchant.getUmengAppkey());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.U_MENG_CHANNEL, merchant.getUmengChannel());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.U_MENG_SECRET, merchant.getUmengMessageSecret());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.SHARE_KEY, merchant.getShareSDKKey());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.TENCENT_KEY, merchant.getTencentKey());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.TENCENT_SECRET, merchant.getTencentSecret());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.SINA_KEY, merchant.getSinaKey());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.SINA_SECRET, merchant.getSinaSecret());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.SINA_REDIRECT_URI, merchant.getSinaRedirectUri());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_SHARE_key, merchant.getWeixinShareKey());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_SHARE_SECRET, merchant.getWeixinShareSecret());
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.PUSH_KEY, merchant.getPushKey());

    }

    /**
     * 记录域名
     *
     * @param domain
     */
    public void writeDomain(String domain) {
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.PREFIX, domain);
    }

    public void writeMenus(List<MenuBean> menus) {
        Gson gson = new Gson();
        String menuStr = gson.toJson(menus);
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_INFO_MENUS, menuStr);
    }

    public void writeMemberInfo(String userName, String userId, String userIcon, String userToken, String unionid , String openid ) {

        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID, userId);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_NAME, userName);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ICON, userIcon == null ? "": userIcon );
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_TOKEN, userToken);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_UNIONID, unionid);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_OPENID , openid );
    }

    public void writeUserUnionId(String unionid) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_UNIONID, unionid);
    }

    public void writeUserToken(String token) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_TOKEN, token);
    }

    public void writeUserName(String userName) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_NAME, userName);
    }

    public void writeUserIcon(String userIcon) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ICON, userIcon);
    }

    //获取用户unionId
    public String readUserUnionId() {
        return readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_UNIONID,"");
    }

    //获取用户编号
    public String readUserId() {
        return readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID,"");
    }

    //获取商户ID
    public String readMerchantId() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_INFO_ID);
    }
    //获取商户的subid
    public String readMerchantSubId(){
        return readString(getApplicationContext(),MERCHANT_INFO,Constants.MERCHANT_SUBID,"");
    }

    public void writeMemberLevelId(int levelid) {
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_LEVELID, levelid);
    }

    //读取 Openid
    public String readOpenId(){
        return readString( getApplicationContext() , Constants.MEMBER_INFO , Constants.MEMBER_OPENID , "");
    }

    public void writeOpenId(String openid){
        PreferenceHelper.writeString( getApplicationContext() , Constants.MEMBER_INFO , Constants.MEMBER_OPENID , openid );
    }

    /**
     * 获取商户的底部菜单信息
     *
     * @return
     */
    public String readMenus() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_INFO_MENUS);
    }

    /**
     * 实现定位回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null == location) {
                return;
            } else {
                localType = location.getLocType();

                if (BDLocation.TypeGpsLocation == localType) {
                    latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                    city = location.getCity();
                    address = location.getAddrStr();
                } else if (BDLocation.TypeNetWorkLocation == localType) {
                    latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                    city = location.getCity();
                    address = location.getAddrStr();
                }
            }

            // 将定位信息写入配置文件
            if (null != city) {
                PreferenceHelper.writeString(getApplicationContext(),
                        Constants.LOCATION_INFO, Constants.CITY, city);
            }
            if (null != address) {
                PreferenceHelper.writeString(getApplicationContext(),
                        Constants.LOCATION_INFO, Constants.ADDRESS, address);
            }
            PreferenceHelper.writeString(getApplicationContext(),
                    Constants.LOCATION_INFO, Constants.LATITUDE,
                    String.valueOf(latitude));
            PreferenceHelper.writeString(getApplicationContext(),
                    Constants.LOCATION_INFO, Constants.LONGITUDE,
                    String.valueOf(Longitude));
        }

    }

    //判断是否登录
    public boolean isLogin() {
        String token = readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_TOKEN);
        if (null != token && !"".equals(token)) {
            return true;
        } else {
            return false;
        }
    }

    //登出
    public void logout() {
//        if( plat ==null ) {
//            plat = ShareSDK.getPlatform(Wechat.NAME);
//        }

        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        //取消授权
        if (null != platform) {
            platform.removeAccount();
        }

        PreferenceHelper.clean(getApplicationContext(), Constants.MEMBER_INFO);

        clearAllCookies();

        EventBus.getDefault().post(new RefreshHttpHeaderEvent() );
    }



    //获取用户图片
    public String getUserLogo() {
        return readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ICON,"");
    }

    //获取用户名称
    public String getUserName() {
        return readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_NAME);
    }

    //判断是否为4.4版本。可设置沉浸模式
    public boolean isKITKAT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    //获取商家的访问渠道
    public String obtainMerchantUrl() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.PREFIX);
    }

    //获取商家的访问渠道
    public String obtainMerchantLogo() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_LOGO);
    }

    //获取商家的访问渠道
    public void writeMerchantLogo(String logo) {
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_LOGO, logo);
    }

    //获取商家的访问渠道
    public String obtainMerchantName() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_NAME);
    }

    //获取商家的访问渠道
    public void writeMerchantName(String name) {
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_NAME, name);
    }

    /**
     * 检测是否已经配置了颜色信息
     *
     * @return
     */
    public boolean checkColorInfo() {
        String mainColor = readString(getApplicationContext(), Constants.COLOR_INFO, Constants.COLOR_MAIN);
        if (TextUtils.isEmpty(mainColor)) {
            return false;
        } else {
            return true;
        }
    }

    public void writeInitInfo(String initStr) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.SYS_INFO, Constants.FIRST_OPEN, initStr);
    }

    //记录会员等级
    public void writeMemberLevel(String level) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_level, level);
    }

    public String readMemberLevel() {
        return readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_level);
    }

    public boolean isFirst() {
        String initInfo = readString(getApplicationContext(), Constants.SYS_INFO, Constants.FIRST_OPEN);
        if (TextUtils.isEmpty(initInfo)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 写入颜色信息
     *
     * @param colorBean
     */
    public void writeColorInfo(ColorBean colorBean) {
        if (null != colorBean) {
            String mainColor = colorBean.getColorMap().get("MAIN_COLOR_1");
            String secondColor = colorBean.getColorMap().get("SECONDART_COLOR_1");
            PreferenceHelper.writeString(getApplicationContext(), Constants.COLOR_INFO, Constants.COLOR_MAIN, mainColor);
            PreferenceHelper.writeString(getApplicationContext(), Constants.COLOR_INFO, Constants.COLOR_SECOND, secondColor);
        }
    }

    public String obtainMainColor() {
        return readString(getApplicationContext(), Constants.COLOR_INFO, Constants.COLOR_MAIN);
    }

    //获取微信key
    public String readWeixinKey() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_SHARE_key);
    }

    //获取微信安全码
    public String readWeixinSecret() {
        return readString(getApplicationContext(), MERCHANT_INFO,
                Constants.WEIXIN_SHARE_SECRET);
    }

    //获取商户信息
    public String readWxParent() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_MERCHANT_ID);
    }

    //获取微信支付的APPID
    public String readWxAppId() {
        return readString(getApplicationContext(), MERCHANT_INFO, MERCHANT_WEIXIN_ID);
    }

    //获取支付宝商户号
    public String readAliMerchant() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_MERCHANT_ID);
    }

    //获取支付宝收款方ID
    public String readMerchantAli() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.MERCHANT_ALIPAY_ID);
    }

    public void writeAlipay( String appId , String parentId, String appKey, String notify, boolean isWebPay , String domain) {
        PreferenceHelper.writeString(getApplicationContext() , MERCHANT_INFO, Constants.ALIPAY_APP_ID , appId );
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_MERCHANT_ID, parentId);
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_KEY, appKey);
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_NOTIFY, notify);
        PreferenceHelper.writeBoolean(getApplicationContext(), MERCHANT_INFO, Constants.IS_WEB_ALIPAY, isWebPay);
        PreferenceHelper.writeString(getApplicationContext(),MERCHANT_INFO , Constants.ALIPAY_DOMAIN , domain);
    }

    public void writeWebAlipay( String appId , String parentId, String appKey, String notify, boolean isWebPay , String domain) {
        PreferenceHelper.writeString(getApplicationContext() , MERCHANT_INFO, Constants.WEB_ALIPAY_APP_ID , appId );
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.WEB_ALIPAY_MERCHANT_ID, parentId);
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.WEB_ALIPAY_KEY, appKey);
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.WEB_ALIPAY_NOTIFY, notify);
        PreferenceHelper.writeBoolean(getApplicationContext(), MERCHANT_INFO, Constants.WEB_ALIPAY_ISWEBPAY, isWebPay);
        PreferenceHelper.writeString(getApplicationContext(),MERCHANT_INFO , Constants.WEB_ALIPAY_DOMAIN , domain);
    }

    public boolean readIsWebAliPay(){
        return PreferenceHelper.readBoolean(getApplicationContext() , MERCHANT_INFO , Constants.IS_WEB_ALIPAY);
    }

    public String readAlipayAppId(){
        return readString(getApplicationContext() , MERCHANT_INFO , Constants.ALIPAY_APP_ID);
    }

    public String readAlipayAppKey() {
        return readString(
                getApplicationContext(), MERCHANT_INFO,
                Constants.ALIPAY_KEY
        );
    }

    public String readAlipayParentId() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_MERCHANT_ID);
    }

    public void writeWx(String parentId, String appId, String appKey, String notify, boolean isWebPay , String domain ) {
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_MERCHANT_ID, parentId);
        PreferenceHelper.writeString(getApplicationContext(), MERCHANT_INFO, MERCHANT_WEIXIN_ID, appId);
        PreferenceHelper.writeString( getApplicationContext(), MERCHANT_INFO, WEIXIN_KEY, appKey );
        PreferenceHelper.writeString( getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_NOTIFY, notify );
        PreferenceHelper.writeBoolean( getApplicationContext(), MERCHANT_INFO, Constants.IS_WEB_WEIXINPAY, isWebPay );
        PreferenceHelper.writeString(getApplicationContext() , MERCHANT_NAME , Constants.WEIXIN_DOMAIN , domain );
    }

    public String readAlipayNotify() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.ALIPAY_NOTIFY);
    }

    public String readAlipayDomain(){
        return readString(getApplicationContext(),MERCHANT_INFO,Constants.ALIPAY_DOMAIN);
    }


    public String readWeixinNotify() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_NOTIFY);
    }

    public String readWeixinDomain(){
        return readString(getApplicationContext(),MERCHANT_INFO,Constants.WEIXIN_DOMAIN);
    }

    //
    public String readWxpayParentId() {
        return readString(getApplicationContext(), MERCHANT_INFO, Constants.WEIXIN_MERCHANT_ID);
    }

    public String readWxpayAppId() {
        return readString(getApplicationContext(), MERCHANT_INFO, MERCHANT_WEIXIN_ID);
    }

    public String readWxpayAppKey() {
        return readString(getApplicationContext(), MERCHANT_INFO,
                WEIXIN_KEY);
    }

    //写入数据包版本号
//    public void writePackageVersion(String packageVersion) {
//        PreferenceHelper.writeString(getApplicationContext(), Constants.DATA_INIT, Constants.PACKAGE_VERSION, packageVersion);
//    }

    //读取数据包版本号
//    public String readPackageVersion() {
//        return readString(getApplicationContext(), Constants.DATA_INIT, Constants.PACKAGE_VERSION);
//    }

    public void writeMemberType(int usertype) {
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_USERTYPE, usertype);
    }

    public int readMemberType() {
        return PreferenceHelper.readInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_USERTYPE);
    }

    public void writePhoneLogin(String loginName, String realName, int relatedType, String authorizeCode, String secure) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_LOGINNAME, loginName);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_REALNAME, realName);
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_RELATEDTYPE, relatedType);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_AUTHORIZECODE, authorizeCode);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_SECURE, secure);
    }

    public void writeMemberRelatedType(int relatedType) {
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_RELATEDTYPE, relatedType);
    }

    //读取 手机登录用户的关联类型
//    public int readMemberRelatedType() {
//        return PreferenceHelper.readInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_RELATEDTYPE, -1);
//    }

    //读取 手机登录 的安全码
    public String readMemberSecure() {
        return readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_SECURE, "");
    }

    //写入 用户登录类型 （1：微信授权登录，2:手机登录）
    public void writeMemberLoginType(int loginType) {
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_LOGINTYPE, loginType);
    }

    //读取 用户登录类型 （1：微信授权登录，2:手机登录）
//    public int readMemberLoginType() {
//        return PreferenceHelper.readInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_LOGINTYPE, 1);
//    }

    public void writeLoginMethod(int loginMethod){
        PreferenceHelper.writeInt( getApplicationContext() , MERCHANT_INFO , Constants.MERCHANT_INFO_LOGINMETHOD , loginMethod );
    }
    public int readLoginMethod(){
        return  PreferenceHelper.readInt( getApplicationContext(), MERCHANT_INFO,Constants.MERCHANT_INFO_LOGINMETHOD , 0 );
    }


    public static void writeNewVersion(int versionid){
        PreferenceHelper.writeInt( single , MERCHANT_INFO , Constants.NEW_APP_VERSION , versionid );
    }

    public static int readNewAppVersion(){
        return  PreferenceHelper.readInt( single , MERCHANT_INFO , Constants.NEW_APP_VERSION , 0 );
    }

    public static void writeAppUrl(String appurl){
        PreferenceHelper.writeString( single , MERCHANT_INFO , Constants.APP_UPDATE_URL , appurl);
    }
    public static String readAppUlr(){
        return readString( single , MERCHANT_INFO , Constants.APP_UPDATE_URL, "");
    }

    public void writeMerchanntWebChannel(String url){
        PreferenceHelper.writeString( getApplicationContext() , MERCHANT_INFO , Constants.MERCHANT_WEBCHANNEL , url  );
    }

    public String readMerchantWebChannel(){
        return readString( getApplicationContext(), MERCHANT_INFO,Constants.MERCHANT_WEBCHANNEL );
    }


    public static int getAppVersionId() {
        int version = 0;
        try {
            version = single.getPackageManager().getPackageInfo(single.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(BaseApplication.class.getName(), e.getMessage());
        }
        return version;
    }


    public void clearAllCookies(){
        CookieManager.getInstance().removeAllCookie();

        //CookieSyncManager.getInstance().sync();
    }

    public static void cleanAliPayInfo(){
        PreferenceHelper.remove( single ,  Constants.MERCHANT_INFO , Constants.ALIPAY_APP_ID);
        PreferenceHelper.remove( single , Constants.MERCHANT_INFO , Constants.ALIPAY_KEY );
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO , Constants.ALIPAY_NOTIFY);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.ALIPAY_MERCHANT_ID);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.IS_WEB_ALIPAY);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.ALIPAY_DOMAIN);

    }

    public static void cleanWebAliPayInfo(){
        PreferenceHelper.remove( single ,  Constants.MERCHANT_INFO , Constants.WEB_ALIPAY_APP_ID);
        PreferenceHelper.remove( single , Constants.MERCHANT_INFO , Constants.WEB_ALIPAY_KEY );
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO , Constants.WEB_ALIPAY_NOTIFY);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.WEB_ALIPAY_MERCHANT_ID);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.WEB_ALIPAY_ISWEBPAY);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.WEB_ALIPAY_DOMAIN);

    }

    public static void cleanWeixinPayInfo(){
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.WEIXIN_MERCHANT_ID);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.MERCHANT_WEIXIN_ID);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.WEIXIN_KEY);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.WEIXIN_NOTIFY);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.IS_WEB_WEIXINPAY);
        PreferenceHelper.remove(single,Constants.MERCHANT_INFO,Constants.WEIXIN_DOMAIN);

    }

    public static void writeBottomMenuConfig(String pageConfig ){
        PreferenceHelper.writeString(single,Constants.BottonConfig_Info , Constants.BottonConfig , pageConfig );
    }

    public static PageConfig readBottomMenuConfig() {
        try {
            String text = PreferenceHelper.readString(single, Constants.BottonConfig_Info, Constants.BottonConfig);
            if (text == null || text.isEmpty()) return null;
            PageConfig pageConfig = JSONUtil.getGson().fromJson(text, PageConfig.class);
            return pageConfig;
        } catch (Exception ex) {
            Log.e(BaseApplication.class.getName(), "readBottomMeneConfig error!");
            return null;
        }
    }



}
