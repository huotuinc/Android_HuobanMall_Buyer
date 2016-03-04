package com.huotu.android.library.buyer.bean.GroupBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 * 商品分组系列组件
 * Created by jinxiangdong on 2016/2/19.
 * Done
 */
public class GoodsGroupConfig extends BaseConfig{
    /**
     * 布局:
     * card –卡片布局
     * normal –方格布局
     */
    private String goods_layout;
    /**
     * 是否显示积分：
     * show –显示积分
     * hide  -不显示
     */
    private String product_userInteger;
    /**
     * 分类列表集合
     */
    private List<GroupBean> Groups;

    public String getGoods_layout() {
        return goods_layout;
    }

    public void setGoods_layout(String goods_layout) {
        this.goods_layout = goods_layout;
    }

    public String getProduct_userInteger() {
        return product_userInteger;
    }

    public void setProduct_userInteger(String product_userInteger) {
        this.product_userInteger = product_userInteger;
    }

    public List<GroupBean> getGroups() {
        return Groups;
    }

    public void setGroups(List<GroupBean> groups) {
        Groups = groups;
    }
}
