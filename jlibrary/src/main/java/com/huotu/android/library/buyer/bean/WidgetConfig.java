package com.huotu.android.library.buyer.bean;

/**
 * 组件配置
 * Created by jinxiangdong on 2016/1/7.
 */
public class WidgetConfig {
    private WidgetTypeEnum widgetTypeEnum;
    private String jsonString;

    public WidgetTypeEnum getWidgetTypeEnum() {
        return widgetTypeEnum;
    }

    public void setWidgetTypeEnum(WidgetTypeEnum widgetTypeEnum) {
        this.widgetTypeEnum = widgetTypeEnum;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
