package com.huotu.android.library.buyer.bean.SearchBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * 搜索组件一
 * Created by jinxiangdong on 2016/1/14.
 * Done
 */
public class Search1Config extends BaseConfig{
    /**
     * 背景颜色，格式使用hex color格式,如:#ffffff
     */
    private String search_background;
    /**
     * 上下距离
     */
    private int paddingTop;
    /**
     * 左右距离
     */
    private int paddingLeft;
    /**
     * 搜索样式:
     * custom-search-style-A;
     * custom-search-style-B
     */
    private String search_style;

    public String getSearch_background() {
        return search_background;
    }

    public void setSearch_background(String search_background) {
        this.search_background = search_background;
    }

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

    public String getSearch_style() {
        return search_style;
    }

    public void setSearch_style(String search_style) {
        this.search_style = search_style;
    }
}
