package com.huotu.android.library.buyer.bean.ShopBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * Created by jinxiangdong on 2016/1/26.
 */
public class ShopDefaultConfig extends BaseConfig {
    /**
     * 背景类型：
     * 0 –背景图片方式
     * 1  -背景颜色方式
     * */
    private int widgetBackType;
    /**
     * 背景颜色,按照hex color格式来,比如:#ffffff
     */
    private String widgetBackColor;
    /**
     * 背景图片(存储的是相对地址,app端要配置一个资源跟地址来拿到完整的图片地址)
     */
    private String widgetBackImage;
//    private String shopName;
//    private String avatarUrl;
//    private int goodsCount;
//    private int newGoodsCount;
//    private int leftMargion;
//    private int rightMargion;
//    private int topMargion;
//    private int bottomMargion;

    public int getWidgetBackType() {
        return widgetBackType;
    }

    public void setWidgetBackType(int widgetBackType) {
        this.widgetBackType = widgetBackType;
    }

    public String getWidgetBackColor() {
        return widgetBackColor;
    }

    public void setWidgetBackColor(String widgetBackColor) {
        this.widgetBackColor = widgetBackColor;
    }

    public String getWidgetBackImage() {
        return widgetBackImage;
    }

    public void setWidgetBackImage(String widgetBackImage) {
        this.widgetBackImage = widgetBackImage;
    }

//    public String getShopName() {
//        return shopName;
//    }
//
//    public void setShopName(String shopName) {
//        this.shopName = shopName;
//    }
//
//    public String getAvatarUrl() {
//        return avatarUrl;
//    }
//
//    public void setAvatarUrl(String avatarUrl) {
//        this.avatarUrl = avatarUrl;
//    }

//    public int getGoodsCount() {
//        return goodsCount;
//    }
//
//    public void setGoodsCount(int goodsCount) {
//        this.goodsCount = goodsCount;
//    }
//
//    public int getNewGoodsCount() {
//        return newGoodsCount;
//    }
//
//    public void setNewGoodsCount(int newGoodsCount) {
//        this.newGoodsCount = newGoodsCount;
//    }

//    public int getLeftMargion() {
//        return leftMargion;
//    }
//
//    public void setLeftMargion(int leftMargion) {
//        this.leftMargion = leftMargion;
//    }
//
//    public int getRightMargion() {
//        return rightMargion;
//    }
//
//    public void setRightMargion(int rightMargion) {
//        this.rightMargion = rightMargion;
//    }
//
//    public int getTopMargion() {
//        return topMargion;
//    }
//
//    public void setTopMargion(int topMargion) {
//        this.topMargion = topMargion;
//    }
//
//    public int getBottomMargion() {
//        return bottomMargion;
//    }
//
//    public void setBottomMargion(int bottomMargion) {
//        this.bottomMargion = bottomMargion;
//    }
}
