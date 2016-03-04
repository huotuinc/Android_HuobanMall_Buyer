package com.huotu.android.library.buyer.bean.AdBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 * 橱窗组件
 * Created by jinxiangdong on 2016/1/12.
 * Done
 */
public class AdWindowConfig extends BaseConfig {
    /**
     * 标题名
     */
    private String text_name;
    /**
     * 内容区标题
     */
    private String text_contentName;
    /**
     * 内容区说明
     */
    private String text_description;

    private List<AdImageBean> images;


    public String getText_name() {
        return text_name;
    }

    public void setText_name(String text_name) {
        this.text_name = text_name;
    }

    public String getText_contentName() {
        return text_contentName;
    }

    public void setText_contentName(String text_contentName) {
        this.text_contentName = text_contentName;
    }

    public String getText_description() {
        return text_description;
    }

    public void setText_description(String text_description) {
        this.text_description = text_description;
    }

    public List<AdImageBean> getImages() {
        return images;
    }

    public void setImages(List<AdImageBean> images) {
        this.images = images;
    }
}
