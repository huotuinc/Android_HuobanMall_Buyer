package com.huotu.android.library.buyer.bean;

import java.util.List;

/**
 * Created by jinxiangdong on 2016/1/7.
 */
public class PageConfig {
    private int version;
    private List<WidgetConfig> widgets;
    /**
     * 标题
     */
    private String title;
    /**
     * 资源根地址
     */
    private String mallResourceURL;
    /**
     *
     */
    private String description;

    private String backgroundColor;

    private String createdTime;

    private String lastModifiedDate;

    private boolean enabled;

    private boolean indexed;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMallResourceURL() {
        return mallResourceURL;
    }

    public void setMallResourceURL(String mallResourceURL) {
        this.mallResourceURL = mallResourceURL;
    }

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
