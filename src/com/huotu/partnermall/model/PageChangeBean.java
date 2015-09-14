package com.huotu.partnermall.model;

/**
 * 页面切换实体
 */
public
class PageChangeBean extends BaseBean {

    /**
     * 页面url
     */
    private String url;
    /**
     * 标题栏左图标资源
     */
    private int    titleLftIcon;
    /**
     * 标题栏右图标资源
     */
    private int    titleRightIcon;
    /**
     * 标题
     */
    private String titleText;


    public
    String getUrl ( ) {
        return url;
    }

    public
    void setUrl ( String url ) {
        this.url = url;
    }

    public
    int getTitleLftIcon ( ) {
        return titleLftIcon;
    }

    public
    void setTitleLftIcon ( int titleLftIcon ) {
        this.titleLftIcon = titleLftIcon;
    }

    public
    int getTitleRightIcon ( ) {
        return titleRightIcon;
    }

    public
    void setTitleRightIcon ( int titleRightIcon ) {
        this.titleRightIcon = titleRightIcon;
    }

    public
    String getTitleText ( ) {
        return titleText;
    }

    public
    void setTitleText ( String titleText ) {
        this.titleText = titleText;
    }
}
