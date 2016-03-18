package com.huotu.android.library.buyer.bean.AdBean;

import java.util.List;

/**
 * 均分广告
 * Created by jinxiangdong on 2016/1/14.
 * Done
 */
public class AdAverageConfig extends AdBaseConfig {
    /**
     * 左外距离
     */
    private int paddingOutLeft=0;
    /**
     * 右外距离
     */
    private int paddingOutRight = 0;
    /**
     * 上下距离
     */
    //private int paddingTop = 0;
    /**
     * 左右距离
     */
    //private int paddingLeft=0;
    /**
     * 背景颜色，格式取Hex color格式，比如#ffffff
     */
    private String backcolor;
    /**
     *
     */
    private List<AdImageBean> images;

    public int getPaddingOutLeft() {
        return paddingOutLeft;
    }

    public void setPaddingOutLeft(int paddingOutLeft) {
        this.paddingOutLeft = paddingOutLeft;
    }

    public int getPaddingOutRight() {
        return paddingOutRight;
    }

    public void setPaddingOutRight(int paddingOutRight) {
        this.paddingOutRight = paddingOutRight;
    }

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
