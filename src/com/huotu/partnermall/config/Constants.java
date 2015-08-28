package com.huotu.partnermall.config;

import android.os.Environment;

public class Constants {
	/**
	 ******************************************* 参数设置信息开始 ******************************************
	 */

	// 应用名称
	public static String APP_NAME = "";

	// 保存参数文件夹名字
	public static final String SHARED_PREFERENCE_NAME = "account_info";

	// SDCard路径
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

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

	/**
	 ******************************************* 参数设置信息结束 ******************************************
	 */

	//标准时间
	public final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	//标准时间01
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * ************************************商户信息xml节点***********************
	 */
	//商户信息
	public static final String MERCHANT = "MERCHANT";
	//商户ID
	public static final String MERCHANT_ID = "id";
	//商户支付宝KEY信息
	public static final String ALIPAY_KEY = "alipaykey";
	//商户微信支付KEY信息
	public static final String WEIXIN_KEY = "weixinkey";
	//主菜单
	public static final String HOME_MENU = "HOME_MENU";
	//主菜单名称
	public static final String MENU_NAME = "menuname";
	//主菜单图标
	public static final String MENU_ICON = "menuicon";
	//类别侧滑菜单
	public static final String CATAGORY_MENU = "CATAGORY_MENU";
	public static final String CATAGORY_TYPE = "catagoryname";

	/**
	 * *******************preference参数设置
	 */
	//商户信息
	public static final String MERCHANT_INFO = "merchant_info";
	//商户ID
	public static final String MERCHANT_INFO_ID = "merchant_id";
	//商户支付宝key信息
	public static final String MERCHANT_INFO_ALIPAY_KEY = "merchant_alipay_key";
	//商户微信支付KEY信息
	public static final String MERCHANT_INFO_WEIXIN_KEY = "merchant_weixin_key";

}
