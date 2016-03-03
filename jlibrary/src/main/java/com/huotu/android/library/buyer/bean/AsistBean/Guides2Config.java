package com.huotu.android.library.buyer.bean.AsistBean;


import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * 辅助线标题组件
 * Created by jinxiangdong on 2016/1/14.
 */
public class Guides2Config extends BaseConfig {
    private int verticalDistance =0;
    private int aroundDistance = 0;
    private String widgetBackColor;
    private String widgetFontColor;
    private String name;

    public int getVerticalDistance() {
        return verticalDistance;
    }

    public void setVerticalDistance(int verticalDistance) {
        this.verticalDistance = verticalDistance;
    }

    public int getAroundDistance() {
        return aroundDistance;
    }

    public void setAroundDistance(int aroundDistance) {
        this.aroundDistance = aroundDistance;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
