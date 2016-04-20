package com.huotu.partnermall.config;

import com.huotu.partnermall.inner.BuildConfig;

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
    public static String get_Config_Root_Url(){
        return BuildConfig.SMART_URL;
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
    /**
     *
     */
    public final static String KEY_SMARTUICONFIGURL="key_smartuiconfigurl";

    public final static String KEY_ISMAINUI="key_ismainui";

    public final static String KEY_CLASSID="key_classid";

    public final static String KEY_SEARCH="key_search";

    public final static String KEY_NEEDREFRESHUI = "key_needrefreshui";

    public final static String NATIVIE_KEY(){
        return BuildConfig.SMART_KEY;
    }

    public final static String Native_security(){
        return BuildConfig.SMART_SECURITY;
    }

    public final static int CUSTOMERID(){
        return BuildConfig.CUSTOMERID;
    }

    public final static int TYPE_SMARTUI=1;
    public final static int TYPE_WEB=2;
    public final static int TYPE_INDEX=0;
    public final static String KEY_TYPE="key_type";
}
