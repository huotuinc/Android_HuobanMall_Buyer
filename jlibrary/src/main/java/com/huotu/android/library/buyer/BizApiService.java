package com.huotu.android.library.buyer;

import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.BrandBean;
import com.huotu.android.library.buyer.bean.BizBean.ClassBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.BizBean.MallInfoBean;
import com.huotu.android.library.buyer.bean.BizBean.TagBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.DataItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/1/31.
 */
public interface BizApiService {

    /**
     * 获得商品信息api
     * @param customerid 商户id
     * @param goodIds 商品id，多个用逗号分割
     * @return
     */
    @GET("goods/getgoodslist")
    Call<List<GoodsBean>> getGoodsList(
            @Header(Constant.HEADER_USER_KEY) String userkey,
            @Header(Constant.HEADER_USER_RANDOM) String userrandom,
            @Header(Constant.HEADER_USER_SECURE) String usersecure,
            @Query("customerId") String customerid ,
            @Query("goodIds") String goodIds );

    /**
     * 获得商城基本信息api
     * @param customerId 商户id
     * @return 商城基本信息
     */
    @GET("buyerSeller/api/goods/getMallBaseInfo")
    Call<BizBaseBean<MallInfoBean>> getMallInfo(
            @Header(Constant.HEADER_USER_KEY) String userkey,
            @Header(Constant.HEADER_USER_RANDOM) String userrandom,
            @Header(Constant.HEADER_USER_SECURE) String usersecure,
            @Query("customerId" ) int customerId
            );

    /**
     * 获得全部品牌api
     * @param customerId 商户id
     * @return 品牌信息
     */
    @GET("buyerSeller/api/goods/getAllBrand")
    Call<BizBaseBean<List<BrandBean>>> getAllBrand(
            @Header(Constant.HEADER_USER_KEY) String userkey,
            @Header(Constant.HEADER_USER_RANDOM) String userrandom,
            @Header(Constant.HEADER_USER_SECURE) String usersecure,
            @Query("customerId") int customerId);

    /**
     * 获得全部分类api
     * @param customerId 商户id
     * @return 分类信息
     */
    @GET("buyerSeller/api/goods/getAllCategory")
    Call<BizBaseBean<List<ClassBean>>> getAllCategory(
            @Header(Constant.HEADER_USER_KEY) String userkey,
            @Header(Constant.HEADER_USER_RANDOM) String userrandom,
            @Header(Constant.HEADER_USER_SECURE) String usersecure,
            @Query("customerId") int customerId
           );

    /**
     * 根据商品分类获得商品列表信息api
     * @param customerId 商户id
     * @param catId 分类id，默认0
     * @param levelId 当前登录用户的等级id
     * @param sortRule 排序字段，见说明，默认按照新品  0(新品): desc,1(销量): desc, 2(价格): desc
     * @param searchKey 搜索关键字，有可能是名称，标签等
     * @param filter 筛选条件，品牌，分类，标签的组合 品牌id,品牌id:分类id,分类id:标签id,标签id  1,2:1,2:1,2
     * @param pageIndex 页索引，从1开始，默认1
     * @param pageSize 页大小，默认10
     * @return
     */
    @GET("buyerSeller/api/goods/getGoodsList")
    Call<BizBaseBean<GoodsListBean>> getGoodsList(
            @Header(Constant.HEADER_USER_KEY) String userkey,
            @Header(Constant.HEADER_USER_RANDOM) String userrandom,
            @Header(Constant.HEADER_USER_SECURE) String usersecure,
            @Query("customerId") int customerId,
                                                  @Query("catId") int catId,
                                                  @Query("levelId") int levelId,
                                                  @Query("sortRule") String sortRule,
                                                  @Query("searchKey") String searchKey,
                                                  @Query("filter") String filter,
                                                  @Query("pageIndex") int pageIndex,
                                                  @Query("pageSize") int pageSize);


    /**
     *根据商户id和商品分类id获得商品分类列表信息api
     * @param customerId 商户id
     * @param catId 商品分类id
     * @return
     */
    @GET("buyerSeller/api/goods/getGoodsCatList")
    Call<BizBaseBean<List<ClassBean>>> getGoodsCatList(
            @Header(Constant.HEADER_USER_KEY) String userkey,
            @Header(Constant.HEADER_USER_RANDOM) String userrandom,
            @Header(Constant.HEADER_USER_SECURE) String usersecure,
            @Query("customerId") int customerId ,
            @Query("catId") int catId,
            @Query("sign") String sign);

    /**
     * 获得全部标签api
     * @param customerId 商户id
     * @return
     */
    @GET("buyerSeller/api/goods/getAllTag")
    Call<BizBaseBean<List<TagBean>>> getAllTag(
            @Header(Constant.HEADER_USER_KEY) String userkey,
            @Header(Constant.HEADER_USER_RANDOM) String userrandom,
            @Header(Constant.HEADER_USER_SECURE) String usersecure,
            @Query("customerId") int customerId);

    /**
     * 根据具体的商品ID来获得商品信息api
     * @param userkey
     * @param userrandom
     * @param usersecure
     * @param customerId 商户id
     * @param goodIds 商品id，多个用逗号分割
     * @param levelId 当前登录用户的等级id
     * @return
     */
    @GET("buyerSeller/api/goods/getGoodsDetail")
    Call<BizBaseBean<List<GoodsBean>>> getGoodsDetail(
            @Header(Constant.HEADER_USER_KEY) String userkey,
            @Header(Constant.HEADER_USER_RANDOM) String userrandom,
            @Header(Constant.HEADER_USER_SECURE) String usersecure,
        @Query("customerId") int customerId,
        @Query("goodIds") String goodIds,
        @Query("levelId") int levelId
    );

}
