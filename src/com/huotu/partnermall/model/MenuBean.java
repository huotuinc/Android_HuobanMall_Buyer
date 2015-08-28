package com.huotu.partnermall.model;

/**
 * 底部菜单实体
 */
public
class MenuBean extends BaseBean {

    //菜单名称
    private String menuName;
    //菜单图标
    private String menuIcon;

    public
    String getMenuName ( ) {
        return menuName;
    }

    public
    void setMenuName ( String menuName ) {
        this.menuName = menuName;
    }

    public
    String getMenuIcon ( ) {
        return menuIcon;
    }

    public
    void setMenuIcon ( String menuIcon ) {
        this.menuIcon = menuIcon;
    }
}
