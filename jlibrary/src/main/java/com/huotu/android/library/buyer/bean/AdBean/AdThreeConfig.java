package com.huotu.android.library.buyer.bean.AdBean;


import java.util.List;

/**
 * 橱窗二组件 左一大右两小
 * Created by jinxiangdong on 2016/1/21.
 * Done
 */
public class AdThreeConfig extends AdBaseConfig {
    /**
     * 背景颜色，格式取Hex color格式，比如#ffffff
     */
    private String backcolor;
    private List<AdImageBean> images;

    public String getBackcolor() {
        return backcolor;
    }

    public void setBackcolor(String backcolor) {
        this.backcolor = backcolor;
    }

    public List<AdImageBean> getImages() {
        return images;
    }

    public void setImages(List<AdImageBean> images) {
        this.images = images;
    }
}
