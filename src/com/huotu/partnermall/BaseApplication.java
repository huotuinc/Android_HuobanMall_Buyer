package com.huotu.partnermall;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.model.ColorBean;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.MerchantBean;
import com.huotu.partnermall.model.PageInfoModel;
import com.huotu.partnermall.model.PageType;
import com.huotu.partnermall.model.SysModel;
import com.huotu.partnermall.utils.CrashHandler;
import com.huotu.partnermall.utils.KJConfig;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.commons.codec.binary.StringUtils;

import java.util.List;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * 系统级别的变量、方法
 * Application
 */
public class BaseApplication extends Application {

    public DrawerLayout layDrag;
    //定位句柄
    public Intent locationI = new Intent ( );
    //定位类型
    public int    localType;
    //地址
    public String address;
    //纬度
    public double latitude;
    //经度
    public double Longitude;
    //是否有网络连接
    public boolean isConn = false;
    //城市
    public String             city;
    public LocationClient     mLocationClient;
    public GeofenceClient     mGeofenceClient;
    public MyLocationListener mMyLocationListener;
    //底部菜单是否隐藏 true显示， false隐藏
    public boolean isMenuHide = false;
    //维护页面标题信息栈
    public
    Stack< PageInfoModel > titleStack;
    /**
     * 是否是左划或者返回
     * true 左划
     * false 返回
     */

    public boolean isLeftImg = true;

    public IWXAPI wApi;

    @Override
    public
    void onConfigurationChanged ( Configuration newConfig ) {
        super.onConfigurationChanged ( newConfig );
    }

    @Override
    public
    void onCreate ( ) {
        super.onCreate ( );
        mLocationClient = new LocationClient ( this.getApplicationContext ( ) );
        mMyLocationListener = new MyLocationListener ( );
        mLocationClient.registerLocationListener ( mMyLocationListener );
        mGeofenceClient = new GeofenceClient ( getApplicationContext ( ) );

        // 初始化Volley实例
        VolleyUtil.init ( this );
        // 极光初始化
        // JPushInterface.setDebugMode(true);// 日志，生产环境关闭
        JPushInterface.init ( this );
        //初始化shareSDK参数
        ShareSDK.initSDK ( getApplicationContext ( ) );
        solveAsyncTaskOnPostExecuteBug ( );

        titleStack = new Stack< PageInfoModel > ( );
        //加载异常处理模块
        CrashHandler crashHandler = CrashHandler.getInstance ( );
        crashHandler.init ( getApplicationContext ( ), titleStack );

    }

    @Override
    public
    void onLowMemory ( ) {
        super.onLowMemory ( );
    }

    @Override
    public
    void onTerminate ( ) {
        super.onTerminate ( );
    }

    /**
     * 解决有些android版本 AsyncTask 无法执行  onPostExecute方法的问题
     *@创建人：jinxiangdong
     *@修改时间：2015年7月7日 上午11:23:32
     *@方法描述：
     *@方法名：solveAsyncTaskOnPostExecuteBug
     *@参数：
     *@返回：void
     *@exception
     *@since
     */
    protected
    void solveAsyncTaskOnPostExecuteBug ( ) {
        try {
            Class.forName ( "android.os.AsyncTask" );
        }
        catch ( ClassNotFoundException e ) {
            e.printStackTrace ( );
        }
    }

    /**
     * 获取手机IMEI码
     */
    public static
    String getPhoneIMEI ( Context cxt ) {
        TelephonyManager tm = ( TelephonyManager ) cxt
                .getSystemService ( Context.TELEPHONY_SERVICE );
        return tm.getDeviceId ( );
    }

    public String readCurrentUrl()
    {
        return PreferenceHelper.readString ( getApplicationContext (), Constants.BASE_INFO, Constants.CURRENT_URL );
    }

    /**
     * 判断网络是否连接
     */
    public static boolean checkNet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo ( );
        return info != null;// 网络是否连接
    }

    /**
     * 仅wifi联网功能是否开启
     */
    public static boolean checkOnlyWifi(Context context)
    {
        if ( PreferenceHelper.readBoolean (
                context, KJConfig.SETTING_FILE,
                KJConfig.ONLY_WIFI
                                          ))
        {
            return isWiFi(context);
        } else
        {
            return true;
        }
    }

    /**
     * 判断是否为wifi联网
     */
    public static boolean isWiFi(Context cxt)
    {
        ConnectivityManager cm = (ConnectivityManager) cxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // wifi的状态：ConnectivityManager.TYPE_WIFI
        // 3G的状态：ConnectivityManager.TYPE_MOBILE
        NetworkInfo.State state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                                    .getState ( );
        return NetworkInfo.State.CONNECTED == state;
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static String getAppVersion(Context context)
    {
        String version = "0";
        try
        {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            KJLoger.e ( e.getMessage ( ) );
        }
        return version;
    }


    /**
     * 检测APP端是否已经设置商户信息
     * @return
     */
    public boolean checkMerchantInfo()
    {
        //商户ID
        String merchantId = PreferenceHelper.readString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_ID );
        //商户支付宝key信息
        String merchantAlipayKey = PreferenceHelper.readString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_ALIPAY_KEY );
        //商户微信支付KEY信息
        String merchantWeixinKey = PreferenceHelper.readString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_WEIXIN_KEY );
        //商户菜单
        String merchantMenus = PreferenceHelper.readString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_MENUS );
        //商户类别菜单
        String merChantCatagory = PreferenceHelper.readString ( getApplicationContext ( ),
                                                                Constants.MERCHANT_INFO,
                                                                Constants.MERCHANT_INFO_CATAGORY );

        if((null == merchantId) && (null == merchantAlipayKey) && (null == merchantWeixinKey) && (null == merchantMenus) && (null == merChantCatagory))
        {
            //app端未设置商户信息
            return false;
        }
        else
        {
            return true;
        }
    }

    public String createUrl(String suffix)
    {
        return obtainMerchantUrl() + suffix;
    }

    /**
     * 判断是否配置菜单信息
     * @return
     */
    public boolean checkMenuInfo()
    {
        //菜单信息
        String menuStr = PreferenceHelper.readString ( getApplicationContext ( ), Constants
                                                               .MERCHANT_INFO, Constants
                                                               .MERCHANT_INFO_MENUS );
        if(null != menuStr && !"".equals ( menuStr.trim () ))
        {
            return  true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 写入商户信息到文件
     * @param merchant
     */
    public void writeMerchantInfo(MerchantBean merchant)
    {
        //商户ID
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_ID, merchant.getMerchantId ( ) );
        //版本号
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.APP_VERSION, merchant.getAppVersion ( ) );
        //APP名称
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.APP_NAME, merchant.getAppName ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.WEIXIN_MERCHANT_ID,  merchant.getWeixinMerchantId ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_WEIXIN_ID, merchant.getMerchantWeixinId ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.WEIXIN_KEY, merchant.getWeixinKey ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.ALIPAY_MERCHANT_ID, merchant.getAlipayMerchantId ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_ALIPAY_ID, merchant.getMerchantAlipayId ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.LOCATION_KEY, merchant.getLocationKey ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.ALIPAY_KEY, merchant.getAlipayKey ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.U_MENG_KEY, merchant.getUmengAppkey ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.U_MENG_CHANNEL, merchant.getUmengChannel ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.U_MENG_SECRET, merchant.getUmengMessageSecret ( ) );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.PREFIX, merchant.getHttpPrefix ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.SHARE_KEY, merchant.getShareSDKKey ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.TENCENT_KEY, merchant.getTencentKey ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.TENCENT_SECRET, merchant.getTencentSecret ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.SINA_KEY, merchant.getSinaKey ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.SINA_SECRET, merchant.getSinaSecret ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.SINA_REDIRECT_URI, merchant.getSinaRedirectUri ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.WEIXIN_SHARE_key, merchant.getWeixinShareKey ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.WEIXIN_SHARE_SECRET, merchant.getWeixinShareSecret ( ));
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.PUSH_KEY, merchant.getPushKey ( ));

    }

    public void writeMenus(List<MenuBean> menus)
    {
        Gson gson = new Gson ();
        String menuStr = gson.toJson ( menus );

        PreferenceHelper.writeString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_MENUS,  menuStr);
    }

    public void writeMemberInfo(String userName, String userId, String userIcon, String userToken)
    {
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MEMBER_INFO, Constants.MEMBER_ID, userId );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MEMBER_INFO, Constants.MEMBER_NAME, userName );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MEMBER_INFO, Constants.MEMBER_ICON, userIcon );
        PreferenceHelper.writeString ( getApplicationContext (), Constants.MEMBER_INFO, Constants.MEMBER_TOKEN, userToken );
    }

    /**
     * 获取商户的底部菜单信息
     * @return
     */
    public String readMenus()
    {
        return PreferenceHelper.readString ( getApplicationContext (), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_MENUS );
    }

    /**
     * 实现定位回调监听
     */
    public class MyLocationListener implements BDLocationListener
    {

        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (null == location)
            {
                return;
            } else
            {
                localType = location.getLocType();

                if (BDLocation.TypeGpsLocation == localType)
                {
                    latitude = location.getLatitude();
                    Longitude = location.getLongitude ( );
                    city = location.getCity ( );
                    address = location.getAddrStr();
                } else if (BDLocation.TypeNetWorkLocation == localType)
                {
                    latitude = location.getLatitude();
                    Longitude = location.getLongitude ( );
                    city = location.getCity ( );
                    address = location.getAddrStr();
                }
            }

            // 将定位信息写入配置文件
            if (null != city)
            {
                PreferenceHelper.writeString(getApplicationContext(),
                                             Constants.LOCATION_INFO, Constants.CITY, city);
            }
            if (null != address)
            {
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
    public boolean isLogin()
    {
        String token = PreferenceHelper.readString ( getApplicationContext (), Constants.MEMBER_INFO, Constants.MEMBER_TOKEN );
        if(null != token && !"".equals ( token ))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //登出
    public void logout()
    {
        PreferenceHelper.clean ( getApplicationContext (), Constants.MEMBER_INFO );
    }

    //获取用户图片
    public String getUserLogo()
    {
        return PreferenceHelper.readString ( getApplicationContext (), Constants.MEMBER_INFO, Constants.MEMBER_ICON );
    }
    //获取用户名称
    public String getUserName()
    {
        return PreferenceHelper.readString ( getApplicationContext (), Constants.MEMBER_INFO, Constants.MEMBER_NAME );
    }

    //判断是否为4.4版本。可设置沉浸模式
    public boolean isKITKAT()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    //获取商家的访问渠道
    public String obtainMerchantUrl()
    {
        return PreferenceHelper.readString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.PREFIX );
    }

    /**
     * 检测是否已经配置了颜色信息
     * @return
     */
    public boolean checkColorInfo()
    {
        String mainColor = PreferenceHelper.readString ( getApplicationContext (), Constants.COLOR_INFO, Constants.COLOR_MAIN );
        if( TextUtils.isEmpty ( mainColor ))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean checkSysInfo()
    {
        String packageStr = PreferenceHelper.readString ( getApplicationContext (), Constants.SYS_INFO, Constants.SYS_PACKAGE );
        if(TextUtils.isEmpty ( packageStr ))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void writeInitInfo(String initStr)
    {
        PreferenceHelper.writeString ( getApplicationContext (), Constants.SYS_INFO, Constants.FIRST_OPEN, initStr );
    }

    public boolean isFirst()
    {
        String initInfo = PreferenceHelper.readString ( getApplicationContext (), Constants.SYS_INFO, Constants.FIRST_OPEN );
        if(TextUtils.isEmpty ( initInfo ))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 写入颜色信息
     * @param colorBean
     */
    public void writeColorInfo(ColorBean colorBean)
    {
        if(null != colorBean)
        {
            String mainColor = colorBean.getColorMap ().get ( "MAIN_COLOR_1" );
            String secondColor = colorBean.getColorMap ().get ( "SECONDART_COLOR_1" );
            PreferenceHelper.writeString ( getApplicationContext (), Constants.COLOR_INFO, Constants.COLOR_MAIN, mainColor );
            PreferenceHelper.writeString ( getApplicationContext (), Constants.COLOR_INFO, Constants.COLOR_SECOND, secondColor );
        }
    }

    public String obtainMainColor()
    {
        return PreferenceHelper.readString ( getApplicationContext (), Constants.COLOR_INFO, Constants.COLOR_MAIN );
    }

    public String obtainSecondColor()
    {
        return PreferenceHelper.readString ( getApplicationContext (), Constants.COLOR_INFO, Constants.COLOR_SECOND );
    }

    public void writeSysInfo(SysModel sysModel)
    {
        if(null != sysModel)
        {
            PreferenceHelper.writeString ( getApplicationContext (), Constants.SYS_INFO, Constants.SYS_PACKAGE, sysModel.getPackageStr () );
            PreferenceHelper.writeString ( getApplicationContext (), Constants.SYS_INFO, Constants.SYS_MENU, sysModel.getSysMenu () );
        }
    }

    public String readSysInfo()
    {
        return PreferenceHelper.readString (  getApplicationContext (), Constants.SYS_INFO, Constants.SYS_PACKAGE );
    }

    public String readSysMenu()
    {
        return PreferenceHelper.readString (  getApplicationContext (), Constants.SYS_INFO, Constants.SYS_MENU );
    }

    //获取微信key
    public String readWeixinKey()
    {
        return PreferenceHelper.readString ( getApplicationContext (), Constants.MERCHANT_INFO,  Constants.WEIXIN_SHARE_key );
    }

    //获取微信安全码
    public String readWeixinSecret()
    {
        return PreferenceHelper.readString ( getApplicationContext ( ), Constants.MERCHANT_INFO,
                                             Constants.WEIXIN_SHARE_SECRET );
    }



}
