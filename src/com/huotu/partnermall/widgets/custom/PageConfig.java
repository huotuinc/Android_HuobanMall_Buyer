package com.huotu.partnermall.widgets.custom;

import java.util.List;

/**
 * Created by jinxiangdong on 2016/1/7.
 */
public class PageConfig {
    private List<WidgetConfig> widgets;
    /**
     * 标题
     */
    //private String title;
    /**
     * 资源根地址
     */
    private String mallResourceURL;

//    public String getTitle() {
//        return title;
//    }

//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getMallResourceURL() {
        return mallResourceURL;
    }

    public void setMallResourceURL(String mallResourceURL) {
        this.mallResourceURL = mallResourceURL;
    }

    public List<WidgetConfig> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<WidgetConfig> widgets) {
        this.widgets = widgets;
    }
}
