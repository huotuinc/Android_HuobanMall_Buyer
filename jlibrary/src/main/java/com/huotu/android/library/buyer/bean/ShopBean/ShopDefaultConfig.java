package com.huotu.android.library.buyer.bean.ShopBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * 店铺头部组件
 * Created by jinxiangdong on 2016/1/26.
 * done
 */
public class ShopDefaultConfig extends BaseConfig {
    /**
     * 背景类型：
     * 0 –背景图片方式
     * 1  -背景颜色方式
     * */
    private int show_type;
    /**
     * 背景颜色,按照hex color格式来,比如:#ffffff
     */
    private String color;
    /**
     * 背景图片(存储的是相对地址,app端要配置一个资源跟地址来拿到完整的图片地址)
     */
    private String background;


    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
