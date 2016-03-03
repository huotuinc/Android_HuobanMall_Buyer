package com.huotu.android.library.buyer.bean.AdBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 * Created by jinxiangdong on 2016/1/13.
 */
public class AdBannerConfig extends BaseConfig {
    private List<AdImageBean> images;
    /**
     * 是否自动播放
     */
    private boolean autoPlay=false;
    private int width;
    private int height;

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
}
