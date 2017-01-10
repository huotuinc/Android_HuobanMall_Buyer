package com.huotu.partnermall.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class AdBannerConfig {
    private List<AdImageBean> images;
    /**
     * 是否自动播放
     */
    private boolean autoPlay=false;
    private int width;
    private int height;
    private int interval = 1500;
    //private int totalSecond = 0;

    public List<AdImageBean> getImages() {
        return images;
    }

    public void setImages(List<AdImageBean> images) {
        this.images = images;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

//    public int getTotalSecond() {
//        return totalSecond;
//    }
//
//    public void setTotalSecond(int totalSecond) {
//        this.totalSecond = totalSecond;
//    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
