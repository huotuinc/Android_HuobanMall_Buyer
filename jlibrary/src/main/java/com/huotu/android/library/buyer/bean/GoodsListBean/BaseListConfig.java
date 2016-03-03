package com.huotu.android.library.buyer.bean.GoodsListBean;


import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * Created by Administrator on 2016/1/6.
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
    * 每一页显示个数
     */
    private int pagesize = 3;
    /**
     * 列表布局
     * size-2  一大两小
     * size-3  详细列表
     */
    private String widgetLayout;
    /**
     * 列表样式
     * card  -卡片样式
     * normal -极简样式
     */
    private String gridStyle;
    /**
     * 是否显示商品名show  -显示
     * hide–不显示
     */
    private boolean isShowName;
    /**
     * 是否显示价格show  -显示
     * hide–不显示
     */
    private boolean isShowPrices;
    /**
     * 是否显示返利积分
     * show  -显示
     * hide–不显示
     */
    private boolean isShowUserInteger;
    /**
     * 商品分类ID,为0时则是全部商品的下拉列表
     */
    private  String bindDataID;

    /**
     * 返利图标(该图标资源存储的是相对地址，需要app配置一个资源地址拼接出资源绝对地址)。
     */
    private String rebateIcon;

    public boolean isOrderRule() {
        return orderRule;
    }

    public void setOrderRule(boolean orderRule) {
        this.orderRule = orderRule;
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

    public String getWidgetLayout() {
        return widgetLayout;
    }

    public void setWidgetLayout(String widgetLayout) {
        this.widgetLayout = widgetLayout;
    }

    public String getGridStyle() {
        return gridStyle;
    }

    public void setGridStyle(String gridStyle) {
        this.gridStyle = gridStyle;
    }

    public boolean isShowName() {
        return isShowName;
    }

    public void setIsShowName(boolean isShowName) {
        this.isShowName = isShowName;
    }

    public boolean isShowPrices() {
        return isShowPrices;
    }

    public void setIsShowPrices(boolean isShowPrices) {
        this.isShowPrices = isShowPrices;
    }

    public boolean isShowUserInteger() {
        return isShowUserInteger;
    }

    public void setIsShowUserInteger(boolean isShowUserInteger) {
        this.isShowUserInteger = isShowUserInteger;
    }

    public String getBindDataID() {
        return bindDataID;
    }

    public void setBindDataID(String bindDataID) {
        this.bindDataID = bindDataID;
    }

    public String getRebateIcon() {
        return rebateIcon;
    }

    public void setRebateIcon(String rebateIcon) {
        this.rebateIcon = rebateIcon;
    }
}
