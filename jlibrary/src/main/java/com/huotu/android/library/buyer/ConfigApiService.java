package com.huotu.android.library.buyer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/3/8.
 */
public interface ConfigApiService {

    /**
     *
     * @param userkey
     * @param userRandom
     * @param userSecurity
     * @param merchantId
     * @return
     */
    @GET("smartPages/search/findIndex")
    Call<Object> findIndex(@Header("_user_key") String userkey ,
                     @Header("_user_random") String userRandom ,
                     @Header("_user_secure") String userSecurity ,
                     @Query("merchantId") int merchantId);


    /**
     *
     * @param userkey
     * @param userRandom
     * @param userSecurity
     * @param platform
     * @param version
     * @param osVersion
     * @return
     */
    @GET("nativeCode")
    Call<Object> nativeCode(
                            @Header("_user_key") String userkey ,
                            @Header("_user_random") String userRandom ,
                            @Header("_user_secure") String userSecurity ,
                            @Query("platform") String platform,
                            @Query("version") String version,
                            @Query("osVersion") String osVersion );

    /**
     *
     * @param userkey
     * @param userRandom
     * @param userSecurity
     * @param merchantId
     * @return
     */
    @GET("merchantWidgetSettings/search/findByMerchantId")
    Call<Object> findByMerchantId(
            @Header("_user_key") String userkey ,
            @Header("_user_random") String userRandom ,
            @Header("_user_secure") String userSecurity ,
            @Query("merchantId") int merchantId
    );

}
