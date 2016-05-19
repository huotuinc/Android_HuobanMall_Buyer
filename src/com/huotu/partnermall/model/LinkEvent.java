package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2016/1/28.
 */
public class LinkEvent {
    private String linkUrl;
    private String linkName;

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public LinkEvent(String linkName , String linkUrl ){
        this.linkUrl = linkUrl;
        this.linkName = linkName;
    }
}
