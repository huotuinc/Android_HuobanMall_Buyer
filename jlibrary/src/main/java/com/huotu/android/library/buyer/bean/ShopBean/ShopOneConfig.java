package com.huotu.android.library.buyer.bean.ShopBean;


import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * Created by jinxiangdong on 2016/1/18.
 */
public class ShopOneConfig extends BaseConfig {
    /**
     * 类型：
     * 0 –默认(默认时则根据api获得logo)
     * 1  -自定义(自定义则不同过api获得logo,使用用户上传的配置信息)
     */
    private int type;
    /**
     * 背景颜色,颜色值统一用hex color格式,比如#ffffff
     */
    private String widgetBackColor;
    /**
     * 字体颜色,按照hex color格式来,比如:#ffffff
     */
    private String widgetFontColor;
    private int aroundDistance;
    private int verticalDistance;
    private String leftImageUrl;
    private String leftLinkName;
    private String leftLinkType;
    private String leftLinkTypeName;
    private String leftLinkUrl;
    private String rightImageUrl;
    private String rightLinkName;
    private String rightLinkType;
    private String rightLinkTypeName;
    private String rightLinkUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWidgetBackColor() {
        return widgetBackColor;
    }

    public void setWidgetBackColor(String widgetBackColor) {
        this.widgetBackColor = widgetBackColor;
    }

    public String getWidgetFontColor() {
        return widgetFontColor;
    }

    public void setWidgetFontColor(String widgetFontColor) {
        this.widgetFontColor = widgetFontColor;
    }

    public int getAroundDistance() {
        return aroundDistance;
    }

    public void setAroundDistance(int aroundDistance) {
        this.aroundDistance = aroundDistance;
    }

    public int getVerticalDistance() {
        return verticalDistance;
    }

    public void setVerticalDistance(int verticalDistance) {
        this.verticalDistance = verticalDistance;
    }

    public String getLeftImageUrl() {
        return leftImageUrl;
    }

    public void setLeftImageUrl(String leftImageUrl) {
        this.leftImageUrl = leftImageUrl;
    }

    public String getLeftLinkName() {
        return leftLinkName;
    }

    public void setLeftLinkName(String leftLinkName) {
        this.leftLinkName = leftLinkName;
    }

    public String getLeftLinkType() {
        return leftLinkType;
    }

    public void setLeftLinkType(String leftLinkType) {
        this.leftLinkType = leftLinkType;
    }

    public String getLeftLinkTypeName() {
        return leftLinkTypeName;
    }

    public void setLeftLinkTypeName(String leftLinkTypeName) {
        this.leftLinkTypeName = leftLinkTypeName;
    }

    public String getLeftLinkUrl() {
        return leftLinkUrl;
    }

    public void setLeftLinkUrl(String leftLinkUrl) {
        this.leftLinkUrl = leftLinkUrl;
    }

    public String getRightImageUrl() {
        return rightImageUrl;
    }

    public void setRightImageUrl(String rightImageUrl) {
        this.rightImageUrl = rightImageUrl;
    }

    public String getRightLinkName() {
        return rightLinkName;
    }

    public void setRightLinkName(String rightLinkName) {
        this.rightLinkName = rightLinkName;
    }

    public String getRightLinkType() {
        return rightLinkType;
    }

    public void setRightLinkType(String rightLinkType) {
        this.rightLinkType = rightLinkType;
    }

    public String getRightLinkTypeName() {
        return rightLinkTypeName;
    }

    public void setRightLinkTypeName(String rightLinkTypeName) {
        this.rightLinkTypeName = rightLinkTypeName;
    }

    public String getRightLinkUrl() {
        return rightLinkUrl;
    }

    public void setRightLinkUrl(String rightLinkUrl) {
        this.rightLinkUrl = rightLinkUrl;
    }
}
