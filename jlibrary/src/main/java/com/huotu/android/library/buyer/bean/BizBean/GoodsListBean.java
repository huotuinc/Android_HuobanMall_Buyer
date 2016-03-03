package com.huotu.android.library.buyer.bean.BizBean;

import java.util.List;

/**
 * 商品列表
 * Created by Administrator on 2016/3/1.
 */
public class GoodsListBean {
    /**
     *当前筛选出来的商品总数量
     */
    private int recordCount;
    /**
     * 页索引
     */
    private int pageIndex;
    /**
     * 商品列表
     */
    private List<GoodsBean> goods;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }
}
