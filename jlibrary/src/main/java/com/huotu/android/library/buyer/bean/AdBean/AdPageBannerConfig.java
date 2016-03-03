package com.huotu.android.library.buyer.bean.AdBean;


import java.util.List;

/**
 * 滑动Banner组件
 * Created by jinxiangdong on 2016/1/14.
 */
public class AdPageBannerConfig extends AdBaseConfig {
    /**
     * 背景颜色
     */
    private String widgetBackColor;
    /**
     *
     */
    private List<AdImageBean> images;
    /**
     * 是否自动播放
     */
    private boolean autoPlay;
    /**
     * 显示数量
     */
    private int pageSize =4;

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }
}
