package com.huotu.android.library.buyer.bean.AdBean;

import java.util.List;

/**
 * 单一图片组件
 * Created by jinxiangdong on 2016/1/12.
 */
public class AdOneConfig extends AdBaseConfig {
    private List<AdImageBean> images;

    public List<AdImageBean> getImages() {
        return images;
    }

    public void setImages(List<AdImageBean> images) {
        this.images = images;
    }
}
