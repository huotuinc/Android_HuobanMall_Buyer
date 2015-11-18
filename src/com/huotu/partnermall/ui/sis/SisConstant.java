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
    public static String INTERFACE_searchSisGoodsList=SISBASEURL + "searchSisGoodsList";//
    public static String INTERFACE_open = SISBASEURL + "open";//
    public static String INTERFACE_getSisProfile= SISBASEURL+ "getSisProfile";
    public static String INTERFACE_getTemplateList= SISBASEURL+ "getTemplateList";
    public static String INTERFACE_updateSisProfile= SISBASEURL+ "updateSisProfile";
    public static String INTERFACE_updateTemplate= SISBASEURL+ "updateTemplate";


    public static List<SisSortModel> CATEGORY;//商品分类数据
}
