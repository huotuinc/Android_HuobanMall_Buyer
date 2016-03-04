package com.huotu.android.library.buyer.bean.AsistBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 * 按钮布局组件
 * Created by jinxiangdong on 2016/2/18.
 * Done
 */
public class ButtonConfig extends BaseConfig {
    /**
     *背景颜色
     */
    private String backColor;
    /**
     * 高度
     */
    private int height;
    /**
     * 边框颜色
     */
    private String borderColor;
    /**
     * 边框圆边度
     */
    private int radius;
    /**
     * 按钮数组
     */
    private List<LinkBean> links;

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public List<LinkBean> getLinks() {
        return links;
    }

    public void setLinks(List<LinkBean> links) {
        this.links = links;
    }
}
