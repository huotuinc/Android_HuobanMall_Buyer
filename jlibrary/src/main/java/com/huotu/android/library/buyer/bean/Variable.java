package com.huotu.android.library.buyer.bean;

/**
 * Created by Administrator on 2016/3/10.
 */
public class Variable {
    /**
     * 商户id
     */
    public static int CustomerId=0;

    public static String BizKey="_demo";

    public static String BizAppSecure = "1f2f3f4f5f6f7f8f";
    /**
     * 获得 组件业务数据的 根地址
     */
    public static String BizRootUrl="http://api.open.fancat.cn:8081/";
    /**
     *  获得smartui页面组件配置信息的 根地址
     */
    public static String configRootUrl = "http://api.open.fancat.cn:8081/";

    /**
     * 当前用户等级
     */
    public static int userLevelId = 1;
    /**
     * 商户资源根地址
     */
    public static String resourceUrl="http://res.huobanj.cn";
    /**
     * 商户域名根地址
     */
    public static String siteUrl ="";
    /**
     * 获取主界面组件配置信息的地址
     */
    public static String mainUiConfigUrl = "";
}
