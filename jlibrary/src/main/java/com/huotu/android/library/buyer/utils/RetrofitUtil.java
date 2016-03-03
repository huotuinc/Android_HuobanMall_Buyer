package com.huotu.android.library.buyer.utils;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by jinxiangdong on 2016/1/31.
 */
public class RetrofitUtil {
    private static final String BASEURL ="http://api.java.com";
    private static Retrofit retrofitClient;

    public static Retrofit getInstance(){
        if ( retrofitClient==null){
            retrofitClient = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitClient;
    }


}
