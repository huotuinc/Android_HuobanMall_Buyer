package com.huotu.partnermall.widgets.custom;

/**
 * 商城基本信息
 * Created by Administrator on 2016/2/24.
 */
public class MallInfoBean {
    /**
     * 商城名称
     */
    private String mallName;
    /**
     * 商城logo
     */
    private String logo;
    /**
     * 分享描述信息
     */
    private String shareDesc;
    /**
     * 首页地址url
     */
    private String indexUrl;
    /**
     * 全部商品数量
     */
    private int goodNum;
    /**
     * 上新商品数量
     */
    private int newGoodNum;
    /**
     * 全部订单数量
     */
    private int orderNum;
    /**
     * 商品链接
     */
    private String goodsListUrl;
    /**
     * 订单链接
     */
    private String orderListUrl;
    /**
     * 客服QQ
     */
    private String clientQQ;

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getNewGoodNum() {
        return newGoodNum;
    }

    public void setNewGoodNum(int newGoodNum) {
        this.newGoodNum = newGoodNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getGoodsListUrl() {
        return goodsListUrl;
    }

    public void setGoodsListUrl(String goodsListUrl) {
        this.goodsListUrl = goodsListUrl;
    }

    public String getOrderListUrl() {
        return orderListUrl;
    }

    public void setOrderListUrl(String orderListUrl) {
        this.orderListUrl = orderListUrl;
    }

    public String getClientQQ() {
        return clientQQ;
    }

    public void setClientQQ(String clientQQ) {
        this.clientQQ = clientQQ;
    }
}
