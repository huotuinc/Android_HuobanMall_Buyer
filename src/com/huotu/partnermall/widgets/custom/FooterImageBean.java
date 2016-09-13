package com.huotu.partnermall.widgets.custom;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FooterImageBean {
    /**
     * 链接地址
     */
    private String linkUrl;
    /**
     * 链接名称（app忽略）
     */
    private String linkName;
    /**
     * 图标地址,需要通过app配置的根地址拼接拿到绝对地址
     */
    private String imageUrl;
    /**
     * 导航名称
     */
    private String name;
    /**
     * 高亮图标地址, 需要通过app配置的根地址拼接拿到绝对地址
     */
    private String heightImageUrl;
    /**
     * 底部菜单项图标的宽度
     */
    private int width;
    /**
     *
     */
    private int height;

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeightImageUrl() {
        return heightImageUrl;
    }

    public void setHeightImageUrl(String heightImageUrl) {
        this.heightImageUrl = heightImageUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
