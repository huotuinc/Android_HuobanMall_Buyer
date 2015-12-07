package com.huotu.partnermall.ui.sis;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class SisGoodsModel implements Serializable{

    private java.lang.Long goodsId;//商品ID
    private java.lang.String imgUrl;//商品图片
    private java.lang.String goodsName;//商品名称
    private double price;//销售价
    private java.lang.Integer stock;//库存量
    private double profit;//佣金
    private boolean goodSelected;//是否上架
    private boolean validate=true;//是否有效
    private double rebate ;//返利
    //private boolean isOnSale=false;//
    private boolean isProcessing=false;//是否正在处理....
    private String detailsUrl;//APP里面的商品页面
    private String shareUrl;//商品详情分享出去的URL

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public double getRebate() {
        return rebate;
    }

    public void setRebate(double rebate) {
        this.rebate = rebate;
    }

    public boolean isGoodSelected() {
        return goodSelected;
    }

    public void setGoodSelected(boolean goodSelected) {
        this.goodSelected = goodSelected;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(boolean isProcessing) {
        this.isProcessing = isProcessing;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

//    public boolean isOnSale() {
//        return isOnSale;
//    }

//    public void setIsOnSale(boolean isOnSale) {
//        this.isOnSale = isOnSale;
//    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
