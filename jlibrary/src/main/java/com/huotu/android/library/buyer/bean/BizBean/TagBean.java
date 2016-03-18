package com.huotu.android.library.buyer.bean.BizBean;

/**
 * 标签
 * Created by Administrator on 2016/3/7.
 */
public class TagBean {
    /**
     * 标签id
     */
    private int tagId;
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 是否系统标签
     */
    private Boolean isSystem;
    /**
     * 标签类型
     */
    private String tagType;
    /**
     * 关联条数
     */
    private int relCount;
    /**
     * 商户Id
     */
    private int customerId;
    /**
     * 图标
     */
    private String tagImage;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public int getRelCount() {
        return relCount;
    }

    public void setRelCount(int relCount) {
        this.relCount = relCount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTagImage() {
        return tagImage;
    }

    public void setTagImage(String tagImage) {
        this.tagImage = tagImage;
    }
}
