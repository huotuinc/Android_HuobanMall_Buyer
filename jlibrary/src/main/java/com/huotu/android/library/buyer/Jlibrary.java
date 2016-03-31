package com.huotu.android.library.buyer;

import android.content.Context;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.huotu.android.library.buyer.bean.Variable;

/**
 * Created by jinxiangdong on 2016/1/26.
 */
public class Jlibrary {

    public  static void initFresco(Context context){
        Fresco.initialize(context);
    }

    public static void initCustomerId(int customerId){
        Variable.CustomerId = customerId;
    }

    public static void initUserLevelId( int levelId ){
        Variable.userLevelId = levelId;
    }

    public static void initResourceUrl(String url){
        Variable.resourceUrl = url;
    }

    public static void initSiteUrl(String url){
        Variable.siteUrl = url;
    }

    public static void initMainUIConfigUrl(String url){
        Variable.mainUiConfigUrl =url;
    }

    public static void initSmartKey(String key){
        Variable.BizKey = key;
    }
    public static void initSmartSecurity(String security){
        Variable.BizAppSecure = security;
    }

    public static void initSmartUrl(String smartUrl){
        Variable.BizRootUrl = smartUrl;
        Variable.configRootUrl = smartUrl;
    }

}
