package com.huotu.partnermall.ui.sis;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class SisConstant {
    //private static String SISBASEURL="http://192.168.1.48:8080/sis/";
    private static String SISBASEURL = "http://test.api.open.huobanplus.com:8081/sis/";

    public static String INTERFACE_getCategoryList= SISBASEURL + "getCategoryList";
    public static String INTERFACE_operGoods=SISBASEURL + "operGoods";
    public static String INTERFACE_searchGoodsList=SISBASEURL + "searchGoodsList";//
    public static String INTERFACE_searchSisGoodsList=SISBASEURL + "searchSisGoodsList";//查询店中店商品
    public static String INTERFACE_open = SISBASEURL + "open";//
    public static String INTERFACE_getSisProfile= SISBASEURL+ "getSisProfile";
    public static String INTERFACE_getTemplateList= SISBASEURL+ "getTemplateList";
    public static String INTERFACE_updateSisProfile= SISBASEURL+ "updateSisProfile";
    public static String INTERFACE_updateTemplate= SISBASEURL+ "updateTemplate";
    public static String INTERFACE_getSisInfo= SISBASEURL + "getSisInfo";//获取店铺信息


    public static List<SisSortModel> CATEGORY;//商品分类数据
    public static SisBaseinfoModel SHOPINFO;//店中店信息

    public static final int REFRESHGOODS_CODE = 7777;//刷新商品列表编号
    public static final int REFRESHSHOPINFO_CODE= 7778;//刷新店铺信息编码

}
