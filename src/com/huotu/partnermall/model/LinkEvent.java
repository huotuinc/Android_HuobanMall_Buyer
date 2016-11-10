package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2016/1/28.
 */
public class LinkEvent {
    private String linkUrl;
    private String linkName;
    /***
     * 标记是否通过外部浏览器打开链接，默认外部打开
     */
    private boolean openUrlByBrowser=true;

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

    public boolean isOpenUrlByBrowser() {
        return openUrlByBrowser;
    }

    public void setOpenUrlByBrowser(boolean openUrlByBrowser) {
        this.openUrlByBrowser = openUrlByBrowser;
    }

    public LinkEvent(String linkName , String linkUrl , boolean openUrlByBrowser){
        this.linkUrl = linkUrl;
        this.linkName = linkName;
        this.openUrlByBrowser = openUrlByBrowser;
    }
}
