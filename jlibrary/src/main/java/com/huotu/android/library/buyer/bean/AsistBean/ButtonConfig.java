package com.huotu.android.library.buyer.bean.AsistBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 * Created by Administrator on 2016/2/18.
 */
public class ButtonConfig extends BaseConfig {
    private String widgetBackColor;
    private int widgetHeight;
    private String widgetBorderColor;
    private int widgetBorderRadius;
    private List<LinkBean> links;

    public String getWidgetBackColor() {
        return widgetBackColor;
    }

    public void setWidgetBackColor(String widgetBackColor) {
        this.widgetBackColor = widgetBackColor;
    }

    public int getWidgetHeight() {
        return widgetHeight;
    }

    public void setWidgetHeight(int widgetHeight) {
        this.widgetHeight = widgetHeight;
    }

    public String getWidgetBorderColor() {
        return widgetBorderColor;
    }

    public void setWidgetBorderColor(String widgetBorderColor) {
        this.widgetBorderColor = widgetBorderColor;
    }

    public int getWidgetBorderRadius() {
        return widgetBorderRadius;
    }

    public void setWidgetBorderRadius(int widgetBorderRadius) {
        this.widgetBorderRadius = widgetBorderRadius;
    }

    public List<LinkBean> getLinks() {
        return links;
    }

    public void setLinks(List<LinkBean> links) {
        this.links = links;
    }
}
