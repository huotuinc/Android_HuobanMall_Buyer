package com.huotu.android.library.buyer.bean.Data;

/**
 * Created by Administrator on 2016/3/8.
 */
public class FindLinkConfig {
    private FindHrefConfig self;
    private FindHrefConfig smartPage;
    private FindHrefConfig widgets;
    private FindHrefConfig classification;

    public FindHrefConfig getSelf() {
        return self;
    }

    public void setSelf(FindHrefConfig self) {
        this.self = self;
    }

    public FindHrefConfig getSmartPage() {
        return smartPage;
    }

    public void setSmartPage(FindHrefConfig smartPage) {
        this.smartPage = smartPage;
    }

    public FindHrefConfig getWidgets() {
        return widgets;
    }

    public void setWidgets(FindHrefConfig widgets) {
        this.widgets = widgets;
    }

    public FindHrefConfig getClassification() {
        return classification;
    }

    public void setClassification(FindHrefConfig classification) {
        this.classification = classification;
    }
}

