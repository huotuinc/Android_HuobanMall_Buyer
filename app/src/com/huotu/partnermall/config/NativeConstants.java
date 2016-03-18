package com.huotu.partnermall.config;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.BuildConfig;
import com.huotu.partnermall.utils.PreferenceHelper;

/**
 * Created by Administrator on 2016/2/24.
 */
public class NativeConstants {
    /**
     * 获得当前App支持的UI配置版本号
     * @return
     */
    public static int Version(){
        return BuildConfig.NATIVE_UI_CONFIG_VERSION;
    }

    public final static String UI_CONFIG_FILE="ui_config_file";
    public final static String UI_CONFIG_SELF_HREF="ui_config_self_href";
    public final static String UI_CONFIG_SELF_KEY ="ui_config_self_key";
    public final static int RESULT_CODE_CLIENT_DOWNLOAD_FAILED = -8000;
    public final static int REQUEST_CODE_CLIENT_DOWNLOAD = 8000;

    /**
     * 获得配置接口根地址
     * @return
     */
    private static String get_Config_Root_Url(){
        if( BuildConfig.DEBUG){
            return  "http://api.open.fancat.cn:8081/";
        }else {
            return "";
        }
    }

    /**
     * 获得商场首页的配置URL根地址
     * @return
     */
//    private static String get_Config_Index_Root_Url(){
//        String rootUrl = PreferenceHelper.readString(BaseApplication.single , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_HREF);
//        if( !rootUrl.endsWith("/") ) rootUrl+="/";
//        return rootUrl;
//    }

    /**
     * 获得商城的所有页面的配置信息接口的url
     */
    public final static String FINDINDEX_URL = get_Config_Root_Url()+ "smartPages/search/findIndex";
    /**
     * 获得首页配置信息接口的url
     */
    public final static String NATIVECODE_URL = "nativeCode";

    public final static String HEADER_USER_KEY="_user_key";
    public final static String HEADER_USER_RANDOM="_user_random";
    public final static String HEADER_USER_SECURE="_user_secure";


    public final static String NATIVIE_KEY(){
        if(BuildConfig.DEBUG){
            return "_demo";
        }else{
            return "";
        }
    }

    public final static String Native_security(){
        if(BuildConfig.DEBUG){
            return "1f2f3f4f5f6f7f8f";
        }else{
            return "";
        }
    }

    public final static int CUSTOMERID(){
        return BuildConfig.CUSTOMERID;
    }
}
