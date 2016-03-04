package com.huotu.android.library.buyer.bean.AdBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 * Banner图组件 可以添加多张Banner图片，同时图片大小上传的时候一般为相同大小
 * Created by jinxiangdong on 2016/1/13.
 * Done
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
