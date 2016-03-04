package com.huotu.android.library.buyer.bean.TextBean;

/**
 * 标题组件 配置信息
 * Created by jinxiangdong on 2016/1/7.
 * Done
 */
public class TitleConfig extends BaseTextConfig {
    /**
     * 标题名称
     */
    private String title_name;
    /**
     * 标题字体大小
     */
    private int fontSize;
    /**
     * 子标题
     */
    private String title_subname;
    /**
     * 组件显示位置
     * （text-left,text-center,text-right）左、中、右对齐。
     */
    private String title_position;
    /**
     * 背景颜色 #ffffff格式(Hex color)
     */
    private String title_background;
    /**
     * 链接名称
     */
    private String title_linkname;
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
    private String linkName;


    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getTitle_position() {
        return title_position;
    }

    public void setTitle_position(String title_position) {
        this.title_position = title_position;
    }

    public String getTitle_background() {
        return title_background;
    }

    public void setTitle_background(String title_background) {
        this.title_background = title_background;
    }

    public String getTitle_linkname() {
        return title_linkname;
    }

    public void setTitle_linkname(String title_linkname) {
        this.title_linkname = title_linkname;
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

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getTitle_subname() {
        return title_subname;
    }

    public void setTitle_subname(String title_subname) {
        this.title_subname = title_subname;
    }
}
