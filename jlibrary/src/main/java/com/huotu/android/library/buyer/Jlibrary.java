package com.huotu.android.library.buyer;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.huotu.android.library.buyer.bean.Variable;

/**
 * Created by jinxiangdong on 2016/1/26.
 */
public class Jlibrary {
    public  static void init(Context context){
        Fresco.initialize(context);
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
    /**
     * 业务数据接口 调用 AppSecurity
     */
    //public final static String BizApiSecurity="1165a8d240b29af3f418b8d10599d0dc";



}
