package com.huotu.android.library.buyer.bean.GroupBean;

/**
 * Created by Administrator on 2016/2/19.
 */
public class GroupBean {
    /**
     * 商品分类ID
     */
    private int id;
    /**
     * 商品分类名称
     */
    private String name;
    /**
     * 该分类显示商品数量
     */
    private int count;
    /**
     * 商户ID（可以忽略）
     */
    private int customerid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }
}
