package com.huotu.partnermall.model;

import java.io.Serializable;

/**
 * 分享参数模型
 */
public
class ShareMsgModel implements Serializable {

    //title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    private String shareTitle;
    //titleUrl是标题的网络链接，仅在人人网和QQ空间使用
    private String shareTitleUrl;
    //text是分享文本，所有平台都需要这个字段
    private String shareText;
    //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    private String shareImagePath;
    //url仅在微信（包括好友和朋友圈）中使用
    private String shareUrl;
    //comment是我对这条分享的评论，仅在人人网和QQ空间使用
    private String shareComment;
    //site是分享此内容的网站名称，仅在QQ空间使用
    private String shareSite;
    //siteUrl是分享此内容的网站地址，仅在QQ空间使用
    private String shareSiteUrl;

    public
    String getShareTitle ( ) {
        return shareTitle;
    }

    public
    void setShareTitle ( String shareTitle ) {
        this.shareTitle = shareTitle;
    }

    public
    String getShareTitleUrl ( ) {
        return shareTitleUrl;
    }

    public
    void setShareTitleUrl ( String shareTitleUrl ) {
        this.shareTitleUrl = shareTitleUrl;
    }

    public
    String getShareText ( ) {
        return shareText;
    }

    public
    void setShareText ( String shareText ) {
        this.shareText = shareText;
    }

    public
    String getShareImagePath ( ) {
        return shareImagePath;
    }

    public
    void setShareImagePath ( String shareImagePath ) {
        this.shareImagePath = shareImagePath;
    }

    public
    String getShareUrl ( ) {
        return shareUrl;
    }

    public
    void setShareUrl ( String shareUrl ) {
        this.shareUrl = shareUrl;
    }

    public
    String getShareComment ( ) {
        return shareComment;
    }

    public
    void setShareComment ( String shareComment ) {
        this.shareComment = shareComment;
    }

    public
    String getShareSite ( ) {
        return shareSite;
    }

    public
    void setShareSite ( String shareSite ) {
        this.shareSite = shareSite;
    }

    public
    String getShareSiteUrl ( ) {
        return shareSiteUrl;
    }

    public
    void setShareSiteUrl ( String shareSiteUrl ) {
        this.shareSiteUrl = shareSiteUrl;
    }
}
