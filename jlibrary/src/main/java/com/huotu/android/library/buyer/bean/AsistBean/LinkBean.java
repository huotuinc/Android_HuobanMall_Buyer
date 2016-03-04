package com.huotu.android.library.buyer.bean.AsistBean;

/**
 * Created by Administrator on 2016/2/18.
 */
public class LinkBean {
    /**
     *按钮名称
     */
    private String name;
    /**
     *按钮背景颜色
     */
    private String backColor;
    /**
     *字体颜色
     */
    private String fontColor;
    /**
     *链接类型（app端可以忽略）
     */
    private String linkType;
    /**
     *链接名称（app端可以忽略）
     */
    private String linkName;
    /**
     *链接地址
     */
    private String linkUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
