package com.huotu.partnermall.model;

import java.io.Serializable;

/**
 * 分享参数模型
 */
public
class ShareMsgModel implements Serializable {

    //text是分享文本，所有平台都需要这个字段
    private String text;
    //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    private String imagePath;
    //url仅在微信（包括好友和朋友圈）中使用
    private String url;
    //site是分享此内容的网站名称，仅在QQ空间使用
    private String site;
    //siteUrl是分享此内容的网站地址，仅在QQ空间使用
    private String siteUrl;
    private String title;
    private String titleUrl;

    public
    String getText ( ) {
        return text;
    }

    public
    void setText ( String text ) {
        this.text = text;
    }

    public
    String getImagePath ( ) {
        return imagePath;
    }

    public
    void setImagePath ( String imagePath ) {
        this.imagePath = imagePath;
    }

    public
    String getUrl ( ) {
        return url;
    }

    public
    void setUrl ( String url ) {
        this.url = url;
    }

    public
    String getSite ( ) {
        return site;
    }

    public
    void setSite ( String site ) {
        this.site = site;
    }

    public
    String getSiteUrl ( ) {
        return siteUrl;
    }

    public
    void setSiteUrl ( String siteUrl ) {
        this.siteUrl = siteUrl;
    }

    public
    String getTitle ( ) {
        return title;
    }

    public
    void setTitle ( String title ) {
        this.title = title;
    }

    public
    String getTitleUrl ( ) {
        return titleUrl;
    }

    public
    void setTitleUrl ( String titleUrl ) {
        this.titleUrl = titleUrl;
    }
}
