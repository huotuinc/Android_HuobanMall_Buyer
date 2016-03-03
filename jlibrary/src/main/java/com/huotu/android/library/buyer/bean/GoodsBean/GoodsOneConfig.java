package com.huotu.android.library.buyer.bean.GoodsBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * 单方格(默认样式)
 * Created by jinxiangdong on 2016/1/25.
 */
public class GoodsOneConfig extends BaseConfig {
    /**
     * 列表样式:
     * card   -卡片样式
     * normal –极简样式
     */
    private String gridStyle;
    /**
     * 是否显示商品名
     * show  -显示
     * hide–不显示
     */
    private String isShowName;
    /**
     * 是否显商品简介
     * show  -显示
     * hide –不显示
     */
    private String isShowSyno;
    /**
     * 是否显示价格
     * show  -显示
     * hide–不显示
     */
    private String isShowPrices;
    /**
     * 是否显示返利积分
     * show  -显示
     * hide–不显示
     */
    private String isShowUserInteger;
    /**
     * 多个商品，ID用逗号隔开,如:1,2,3,4
     */
    private String bindDataID;
    /**
     * 返利图标(该图标资源存储的是相对地址，需要app配置一个资源地址拼接出资源绝对地址)。
     */
    private String rebateIcon;

//    private String imageUrl;
//    private String name;
//    private String desc;
//    private String price;
//    private String zPrice;
//    private String jifen;
//    private String jifenUrl;
    private int iconWidth;
    private int iconHeight;

    private int leftMargion;
    private int rightMargion;
    private int topMargion;
    private int bottomMargion;

    public String getGridStyle() {
        return gridStyle;
    }

    public void setGridStyle(String gridStyle) {
        this.gridStyle = gridStyle;
    }

    public String getIsShowName() {
        return isShowName;
    }

    public void setIsShowName(String isShowName) {
        this.isShowName = isShowName;
    }

    public String getIsShowSyno() {
        return isShowSyno;
    }

    public void setIsShowSyno(String isShowSyno) {
        this.isShowSyno = isShowSyno;
    }

    public String getIsShowPrices() {
        return isShowPrices;
    }

    public void setIsShowPrices(String isShowPrices) {
        this.isShowPrices = isShowPrices;
    }

    public String getIsShowUserInteger() {
        return isShowUserInteger;
    }

    public void setIsShowUserInteger(String isShowUserInteger) {
        this.isShowUserInteger = isShowUserInteger;
    }

    public String getRebateIcon() {
        return rebateIcon;
    }

    public void setRebateIcon(String rebateIcon) {
        this.rebateIcon = rebateIcon;
    }

    public String getBindDataID() {
        return bindDataID;
    }

    public void setBindDataID(String bindDataID) {
        this.bindDataID = bindDataID;
    }


//
//    public List<String> getDisplayItems() {
//        return displayItems;
//    }
//
//    public void setDisplayItems(List<String> displayItems) {
//        this.displayItems = displayItems;
//    }

//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public String getJifen() {
//        return jifen;
//    }
//
//    public void setJifen(String jifen) {
//        this.jifen = jifen;
//    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public String getPrice() {
//        return price;
//    }

//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public String getzPrice() {
//        return zPrice;
//    }
//
//    public void setzPrice(String zPrice) {
//        this.zPrice = zPrice;
//    }
//
//    public String getJifenUrl() {
//        return jifenUrl;
//    }
//
//    public void setJifenUrl(String jifenUrl) {
//        this.jifenUrl = jifenUrl;
//    }

    public int getIconWidth() {
        return iconWidth;
    }

    public void setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public void setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
    }

    public int getLeftMargion() {
        return leftMargion;
    }

    public void setLeftMargion(int leftMargion) {
        this.leftMargion = leftMargion;
    }

    public int getRightMargion() {
        return rightMargion;
    }

    public void setRightMargion(int rightMargion) {
        this.rightMargion = rightMargion;
    }

    public int getTopMargion() {
        return topMargion;
    }

    public void setTopMargion(int topMargion) {
        this.topMargion = topMargion;
    }

    public int getBottomMargion() {
        return bottomMargion;
    }

    public void setBottomMargion(int bottomMargion) {
        this.bottomMargion = bottomMargion;
    }


}
