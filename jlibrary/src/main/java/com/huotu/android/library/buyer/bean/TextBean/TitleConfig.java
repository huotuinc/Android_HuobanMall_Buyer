package com.huotu.android.library.buyer.bean.TextBean;

/**
 * 标题组件 配置信息
 * Created by jinxiangdong on 2016/1/7.
 */
public class TitleConfig extends BaseTextConfig {
    /**
     * 标题名称
     */
    private String subjectName;
    /**
     * 标题字体大小
     */
    private int subjectFontSize;
    /**
     * 子标题
     */
    private String subTitle;
    /**
     * 组件显示位置
     * （text-left,text-center,text-right）左、中、右对齐。
     */
    private String widgetAlign;
    /**
     * 背景颜色 #ffffff格式(Hex color)
     */
    private String widgetBackColor;
    /**
     * 链接名称
     */
    private String linkName;
    /**
     * 链接地址
     */
    private String linkUrl;
    /**
     * 链接类型(目前可以忽略,只用于服务端编辑时使用)
     */
    private String linkType;
    /**
     * 链接类型名称(目前可以忽略, 只用于服务端编辑时使用)
     */
    private String linkTypeName;


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectFontSize() {
        return subjectFontSize;
    }

    public void setSubjectFontSize(int subjectFontSize) {
        this.subjectFontSize = subjectFontSize;
    }

    public String getWidgetAlign() {
        return widgetAlign;
    }

    public void setWidgetAlign(String widgetAlign) {
        this.widgetAlign = widgetAlign;
    }

    public String getWidgetBackColor() {
        return widgetBackColor;
    }

    public void setWidgetBackColor(String widgetBackColor) {
        this.widgetBackColor = widgetBackColor;
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

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkTypeName() {
        return linkTypeName;
    }

    public void setLinkTypeName(String linkTypeName) {
        this.linkTypeName = linkTypeName;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
