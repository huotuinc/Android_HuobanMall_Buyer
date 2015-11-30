package com.huotu.partnermall.model;

import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class UpdateLeftInfoModel {
    private int code;
    private String msg;
    private InnerClass data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public InnerClass getData() {
        return data;
    }

    public void setData(InnerClass data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class InnerClass{
        private String levelName;
        //1,  --0 不返回home_menus
        private int menusCode;
        private List<MenuModel> home_menus;

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public List<MenuModel> getHome_menus() {
            return home_menus;
        }

        public void setHome_menus(List<MenuModel> home_menus) {
            this.home_menus = home_menus;
        }

        public int getMenusCode() {
            return menusCode;
        }

        public void setMenusCode(int menusCode) {
            this.menusCode = menusCode;
        }
    }

    public class MenuModel {
        private int menu_group;
        private String menu_name;
        private String menu_url;
        private String menu_icon;
        private String menu_icon_url;

        public int getMenu_group() {
            return menu_group;
        }

        public void setMenu_group(int menu_group) {
            this.menu_group = menu_group;
        }

        public String getMenu_name() {
            return menu_name;
        }

        public void setMenu_name(String menu_name) {
            this.menu_name = menu_name;
        }

        public String getMenu_url() {
            return menu_url;
        }

        public void setMenu_url(String menu_url) {
            this.menu_url = menu_url;
        }

        public String getMenu_icon() {
            return menu_icon;
        }

        public void setMenu_icon(String menu_icon) {
            this.menu_icon = menu_icon;
        }

        public String getMenu_icon_url() {
            return menu_icon_url;
        }

        public void setMenu_icon_url(String menu_icon_url) {
            this.menu_icon_url = menu_icon_url;
        }
    }
}