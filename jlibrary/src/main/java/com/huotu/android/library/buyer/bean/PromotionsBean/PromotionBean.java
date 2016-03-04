package com.huotu.android.library.buyer.bean.PromotionsBean;

/**
 * Created by jinxiangdong on 2016/1/29.
 */
public class PromotionBean {
    /**
     * 优惠卷名称
     */
    private String name;
    /**
     * 优惠卷ID
     */
    private String id;
    /**
     * 优惠券价格，或者是折扣百分比
     */
    private String prices;
    /**
     * 优惠卷链接地址
     */
    private String pageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
