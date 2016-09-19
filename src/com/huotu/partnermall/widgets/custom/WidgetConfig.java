package com.huotu.partnermall.widgets.custom;

import java.util.Map;

/**
 * 组件配置
 * Created by jinxiangdong on 2016/1/7.
 */
public class WidgetConfig {
    /**
     * 组件类型
     */
    private int type;
    /**
     * 组件版本号
     */
    private int version;
    /**
     * 组件详细配置信息
     */
    private Map properties;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }
}
