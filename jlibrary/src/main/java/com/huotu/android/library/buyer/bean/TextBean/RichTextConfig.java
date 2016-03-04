package com.huotu.android.library.buyer.bean.TextBean;

/**
 * 富文本组件
 * Created by jinxiangdong on 2016/1/7.
 * Done
 */
public class RichTextConfig extends BaseTextConfig {
    /**
     * 富文本内容(包含html)
     */
    private String ueditorValue;
    /**
     * 左右距离
     */
    private  int paddingLeft;
    /**
     * 上下距离
     */
    private int paddingTop;
    /**
     * 背景颜色 #FFFFF格式(Hex color)
     */
    private String ueditorBackColor;

    public String getUeditorValue() {
        return ueditorValue;
    }

    public void setUeditorValue(String ueditorValue) {
        this.ueditorValue = ueditorValue;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public String getUeditorBackColor() {
        return ueditorBackColor;
    }

    public void setUeditorBackColor(String ueditorBackColor) {
        this.ueditorBackColor = ueditorBackColor;
    }
}
