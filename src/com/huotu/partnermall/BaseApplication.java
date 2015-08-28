package com.huotu.partnermall;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;

import com.huotu.partnermall.utils.KJConfig;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.PreferenceHelper;

/**
 * 系统级别的变量、方法
 * Application
 */
public class BaseApplication extends Application {

    public DrawerLayout layDrag;

    @Override
    public
    void onConfigurationChanged ( Configuration newConfig ) {
        super.onConfigurationChanged ( newConfig );
    }

    @Override
    public
    void onCreate ( ) {
        super.onCreate ( );
    }

    @Override
    public
    void onLowMemory ( ) {
        super.onLowMemory ( );
    }

    @Override
    public void onTerminate() {
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
    protected void solveAsyncTaskOnPostExecuteBug(){
        try
        {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Context cxt)
    {
        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 判断网络是否连接
     */
    public static boolean checkNet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
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
                                    .getState();
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



}
