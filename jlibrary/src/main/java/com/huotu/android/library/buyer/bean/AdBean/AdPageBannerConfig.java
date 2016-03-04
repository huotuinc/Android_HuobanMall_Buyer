package com.huotu.android.library.buyer.bean.AdBean;


import java.util.List;

/**
 * 滑动Banner组件
 * Created by jinxiangdong on 2016/1/14.
 * Done
 */
public class AdPageBannerConfig extends AdBaseConfig {
    /**
     * 背景颜色
     */
    private String backcolor;
    /**
     *多条图片信息
     */
    private List<AdImageBean> images;
    /**
     * 是否自动播放
     */
    private boolean swiperStop;
    /**
     * 显示数量 ,可以为小数,比如4.5，则显示4.5张图片
     */
    private float swiperPage =4;

    public List<AdImageBean> getImages() {
        return images;
    }

    public void setImages(List<AdImageBean> images) {
        this.images = images;
    }

    public String getBackcolor() {
        return backcolor;
    }

    public void setBackcolor(String backcolor) {
        this.backcolor = backcolor;
    }

    public boolean isSwiperStop() {
        return swiperStop;
    }

    public void setSwiperStop(boolean swiperStop) {
        this.swiperStop = swiperStop;
    }

    public float getSwiperPage() {
        return swiperPage;
    }

    public void setSwiperPage(float swiperPage) {
        this.swiperPage = swiperPage;
    }
}
