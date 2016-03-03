package com.huotu.android.library.buyer.bean.Data;

/**
 * Created by Administrator on 2016/2/19.
 */
public class GroupGoodsBean {
    private int groupid;
    private int goodid;
    private String goodname;
    private String price;
    private String zprice;
    private String jifen;
    private String imageUrl;

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public int getGoodid() {
        return goodid;
    }

    public void setGoodid(int goodid) {
        this.goodid = goodid;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getZprice() {
        return zprice;
    }

    public void setZprice(String zprice) {
        this.zprice = zprice;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
