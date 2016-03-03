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

    public static String UI_CONFIG_FILE="ui_config_file";
    public static String UI_CONFIG_KEY ="ui_config_key";
    public final static int RESULT_CODE_CLIENT_DOWNLOAD_FAILED = -8000;
    public final static int REQUEST_CODE_CLIENT_DOWNLOAD = 8000;
}
