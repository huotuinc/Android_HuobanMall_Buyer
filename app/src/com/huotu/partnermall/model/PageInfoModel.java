package com.huotu.partnermall.model;

import java.io.Serializable;

/**
 * 页面信息模型
 */
public
class PageInfoModel implements Serializable {

    //页面标题
    private String pageTitle;
    //页面链接
    private String pageUrl;


    public
    String getPageTitle ( ) {
        return pageTitle;
    }

    public
    void setPageTitle ( String pageTitle ) {
        this.pageTitle = pageTitle;
    }

    public
    String getPageUrl ( ) {
        return pageUrl;
    }

    public
    void setPageUrl ( String pageUrl ) {
        this.pageUrl = pageUrl;
    }
}
