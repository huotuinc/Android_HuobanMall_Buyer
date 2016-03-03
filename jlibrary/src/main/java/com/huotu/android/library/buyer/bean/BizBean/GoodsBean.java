package com.huotu.android.library.buyer.bean.BizBean;

import java.util.Date;
import java.util.List;

/**
 * 商品详情
 * Created by Administrator on 2016/2/24.
 */
public class GoodsBean {
    /**
     *商品名称
     */
    private String goodName;
    /**
     *分类id
     */
    private int catId;
    /**
     *类型id
     */
    private int typeId;
    /**
     *商品类型
     */
    private String goodType;
    /**
     *品牌id
     */
    private int brandId;
    /**
     *商品品牌
     */
    private String brand;
    /**
     *市场价
     */
    private double marketPrice;
    /**
     *销售价
     */
    private double price;
    /**
     *商品编码
     */
    private String bn;
    /**
     *自己购买获得的积分，[min,max]
     */
    private String score;
    /**
     *下线购买得到的返利，[min,max]
     */
    private List<String> rebate;
    /**
     *缩略图
     */
    private String thumbnailPic;
    /**
     *商品简介
     */
    private String brief;
    /**
     *详细介绍
     */
    private String intro;
    /**
     *库存
     */
    private int store;
    /**
     *规格名序列化，{1:”颜色”,2:”尺码”}
     */
    private String spec;
    /**
     *货品集合序列化，[{"ProductId":14,"Desc":"红色,S"},{"ProductId":15,"Desc":"红色,M"},{"ProductId":16,"Desc":"红色,L"},{"ProductId":17,"Desc":"红色,XL"}]
     */
    private String pdtSpec;
    /**
     *规格内容序列化，[{“SpecId”:1,"ShowType":"text","SpecValue":"红色","SpecValueId":1,"SpecImage":null,"GoodsImageIds":[]}]
     */
    private String specDesc;
    /**
     *创建时间
     */
    private Date createTime;

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getRebate() {
        return rebate;
    }

    public void setRebate(List<String> rebate) {
        this.rebate = rebate;
    }

    public String getThumbnailPic() {
        return thumbnailPic;
    }

    public void setThumbnailPic(String thumbnailPic) {
        this.thumbnailPic = thumbnailPic;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPdtSpec() {
        return pdtSpec;
    }

    public void setPdtSpec(String pdtSpec) {
        this.pdtSpec = pdtSpec;
    }

    public String getSpecDesc() {
        return specDesc;
    }

    public void setSpecDesc(String specDesc) {
        this.specDesc = specDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
