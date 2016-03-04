package com.huotu.android.library.buyer.bean.GoodsBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * 单方格(默认样式)
 * Created by jinxiangdong on 2016/1/25.
 * Done
 */
public class GoodsOneConfig extends BaseConfig {
    /**
     * 列表样式:
     * card   -卡片样式
     * normal –极简样式
     */
    private String goods_layer;
    /**
     * 是否显示商品名
     * show  -显示
     * hide–不显示
     */
    private String product_showname;
    /**
     * 是否显商品简介
     * show  -显示
     * hide –不显示
     */
    private String product_showsyno;
    /**
     * 是否显示价格
     * show  -显示
     * hide–不显示
     */
    private String product_showprices;
    /**
     * 是否显示返利积分
     * show  -显示
     * hide–不显示
     */
    private String product_userInteger;
    /**
     * 多个商品，ID用逗号隔开,如:1,2,3,4
     */
    private String bindDataID;
    /**
     * 返利图标(该图标资源存储的是相对地址，需要app配置一个资源地址拼接出资源绝对地址)。
     */
    private String background;


    private int iconWidth;
    private int iconHeight;

    private int leftMargion;
    private int rightMargion;
    private int topMargion;
    private int bottomMargion;

    public String getGoods_layer() {
        return goods_layer;
    }

    public void setGoods_layer(String goods_layer) {
        this.goods_layer = goods_layer;
    }

    public String getProduct_showname() {
        return product_showname;
    }

    public void setProduct_showname(String product_showname) {
        this.product_showname = product_showname;
    }

    public String getProduct_showsyno() {
        return product_showsyno;
    }

    public void setProduct_showsyno(String product_showsyno) {
        this.product_showsyno = product_showsyno;
    }

    public String getProduct_showprices() {
        return product_showprices;
    }

    public void setProduct_showprices(String product_showprices) {
        this.product_showprices = product_showprices;
    }

    public String getProduct_userInteger() {
        return product_userInteger;
    }

    public void setProduct_userInteger(String product_userInteger) {
        this.product_userInteger = product_userInteger;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBindDataID() {
        return bindDataID;
    }

    public void setBindDataID(String bindDataID) {
        this.bindDataID = bindDataID;
    }

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
