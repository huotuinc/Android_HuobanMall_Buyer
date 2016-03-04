package com.huotu.android.library.buyer.bean.TextBean;

/**
 * Created by jinxiangdong on 2016/1/7.
 * 公告组件
 * Done
 *
 */
public class BillBoardConfig extends BaseTextConfig{
    /**
     * 公告内容
     */
    private String text_content;
    /**
     * 字体大小
     */
    private Integer fontSize;
    /**
     * 背景颜色 #FFFFF格式(Hex color)
     */
    private String text_background;
    /**
     * 字体颜色 #FFFFF格式(Hex color)
     */
    private String text_color;

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public String getText_background() {
        return text_background;
    }

    public void setText_background(String text_background) {
        this.text_background = text_background;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }
}
