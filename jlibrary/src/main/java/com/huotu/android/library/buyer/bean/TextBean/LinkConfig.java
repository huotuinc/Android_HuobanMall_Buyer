package com.huotu.android.library.buyer.bean.TextBean;

/**
 * Created by Administrator on 2016/1/28.
 * Done
 */
public class LinkConfig {
    /**
     * 链接名称（导航名称）
     */
    private String text_Name;
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

    public String getText_Name() {
        return text_Name;
    }

    public void setText_Name(String text_Name) {
        this.text_Name = text_Name;
    }
}
