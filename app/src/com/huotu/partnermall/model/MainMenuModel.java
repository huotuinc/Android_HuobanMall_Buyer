package com.huotu.partnermall.model;

import java.io.Serializable;

/**
 * 底部主菜单模型
 */
public
class MainMenuModel implements Serializable {

    //菜单名称
    private String mainMenuName;
    //菜单图标
    private String mainMenuIcon;
    //菜单标识
    private String mainMenuTag;
    //菜单url
    private String mainMenuUrl;

    public
    String getMainMenuName ( ) {
        return mainMenuName;
    }

    public
    void setMainMenuName ( String mainMenuName ) {
        this.mainMenuName = mainMenuName;
    }

    public
    String getMainMenuIcon ( ) {
        return mainMenuIcon;
    }

    public
    void setMainMenuIcon ( String mainMenuIcon ) {
        this.mainMenuIcon = mainMenuIcon;
    }

    public
    String getMainMenuTag ( ) {
        return mainMenuTag;
    }

    public
    void setMainMenuTag ( String mainMenuTag ) {
        this.mainMenuTag = mainMenuTag;
    }

    public
    String getMainMenuUrl ( ) {
        return mainMenuUrl;
    }

    public
    void setMainMenuUrl ( String mainMenuUrl ) {
        this.mainMenuUrl = mainMenuUrl;
    }
}