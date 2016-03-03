package com.huotu.android.library.buyer.bean.GoodsBean;

/**
 * 二方格(默认样式)
 * Created by jinxiangdong on 2016/1/31.
 */
public class GoodsTwoConfig extends GoodsOneConfig {
    /**
     * 是否是瀑布流
     */
    private boolean styleLayout;

    public boolean isStyleLayout() {
        return styleLayout;
    }

    public void setStyleLayout(boolean styleLayout) {
        this.styleLayout = styleLayout;
    }
}
