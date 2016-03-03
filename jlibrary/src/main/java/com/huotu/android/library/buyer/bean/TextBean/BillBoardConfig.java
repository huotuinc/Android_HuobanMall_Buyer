package com.huotu.android.library.buyer.bean.TextBean;

/**
 * Created by jinxiangdong on 2016/1/7.
 * 公告组件
 */
public class BillBoardConfig extends BaseTextConfig{
    /**
     * 公告内容
     */
    private String content;
    /**
     * 字体大小
     */
    private Integer fontSize;
    /**
     * 背景颜色 #FFFFF格式(Hex color)
     */
    private String widgetBackColor;
    /**
     * 字体颜色 #FFFFF格式(Hex color)
     */
    private String widgetFontColor;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getWidgetBackColor() {
        return widgetBackColor;
    }

    public void setWidgetBackColor(String widgetBackColor) {
        this.widgetBackColor = widgetBackColor;
    }

    public String getWidgetFontColor() {
        return widgetFontColor;
    }

    public void setWidgetFontColor(String widgetFontColor) {
        this.widgetFontColor = widgetFontColor;
    }
}
