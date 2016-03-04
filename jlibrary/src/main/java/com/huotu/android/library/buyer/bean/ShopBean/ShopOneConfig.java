package com.huotu.android.library.buyer.bean.ShopBean;


import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * 店铺头部组件一
 * Created by jinxiangdong on 2016/1/18.
 * Done
 */
public class ShopOneConfig extends BaseConfig {
    /**
     * 类型：
     * 0 –默认(默认时则根据api获得logo)
     * 1 -自定义(自定义则不同过api获得logo,使用用户上传的配置信息)
     */
    private int show_type;
    /**
     * 背景颜色,颜色值统一用hex color格式,比如#ffffff
     */
    private String backColor;
    /**
     * 字体颜色,按照hex color格式来,比如:#ffffff
     */
    private String fontColor;
    /**
     * 上下距离
     */
    private int paddingTop;
    /**
     * 左右距离
     */
    private int paddingLeft;
    /**
     * 左边图片
     */
    private String imageUrl1;
    /**
     * 文字
     */
    private String title_linkname1;
    /**
     *链接类型(app忽略)
     */
    private String linkType1;
    /**
     * 链接名称(app忽略)
     */
    private String linkName1;
    /**
     * 链接地址
     */
    private String linkUrl1;
    /**
     * 右边图标
     */
    private String imageUrl;
    /**
     * 右边图标名称
     */
    private String title_linkname;
    /**
     * 链接类型(app忽略)
     */
    private String linkType;
    /**
     * 链接名称(app忽略)
     */
    private String linkName;
    /**
     * 右边图标链接地址(默认提供搜索链接)
     */
    private String linkUrl;


    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
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

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getTitle_linkname1() {
        return title_linkname1;
    }

    public void setTitle_linkname1(String title_linkname1) {
        this.title_linkname1 = title_linkname1;
    }

    public String getLinkType1() {
        return linkType1;
    }

    public void setLinkType1(String linkType1) {
        this.linkType1 = linkType1;
    }

    public String getLinkName1() {
        return linkName1;
    }

    public void setLinkName1(String linkName1) {
        this.linkName1 = linkName1;
    }

    public String getLinkUrl1() {
        return linkUrl1;
    }

    public void setLinkUrl1(String linkUrl1) {
        this.linkUrl1 = linkUrl1;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle_linkname() {
        return title_linkname;
    }

    public void setTitle_linkname(String title_linkname) {
        this.title_linkname = title_linkname;
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
