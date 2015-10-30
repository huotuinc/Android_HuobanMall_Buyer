package com.huotu.partnermall.config;

import android.os.Environment;

public class Constants {
	/**
	 ******************************************* 参数设置信息开始
	 * ******************************************
	 */

	// 保存参数文件夹名字
	public static final String SHARED_PREFERENCE_NAME = "account_info";

	// SDCard路径
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory ( ).getAbsolutePath ( );

	// 手机IMEI号码
	public static String IMEI = "";

	// 手机号码
	public static String TEL = "";

	// 屏幕高度
	public static int SCREEN_HEIGHT = 800;

	// 屏幕宽度
	public static int SCREEN_WIDTH = 480;

	// 屏幕密度
	public static float SCREEN_DENSITY = 1.5f;

	// 分享成功
	public static final int SHARE_SUCCESS = 0X1000;

	// 分享取消
	public static final int SHARE_CANCEL = 0X2000;

	// 分享失败
	public static final int SHARE_ERROR = 0X3000;

	// 开始执行
	public static final int EXECUTE_LOADING = 0X4000;

	// 正在执行
	public static final int EXECUTE_SUCCESS = 0X5000;

	// 执行完成
	public static final int EXECUTE_FAILED = 0X6000;

	// 加载数据成功
	public static final int LOAD_DATA_SUCCESS = 0X7000;

	// 加载数据失败
	public static final int LOAD_DATA_ERROR = 0X8000;

	// 动态加载数据
	public static final int SET_DATA = 0X9000;

	// 未登陆
	public static final int NONE_LOGIN = 0X10000;

	//左侧划状态
	public static final int    LEFT_IMG_SIDE = 0X33310;
	// 返回状态
	public static final int    LEFT_IMG_BACK = 0X33311;
	/**
	 ******************************************* 参数设置信息结束
	 * ******************************************
	 */

	//标准时间
	public final static String TIME_FORMAT   = "yyyy-MM-dd HH:mm:ss";
	//标准时间01
	public static final String DATE_FORMAT   = "yyyy-MM-dd";

	/**
	 * ************************************商户信息xml节点***********************
	 */
	//app信息
	public static final String APP_INFO    = "appinfo";
	//app版本号
	public static final String APP_VERSION = "app_version";
	//app名称
	public static final String APP_NAME    = "app_name";
	//app_build
	public static final String APP_BUILD   = "app_build";

	//系统指定包名
	public static final String SYS_PACKAGE_INI = "sys_package";
	//系统菜单
	public static final String SYS_MENU        = "sys_menu";


	//商户信息
	public static final String MERCHANT    = "MERCHANT";
	//商户ID
	public static final String MERCHANT_ID = "app_merchant_id";

	//微信支付信息
	public static final String WEIXIN_PAY         = "weixinpay";
	//微信商家编号
	public static final String WEIXIN_MERCHANT_ID = "weixin_merchant_id";
	//商家微信编号
	public static final String MERCHANT_WEIXIN_ID = "merchant_weixin_id";
	//商户微信支付KEY信息
	public static final String WEIXIN_KEY         = "weixin_key";
	//支付宝商家编号
	public static final String ALIPAY_MERCHANT_ID = "alipay_merchant_id";
	//商家支付宝编号
	public static final String MERCHANT_ALIPAY_ID = "merchant_alipay_id";
	//商户支付宝KEY信息
	public static final String ALIPAY_KEY         = "alipay_key";
	//商户logo
	public static final String MERCHANT_LOGO      = "merchant_logo";
	//商户名称
	public static final String MERCHANT_NAME      = "merchant_name";

	//UMENG统计信息
	public static final String U_MENG            = "umeng";
	//UMENG app key
	public static final String U_MENG_KEY        = "umeng_appkey";
	//umeng_channel
	public static final String U_MENG_CHANNEL    = "umeng_channel";
	//umeng_message_secret
	public static final String U_MENG_SECRET     = "umeng_message_secret";
	//网络请求
	public static final String HTTP_PREFIX       = "httpprefix";
	//网络请求前缀
	public static final String PREFIX            = "prefix";
	//分享
	public static final String SHARE_INFO        = "shareinfo";
	//share KEY
	public static final String SHARE_KEY         = "share_key";
	//tencent_key
	public static final String TENCENT_KEY       = "tencent_key";
	//tencent_secret
	public static final String TENCENT_SECRET    = "tencent_secret";
	//sina_key
	public static final String SINA_KEY          = "sina_key";
	//sina_secret
	public static final String SINA_SECRET       = "sina_secret";
	//sina_redirect_uri
	public static final String SINA_REDIRECT_URI = "sina_redirect_uri";
	//weixin SHARE key
	public static final String WEIXIN_SHARE_key  = "weixin_share_key";
	//推送信息
	public static final String PUSH_INFO         = "push_info";
	//推送key
	public static final String PUSH_KEY          = "push_key";

	//定位key
	public static final String LOCATION_KEY        = "location_key";
	//微信分享加密
	public static final String WEIXIN_SHARE_SECRET = "weixin_share_secret";


	//主菜单
	public static final String HOME_MENU  = "home_menu";
	//主菜单名称
	public static final String MENU_NAME  = "menu_name";
	//主菜单标识
	public static final String MENU_TAG   = "menu_tag";
	//主菜单图标
	public static final String MENU_ICON  = "menu_icon";
	//主菜单url
	public static final String MENU_URL   = "menu_url";
	//主菜单分组
	public static final String MENU_GROUP = "menu_group";

	/**
	 * *******************preference参数设置
	 */
	//商户信息
	public static final String MERCHANT_INFO            = "merchant_info";
	//会员信息
	public static final String MEMBER_INFO              = "member_info";
	//会员等级
	public static final String MEMBER_level             = "member_level";
	//会员名称
	public static final String MEMBER_NAME              = "member_name";
	//会员ID
	public static final String MEMBER_ID                = "member_id";
	//会员token
	public static final String MEMBER_UNIONID           = "member_unionid";
	//会员token
	public static final String MEMBER_TOKEN             = "member_token";
	//会员icon
	public static final String MEMBER_ICON              = "member_icon";
	//商户ID
	public static final String MERCHANT_INFO_ID         = "merchant_id";
	//商户支付宝key信息
	public static final String MERCHANT_INFO_ALIPAY_KEY = "merchant_alipay_key";
	//商户微信支付KEY信息
	public static final String MERCHANT_INFO_WEIXIN_KEY = "merchant_weixin_key";
	//商户菜单
	public static final String MERCHANT_INFO_MENUS      = "main_menus";
	//商户分类菜单
	public static final String MERCHANT_INFO_CATAGORY   = "catagory_menus";

	/**
	 * ************************定位信息设置
	 */
	public static final String LOCATION_INFO = "location_info";
	public static final String ADDRESS       = "address";
	public static final String LATITUDE      = "latitude";
	public static final String LONGITUDE     = "Longitude";
	public static final String CITY          = "city";

	/**
	 * 底部Tab菜单
	 */
	public static final String TAB_1 = "TAB_1";
	public static final String TAB_2 = "TAB_2";
	public static final String TAB_3 = "TAB_3";
	public static final String TAB_4 = "TAB_4";
	public static final String TAB_5 = "TAB_5";
	public static final String TAB_6 = "TAB_6";

	//http请求参数
	//获取具体页面的商品类别
	public static final String HTTP_OBTAIN_CATATORY = "/goods/obtainCatagory?";
	//获取商品信息
	public static final String HTTP_OBTAIN_GOODS    = "/goods/obtainGoods?";
	//new view
	public static final String WEB_TAG_NEWFRAME     = "__newframe";
	//上传图片
	public static final String WEB_TAG_COMMITIMG    = "partnermall520://pickimage";
	//登出
	public static final String WEB_TAG_LOGOUT       = "partnermall520://logout";
	//信息保护
	public static final String WEB_TAG_INFO         = "partnermall520://togglepb";
	//关闭当前页
	public static final String WEB_TAG_FINISH       = "partnermall520://closepage";
	//share
	public static final String WEB_TAG_SHARE        = "partnermall520://shareweb";
	//弹出框
	public static final String WEB_TAG_ALERT        = "partnermall520://alert";
	//支付
	public static final String WEB_TAG_PAYMENT      = "partnermall520://payment";
	//用户信息修改
	public static final String WEB_TAG_USERINFO     = "partnermall520://userinfo?";
	//联系客服
	public static final String WEB_CONTACT          = "mqqwpa://im/chat";

	//支付
	public static final String WEB_PAY      = "/Mall/AppAlipay.aspx";
	//鉴权失效
	public static final String AUTH_FAILURE = "/UserCenter/Login.aspx";

	//鉴权失效
	public static final int LOGIN_AUTH_ERROR = 2131;
	//网页支付
	public static final int PAY_NET = 2222;

	//是否测试环境
	public static final boolean IS_TEST = true;

	//handler code
	//1、success
	public static final int SUCCESS_CODE = 0;
	//2、error code
	public static final int ERROR_CODE   = 1;
	//3、null code
	public static final int NULL_CODE    = 2;

	//微信登录:用户存在
	public static final int MSG_USERID_FOUND    = 1;
	//微信登录：用户不存在
	public static final int MSG_USERID_NO_FOUND = 0;
	public static final int MSG_LOGIN           = 2;
	public static final int MSG_AUTH_CANCEL     = 3;
	public static final int MSG_AUTH_ERROR      = 4;
	public static final int MSG_AUTH_COMPLETE   = 5;

	public static final String INTENT_URL   = "INTENT_URL";
	public static final String INTENT_TITLE = "INTENT_TITLE";

	/**
	 * 修改密码
	 */
	public static final String MODIFY_PSW = "modifyPsw";

	/**
	 * 侧滑菜单加载页面
	 */
	public static final int LOAD_PAGE_MESSAGE_TAG = 4381;

	/**
	 * 关闭载入用户数据
	 */
	public static final int LOAD_SWITCH_USER_OVER = 8888;

	/**
	 * tile栏刷新页面
	 */
	public static final int FRESHEN_PAGE_MESSAGE_TAG = 4380;

	/**
	 * 初始化菜单失败
	 */
	public static final int INIT_MENU_ERROR = 6361;

	/**
	 * 切换用户
	 */
	public static final int SWITCH_USER_NOTIFY = 9988;

	/**
	 * 首页Url
	 */
	public static final String HOME_PAGE_URL = "http://www.baidu.com";

	/**
	 * 基本信息
	 */
	public static final String BASE_INFO   = "base_ifo";
	/**
	 * 当前加载的页面
	 */
	public static final String CURRENT_URL = "current_url";

	//页面的类型
	/**
	 * 设置界面
	 */
	public static final String PAGE_TYPE_SETTING = "setting";

	/**
	 * 微信支付appID
	 */
	public static final String WXPAY_ID    = "wx2f2604e380cf6be1";
	public static final String WXPAY_SECRT = "ae3a7d851f24bfc97047954fa3975cec";
	public static final String PAY_URL     = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	// 商户PID
	public static final String PARTNER      = "";
	// 商户收款账号
	public static final String SELLER       = "";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE  = "";
	// 支付宝公钥
	public static final String RSA_PUBLIC   = "";
	public static final int    SDK_PAY_FLAG = 1;

	public static final int SDK_CHECK_FLAG = 2;

	// API密钥，在商户平台设置(微信支付商户)
	public static final String wxpayApikey    = "0db0d4908a6ae6a09b0a7727878f0ca6";
	//微信parterKey
	public static final String wxpayParterkey = "1251040401";

	//微信支付
	public static final String WX_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";


	/**
	 * 操作平台码
	 */
	public static final String OPERATION_CODE = "BUYER_2015DC";

	public static final String APPKEY = "b73ca64567fb49ee963477263283a1bf";

	/**
	 * capCode
	 */
	public static final String CAP_CODE = "default";

	public final static int ANIMATION_COUNT = 1000;

	/**
	 * app系统配置
	 */
	public static final String SYS_INFO    = "sysInfo";
	public static final String SYS_PACKAGE = "sysPackage";
	public static final String FIRST_OPEN  = "firstOpen";

	/**
	 * app颜色配置
	 */
	public static final String COLOR_INFO   = "colorInfo";
	public static final String COLOR_MAIN   = "mainColor";
	public static final String COLOR_SECOND = "secondColor";

	public static final String CUSTOMER_ID = "customerid={}";
	public static final String USER_ID     = "userid={}";

	//测试
	//public static final String APP_ID = "huotu686ad1fe2bd8a02bb";
	//正式
	public static final String APP_ID = "huotuacf89c9231848c9f49";

	//接口连接前缀
	//测试
	//public static final String INTERFACE_PREFIX = "http://mallapi.huobanj.cn/";
	//正式
	public static final String INTERFACE_PREFIX = "http://mallapi.huobanmall.com/";
	//接口连接前缀
	//public static final String INTERFACE_PREFIX = "http://192.168.1.56:8089/";

	// 平台安全码
	//测试
	//public static final String APP_SECRET = "483686ad1fe2bd8a02bbdca24e109953a4a96c";
	//正式
	public static final String APP_SECRET = "0ad8abe244331aacf89c9231848c9f49";


	public static final String ALIPAY_NOTIFY = "alipay_notify";
	public static final String WEIXIN_NOTIFY = "weixin_notify";
	public static final String IS_WEB_WEIXINPAY = "is_web_weixinpay";
	public static final String IS_WEB_ALIPAY = "is_web_alipay";

	public static final String COMMON_SHARE_LOGO = "http://1804.img.pp.sohu.com.cn/images/2013/1/14/16/2/6205e011f029437o_13cfbf362e6g85.jpg";

	//数据包版本号
	public static final String DATA_INIT            = "data_init";
	//会员信息
	public static final String PACKAGE_VERSION              = "package_version";
}
