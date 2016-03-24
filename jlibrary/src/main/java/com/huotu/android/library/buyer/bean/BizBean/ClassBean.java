package com.huotu.android.library.buyer.bean.BizBean;

/**
 * 分类信息
 * Created by jinxiangdong on 2016/2/24.
 */
public class ClassBean {
    /**
     * 分类id
     */
   private int  catId;
    /**
     * 父分类id，一级分类父分类为0
     */
    private int parentId;
    /**
     * 分类路径，例如   “|1|2|”
     */
    private String catPath;
    /**
     * 分类名称
     */
    private String catName;
    /**
     * 直接子类目的数量
     */
    private int childCount;
    /**
     * 商品数量
     */
    private int goodCount;

    /**
     * 是否选择
     */
    private boolean checked;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
