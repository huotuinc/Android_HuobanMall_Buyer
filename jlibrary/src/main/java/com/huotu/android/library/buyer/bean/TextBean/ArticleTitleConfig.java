package com.huotu.android.library.buyer.bean.TextBean;

/**
 * Created by jinxiangdong on 2016/1/7.
 * 微信标题组件
 * Done
 */
public class ArticleTitleConfig extends BaseTextConfig{
    /**
     * 标题名称
     */
    private String title_name;
    /**
     * 日期,一般为yyyy-MM-dd，但是也可以输入其他(可能不是日期格式，建议不要强制设置为时间类型)
     */
    private String title_time;
    /**
     * 作者
     */
    private String title_author;
    /**
     * 组件显示位置（text-left,text-center,text-right）左、中、右对齐。
     */
    private String title_position;
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
//    private int leftMargion;
//    private int rightMargion;
//    private int topMargion;
//    private int bottomMargion;

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getTitle_time() {
        return title_time;
    }

    public void setTitle_time(String title_time) {
        this.title_time = title_time;
    }

    public String getTitle_position() {
        return title_position;
    }

    public void setTitle_position(String title_position) {
        this.title_position = title_position;
    }

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

    public String getTitle_linkname() {
        return title_linkname;
    }

    public void setTitle_linkname(String title_linkname) {
        this.title_linkname = title_linkname;
    }

    public String getTitle_author() {
        return title_author;
    }

    public void setTitle_author(String title_author) {
        this.title_author = title_author;
    }

//    public int getLeftMargion() {
//        return leftMargion;
//    }
//
//    public void setLeftMargion(int leftMargion) {
//        this.leftMargion = leftMargion;
//    }
//
//    public int getRightMargion() {
//        return rightMargion;
//    }
//
//    public void setRightMargion(int rightMargion) {
//        this.rightMargion = rightMargion;
//    }

//    public int getTopMargion() {
//        return topMargion;
//    }
//
//    public void setTopMargion(int topMargion) {
//        this.topMargion = topMargion;
//    }
//
//    public int getBottomMargion() {
//        return bottomMargion;
//    }
//
//    public void setBottomMargion(int bottomMargion) {
//        this.bottomMargion = bottomMargion;
//    }
}
