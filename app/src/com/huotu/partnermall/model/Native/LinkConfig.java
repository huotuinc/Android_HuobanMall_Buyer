package com.huotu.partnermall.model.Native;

/**
 * Created by Administrator on 2016/3/8.
 */
public class LinkConfig {
    private HrefConfig self;
    private HrefConfig smartPage;
    private HrefConfig widgets;
    private HrefConfig classification;

    public HrefConfig getSelf() {
        return self;
    }

    public void setSelf(HrefConfig self) {
        this.self = self;
    }

    public HrefConfig getSmartPage() {
        return smartPage;
    }

    public void setSmartPage(HrefConfig smartPage) {
        this.smartPage = smartPage;
    }

    public HrefConfig getWidgets() {
        return widgets;
    }

    public void setWidgets(HrefConfig widgets) {
        this.widgets = widgets;
    }

    public HrefConfig getClassification() {
        return classification;
    }

    public void setClassification(HrefConfig classification) {
        this.classification = classification;
    }
}

