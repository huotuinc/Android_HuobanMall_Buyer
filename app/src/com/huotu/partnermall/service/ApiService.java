package com.huotu.partnermall.service;

import com.huotu.android.library.buyer.bean.BizBean.MallInfoBean;
import com.huotu.partnermall.model.MSiteModel;
import com.huotu.partnermall.model.MerchantPayInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/3/15.
 */
public interface ApiService {
    /**
     * 获得 商户 站点域名
     * @param customerId
     * @return
     */
    @GET("/mall/getmsiteurl")
    Call<MSiteModel> getSiteUrl(@Query("customerId") String customerId);

    /**
     * 获得 支付信息
     * @param customerid
     * @return
     */
    @GET("/PayConfig")
    Call<MerchantPayInfo> payConfig(
            @Query("version" ) String version,
            @Query("operation") String operation,
            @Query("timestamp") String timestamp,
            @Query("appid") String appid,
            @Query("sign") String sign,
            @Query("customerid") String customerid);
}
