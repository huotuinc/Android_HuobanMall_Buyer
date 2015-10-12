package com.huotu.partnermall.model;

/**
 * app系统配置文件
 */
public
class SysModel extends BaseBean {

    private String packageStr;
    private String sysMenu;

    public
    String getSysMenu ( ) {
        return sysMenu;
    }

    public
    void setSysMenu ( String sysMenu ) {
        this.sysMenu = sysMenu;
    }

    public
    String getPackageStr ( ) {
        return packageStr;
    }

    public
    void setPackageStr ( String packageStr ) {
        this.packageStr = packageStr;
    }
}
