package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MenuLinkEvent {
    private String linkUrl;
    private String linkName;

    public MenuLinkEvent(String linkName , String linkUrl) {
        this.linkUrl = linkUrl;
        this.linkName = linkName;
    }

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

}
