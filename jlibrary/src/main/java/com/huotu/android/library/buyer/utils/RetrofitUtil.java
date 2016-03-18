package com.huotu.android.library.buyer.utils;

import com.huotu.android.library.buyer.BuildConfig;
import com.huotu.android.library.buyer.bean.Variable;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 *
 * Created by jinxiangdong on 2016/1/31.
 */
public class RetrofitUtil {
    //private static final String BASEURL ="";

//    public static final String getConfigBaseUrl(){
//        if(BuildConfig.DEBUG){//开发机地址
//            return "http://api.open.fancat.cn:8081/";
//        }else{
//            return "";
//        }
//    }

    private static Retrofit configRetrofitClient;
    private static Retrofit bizRetrofitClient;

    /**
     * 获得UI配置接口
     * @return
     */
    public static Retrofit getConfigInstance(){
        if ( configRetrofitClient==null){
            configRetrofitClient = new Retrofit.Builder()
                    .baseUrl(Variable.configRootUrl )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return configRetrofitClient;
    }


    public static Retrofit getBizRetroftInstance( String bizBaseUrl ) {
        if ( bizRetrofitClient==null){
            bizRetrofitClient = new Retrofit.Builder()
                    .baseUrl( bizBaseUrl )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return bizRetrofitClient;
    }
}
