package com.huotu.android.library.buyer.bean.AdBean;

import java.util.List;

/**
 * 均分广告
 * Created by jinxiangdong on 2016/1/14.
 */
public class AdAverageConfig extends AdBaseConfig {
    /**
     * 左外距离
     */
    //private int leftDistance=0;
    /**
     * 右外距离
     */
    //private int rightDistance = 0;
    /**
     * 上下距离
     */
    private int upDownDistance = 0;
    /**
     * 左右距离
     */
    private int leftRightDistance=0;
    private String widgetBackColor;
    private List<AdImageBean> images;

    public int getUpDownDistance() {
        return upDownDistance;
    }

    public void setUpDownDistance(int upDownDistance) {
        this.upDownDistance = upDownDistance;
    }

    public int getLeftRightDistance() {
        return leftRightDistance;
    }

    public void setLeftRightDistance(int leftRightDistance) {
        this.leftRightDistance = leftRightDistance;
    }

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
