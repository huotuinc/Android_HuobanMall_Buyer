package com.huotu.android.library.buyer.bean.PromotionsBean;

/**
 * Created by jinxiangdong on 2016/1/29.
 */
public class PromotionBean {
    /**
     * 优惠卷名称
     */
    private String coupnName;
    /**
     * 优惠卷ID
     */
    private String coupnId;
    /**
     * 优惠券价格，或者是折扣百分比
     */
    private String coupnPrices;
    /**
     * 优惠卷链接地址
     */
    private String coupnPageUrl;

    public String getCoupnName() {
        return coupnName;
    }

    public void setCoupnName(String coupnName) {
        this.coupnName = coupnName;
    }

    public String getCoupnId() {
        return coupnId;
    }

    public void setCoupnId(String coupnId) {
        this.coupnId = coupnId;
    }

    public String getCoupnPrices() {
        return coupnPrices;
    }

    public void setCoupnPrices(String coupnPrices) {
        this.coupnPrices = coupnPrices;
    }

    public String getCoupnPageUrl() {
        return coupnPageUrl;
    }

    public void setCoupnPageUrl(String coupnPageUrl) {
        this.coupnPageUrl = coupnPageUrl;
    }
}
