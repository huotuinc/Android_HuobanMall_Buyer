package com.huotu.android.library.buyer.bean.TextBean;

/**
 * Created by jinxiangdong on 2016/1/7.
 * 微信标题组件
 */
public class ArticleTitleConfig extends BaseTextConfig{
    private String subjectName;
    /**
     * 日期,一般为yyyy-MM-dd，但是也可以输入其他(可能不是日期格式，建议不要强制设置为时间类型)
     */
    private String date;
    /**
     * author
     */
    private String author;
    private String widgetAlign;
    private String linkName;
    private String linkUrl;
    private String linkType;
    private String linkTypeName;
    private int leftMargion;
    private int rightMargion;
    private int topMargion;
    private int bottomMargion;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getWidgetAlign() {
        return widgetAlign;
    }

    public void setWidgetAlign(String widgetAlign) {
        this.widgetAlign = widgetAlign;
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

    public String getLinkTypeName() {
        return linkTypeName;
    }

    public void setLinkTypeName(String linkTypeName) {
        this.linkTypeName = linkTypeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLeftMargion() {
        return leftMargion;
    }

    public void setLeftMargion(int leftMargion) {
        this.leftMargion = leftMargion;
    }

    public int getRightMargion() {
        return rightMargion;
    }

    public void setRightMargion(int rightMargion) {
        this.rightMargion = rightMargion;
    }

    public int getTopMargion() {
        return topMargion;
    }

    public void setTopMargion(int topMargion) {
        this.topMargion = topMargion;
    }

    public int getBottomMargion() {
        return bottomMargion;
    }

    public void setBottomMargion(int bottomMargion) {
        this.bottomMargion = bottomMargion;
    }
}
