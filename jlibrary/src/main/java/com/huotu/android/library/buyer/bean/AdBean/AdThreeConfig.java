package com.huotu.android.library.buyer.bean.AdBean;


import java.util.List;

/**
 * 橱窗二组件 左一大右两小
 * Created by jinxiangdong on 2016/1/21.
 */
public class AdThreeConfig extends AdBaseConfig {
    private String widgetBackColor;
    private List<AdImageBean> images;

    public String getWidgetBackColor() {
        return widgetBackColor;
    }

    public void setWidgetBackColor(String widgetBackColor) {
        this.widgetBackColor = widgetBackColor;
    }

    public List<AdImageBean> getImages() {
        return images;
    }

    public void setImages(List<AdImageBean> images) {
        this.images = images;
    }
}
