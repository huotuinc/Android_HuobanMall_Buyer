package com.huotu.android.library.buyer;

import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.ClassificationConfig;
import com.huotu.android.library.buyer.bean.Data.FindIndexConfig;
import com.huotu.android.library.buyer.bean.PageConfig;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by jinxiangdong on 2016/3/8.
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
    Call<FindIndexConfig> findIndex(
            @Header(Constant.HEADER_USER_KEY) String userkey ,
            @Header(Constant.HEADER_USER_RANDOM) String userRandom ,
            @Header(Constant.HEADER_USER_SECURE) String userSecurity ,
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
    Call<PageConfig> nativeCode(
                            @Header(Constant.HEADER_USER_KEY) String userkey ,
                            @Header(Constant.HEADER_USER_RANDOM) String userRandom ,
                            @Header(Constant.HEADER_USER_SECURE) String userSecurity ,
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
            @Header(Constant.HEADER_USER_KEY) String userkey ,
            @Header(Constant.HEADER_USER_RANDOM) String userRandom ,
            @Header(Constant.HEADER_USER_SECURE) String userSecurity ,
            @Query("merchantId") int merchantId
    );

    /**
     *
     * @param userkey
     * @param userRandom
     * @param userSecurity
     * @param merchantId
     * @param productClassification
     * @param type
     * @return
     */
    @GET("namedPages/search/findByClassification")
    Call<ClassificationConfig> findByClassification(
                                    @Header(Constant.HEADER_USER_KEY) String userkey ,
                                       @Header(Constant.HEADER_USER_RANDOM) String userRandom ,
                                       @Header(Constant.HEADER_USER_SECURE) String userSecurity ,
                                       @Query("merchantId") int merchantId,
                                       @Query("productClassification") String productClassification,
                                       @Query("type") String type );

}
