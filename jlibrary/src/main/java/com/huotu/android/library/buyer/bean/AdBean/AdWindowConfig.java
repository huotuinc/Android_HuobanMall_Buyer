package com.huotu.android.library.buyer.bean.AdBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 * 橱窗组件
 * Created by jinxiangdong on 2016/1/12.
 */
public class AdWindowConfig extends BaseConfig {
    /**
     * 标题名
     */
    private String title;
    /**
     * 内容区标题
     */
    private String contentTitle;
    /**
     * 内容区说明
     */
    private String contentCaption;

    private List<AdImageBean> images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentCaption() {
        return contentCaption;
    }

    public void setContentCaption(String contentCaption) {
        this.contentCaption = contentCaption;
    }

    public List<AdImageBean> getImages() {
        return images;
    }

    public void setImages(List<AdImageBean> images) {
        this.images = images;
    }
}
