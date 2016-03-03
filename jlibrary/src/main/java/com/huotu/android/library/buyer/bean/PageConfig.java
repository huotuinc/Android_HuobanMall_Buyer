package com.huotu.android.library.buyer.bean;

import java.util.List;

/**
 * Created by jinxiangdong on 2016/1/7.
 */
public class PageConfig {
    private int version;
    private List<WidgetConfig> widgets;



    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<WidgetConfig> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<WidgetConfig> widgets) {
        this.widgets = widgets;
    }
}
