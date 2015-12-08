package com.huotu.partnermall.ui.sis;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class SisConstant {
    private static String SISBASEURL = "http://test.api.open.huobanplus.com:8081/";
    //private static String SISBASEURL="http://192.168.1.48:8080/sis/";
    //private static String SISBASEURL="http://192.168.1.57:8080/sis/";
    private static String SISBASEAPPURL = SISBASEURL + "sis/";

    public static String INTERFACE_getCategoryList= SISBASEAPPURL + "getCategoryList";
    public static String INTERFACE_operGoods=SISBASEAPPURL + "operGoods";
    public static String INTERFACE_searchGoodsList=SISBASEAPPURL + "searchGoodsList";//
    public static String INTERFACE_searchSisGoodsList=SISBASEAPPURL + "searchSisGoodsList";//查询店中店商品
    public static String INTERFACE_open = SISBASEAPPURL + "open";//
    public static String INTERFACE_getSisProfile= SISBASEAPPURL+ "getSisProfile";
    public static String INTERFACE_getTemplateList= SISBASEAPPURL+ "getTemplateList";
    public static String INTERFACE_updateSisProfile= SISBASEAPPURL+ "updateSisProfile";
    public static String INTERFACE_updateTemplate= SISBASEAPPURL+ "updateTemplate";
    public static String INTERFACE_getSisInfo= SISBASEAPPURL + "getSisInfo";//获取店铺信息
    //public static String INTERFACE_getGoodDetails = SISBASEURL + "getGoodDetails";//商品详细页面地址

    public static List<SisSortModel> CATEGORY;//商品分类数据
    public static SisBaseinfoModel SHOPINFO;//店中店信息

    public static final int REFRESHGOODS_CODE = 7777;//刷新商品列表编号
    public static final int REFRESHSHOPINFO_CODE= 7778;//刷新店铺信息编码

}
