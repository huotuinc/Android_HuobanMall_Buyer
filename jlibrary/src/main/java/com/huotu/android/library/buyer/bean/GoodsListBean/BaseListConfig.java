package com.huotu.android.library.buyer.bean.GoodsListBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * Created by jinxiangdong on 2016/1/6.
 */
public class BaseListConfig extends BaseConfig {
    /**
     * 是否启用排序
     */
    private boolean orderRule = false;
    /**
     * 是否启用筛选
     */
    private boolean filterRule=false;
    /**
     * 是否瀑布
     */
    private boolean styleLayout=false;
    /**
    * 每一页显示个数
     */
    private int pagesize = 5;
    /**
     * 列表布局
     * size-2  一大两小
     * size-3  详细列表
     */
    private String goods_layout;
    /**
     * 列表样式
     * card  -卡片样式
     * normal -极简样式
     */
    private String goods_layer;
    /**
     * 是否显示商品名
     * show-显示
     * hide–不显示
     */
    private boolean product_showname;
    /**
     * 是否显示价格
     * show-显示
     * hide–不显示
     */
    private boolean product_showprices;
    /**
     * 是否显示返利积分
     * show-显示
     * hide–不显示
     */
    private boolean product_userInteger;
    /**
     * 商品分类ID,为0时则是全部商品的下拉列表
     */
    private  String bindDataID;
    /**
     * 返利图标(该图标资源存储的是相对地址，需要app配置一个资源地址拼接出资源绝对地址)。
     */
    private String background;

    public boolean isOrderRule() {
        return orderRule;
    }

    public void setOrderRule(boolean orderRule) {
        this.orderRule = orderRule;
    }

    public boolean isStyleLayout() {
        return styleLayout;
    }

    public void setStyleLayout(boolean styleLayout) {
        this.styleLayout = styleLayout;
    }

    public boolean isFilterRule() {
        return filterRule;
    }

    public void setFilterRule(boolean filterRule) {
        this.filterRule = filterRule;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String getGoods_layout() {
        return goods_layout;
    }

    public void setGoods_layout(String goods_layout) {
        this.goods_layout = goods_layout;
    }

    public String getGoods_layer() {
        return goods_layer;
    }

    public void setGoods_layer(String goods_layer) {
        this.goods_layer = goods_layer;
    }

    public boolean isProduct_showname() {
        return product_showname;
    }

    public void setProduct_showname(boolean product_showname) {
        this.product_showname = product_showname;
    }

    public boolean isProduct_showprices() {
        return product_showprices;
    }

    public void setProduct_showprices(boolean product_showprices) {
        this.product_showprices = product_showprices;
    }

    public boolean isProduct_userInteger() {
        return product_userInteger;
    }

    public void setProduct_userInteger(boolean product_userInteger) {
        this.product_userInteger = product_userInteger;
    }

    public String getBindDataID() {
        return bindDataID;
    }

    public void setBindDataID(String bindDataID) {
        this.bindDataID = bindDataID;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
