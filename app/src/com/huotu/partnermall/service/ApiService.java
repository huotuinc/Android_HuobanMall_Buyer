package com.huotu.partnermall.service;

import com.huotu.partnermall.model.MSiteModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/3/15.
 */
public interface ApiService {
    /**
     *
     * @param customerId
     * @return
     */
    @GET("/mall/getmsiteurl")
    Call<MSiteModel> getSiteUrl(@Query("customerId") String customerId);
}
