package com.huotu.android.library.buyer.bean.TextBean;

/**
 * Created by Administrator on 2016/1/28.
 */
public class LinkConfig {
    /**
     * 链接名称（导航名称）
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

}
