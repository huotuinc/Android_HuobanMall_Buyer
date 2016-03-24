package com.huotu.android.library.buyer.bean.BizBean;

/**
 * Created by Administrator on 2016/2/24.
 */
public class BrandBean {
    /**
     *品牌id
     */
    private int brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 品牌图片
     */
    private String brandUrl;
    /**
     * 排序字段
     */
    private int orderNum;
    /**
     * 标记 是否 选中 状态
     */
    private boolean checked;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(String brandUrl) {
        this.brandUrl = brandUrl;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
