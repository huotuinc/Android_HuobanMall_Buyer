package com.huotu.android.library.buyer.bean;

/**
 *
 * Created by jinxiangdong on 2016/1/27.
 */
public class Constant {
    /**
     * 文字对齐方式 左对齐
     */
    public static String TEXT_LEFT ="text-left";
    /**
     * 文字对齐方式 中间对齐
     */
    public static String TEXT_CENTER ="text-center";
    /**
     * 文字对齐方式 右对齐
     */
    public static String TEXT_RIGHT="text-right";
    /**
     * 搜索组件的样式一
     */
    public static String CUSTOM_SEARCH_STYLE_A = "custom-search-style-A";
    /**
     * 搜索组件的样式二
     */
    public static String CUSTOM_SEARCH_STYLE_B = "custom-search-style-B";
    /**
     * 店铺头部组件 背景类型
     */
    public static int WIDGETBACKTYPE_IMAGE = 0;
    /**
     * 店铺头部组件 背景类型
     */
    public static int WIDGETBACKTYPE_COLOR = 1;
    /**
     * 0 –默认(默认时则根据api获得logo)
     */
    public static int LOGO_0 =0;
    /**
     * 1 -自定义(自定义则不同过api获得logo,使用用户上传的配置信息)
     */
    public static int LOGO_1=1;
    /**
     *  一大两小
     */
    public static String WIDGETLAYOUT_SIZE_2 ="size-2";
    /**
     * 详细列表
     */
    public static String  WIDGETLAYOUT_SIZE_3 ="size-3";

    /**
     * 列表样式: card -卡片样式
     */
    public static String LAYER_STYLE_CARD ="card";
    /**
     * 列表样式:极简样式
     */
    public static String LAYER_STYLE_NORMAL ="normal";
    /**
     * 列表样式: 促销样式
     */
    public static String LAYER_STYLE_PROMOTION ="promotion";
    /**
     * 是否显示商品名
     * show  -显示
     */
    public static String GOODS_SHOW ="show";
    /**
     * 是否显示
     * hide–不显示
     */
    public static String GOODS_HIDE="hide";



    public final static String HEADER_USER_KEY="_user_key";
    public final static String HEADER_USER_RANDOM="_user_random";
    public final static String HEADER_USER_SECURE="_user_secure";

    /**
     * 每页显示的条数
     */
    public final static int PAGESIZE=10;
    /**
     * 请求业务数据接口 成功状态码 200
     */
    public final static int REQUEST_SUCCESS=200;
    /**
     * 返利图标的宽度
     */
    public final static int REBATEICON_WIDTH= 40;
    /**
     * 底部导航栏图标的宽度
     */
    public final static int FOOTER_ICON_WIDTH = 25;
    /**
     * url地址中的参数 占位符
     */
    public final static String URL_PARAMETER_CUSTOMERID="{CustomerID}";
    /**
     * URL地址中的参数 占位符
     */
    public final static String URL_PARAMETER_QQ ="{QQ}";
    /**
     * smart ui 页
     */
    public final static String URL_SMART_ASPX="smart.aspx";
    /**
     * 店铺首页
     */
    public final static String URL_SHOP_INDEX="index.aspx";
    /**
     * 分类页面
     */
    public final static String URL_CLASS_ASPX="mall/list.aspx";
    /**
     * 默认 分类 页面
     */
    public final static String URL_CAT_ASPX="mall/cat.aspx";
    /**
     * 搜索页面
     */
    public final static String URL_SEARCH_ASPX="mall/search.aspx";
    /**
     * 商品分类页面
     */
    public final static int PAGE_TYPE_1=1;
    /**
     * 虚拟分类页面
     */
    public final static int PAGE_TYPE_2=2;
    /**
     * 搜索页面
     */
    public final static int PAGE_TYPE_3=3;
    /**
     * 品牌页面
     */
    public final static int PAGE_TYPE_4=4;
    /**
     * 搜索页面类型
     */
    public final static int TYPE_2=-2;
    /**
     * 商品分类 类型
     */
    public final static int TYPE_1=-1;
    /**
     * 虚拟商品分类 类型
     */
    public final static int TYPE_3=-3;
    /**
     * 品牌类型
     */
    public final static int TYPE_4=-4;


}
