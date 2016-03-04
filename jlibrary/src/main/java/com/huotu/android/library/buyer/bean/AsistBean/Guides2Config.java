package com.huotu.android.library.buyer.bean.AsistBean;


import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * 辅助线标题组件
 * Created by jinxiangdong on 2016/1/14.
 * Done
 */
public class Guides2Config extends BaseConfig {
    /**
     * 上下距离
     */
    private int paddingTop =0;
    /**
     * 左右距离
     */
    private int paddingLeft = 0;
    /**
     * 背景颜色
     */
    private String backColor;
    /**
     * 字体颜色
     */
    private String fontColor;
    /**
     * 文字标题
     */
    private String name;

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
