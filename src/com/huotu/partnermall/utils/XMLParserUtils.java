package com.huotu.partnermall.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.MerchantBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采用XmlResourceParser解析xml信息
 */
public
class XMLParserUtils {

    private static class Holder
    {
        private static final XMLParserUtils instance = new XMLParserUtils();
    }

    private XMLParserUtils()
    {

    }

    public static final XMLParserUtils getInstance()
    {
        return Holder.instance;
    }

    public MerchantBean readMerchantInfo(Context context)
    {
        Map<String, MerchantBean> merchantMap = new HashMap< String, MerchantBean > (  );
        MerchantBean merchant = null;

        try {
            XmlResourceParser xmlResourceParser = context.getResources ().getXml ( R.xml.merchant_info );
            merchant = new MerchantBean ();
            //如果没有到文件尾继续执行
            while (xmlResourceParser.getEventType () != XmlResourceParser.END_DOCUMENT) {

                //如果是开始标签
                if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG) {
                    //获取标签名称
                    String name = xmlResourceParser.getName();

                    //判断标签名称是否等于ID
                    if(name.equals( Constants.APP_VERSION)){
                        merchant.setAppVersion ( xmlResourceParser.nextText () );
                    }
                    else if(name.equals ( Constants.APP_NAME ))
                    {
                        merchant.setAppName ( xmlResourceParser.nextText () );
                    }
                    else if(name.equals ( Constants.APP_BUILD ))
                    {
                        merchant.setAppBuild ( xmlResourceParser.nextText () );
                    }
                    else if(name.equals ( Constants.MERCHANT_ID ))
                    {
                        merchant.setMerchantId ( xmlResourceParser.nextText () );
                    }
                    else if(name.equals ( Constants.WEIXIN_MERCHANT_ID ))
                    {
                        merchant.setWeixinMerchantId ( xmlResourceParser.nextText () );
                    }
                    else if(name.equals ( Constants.MERCHANT_WEIXIN_ID ))
                    {
                        merchant.setMerchantWeixinId ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.WEIXIN_KEY ))
                    {
                        merchant.setWeixinKey ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.ALIPAY_MERCHANT_ID ))
                    {
                        merchant.setAlipayMerchantId ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.MERCHANT_ALIPAY_ID ))
                    {
                        merchant.setMerchantAlipayId ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.LOCATION_KEY ))
                    {
                        merchant.setLocationKey ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.ALIPAY_KEY ))
                    {
                        merchant.setAlipayKey ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.U_MENG_KEY ))
                    {
                        merchant.setUmengAppkey ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.U_MENG_CHANNEL))
                    {
                        merchant.setUmengChannel ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.U_MENG_SECRET))
                    {
                        merchant.setUmengMessageSecret ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.PREFIX))
                    {
                        merchant.setHttpPrefix ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.SHARE_KEY))
                    {
                        merchant.setShareSDKKey ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.TENCENT_KEY))
                    {
                        merchant.setTencentKey ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.TENCENT_SECRET))
                    {
                        merchant.setTencentSecret ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.SINA_KEY))
                    {
                        merchant.setSinaKey ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.SINA_SECRET))
                    {
                        merchant.setSinaSecret ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.SINA_REDIRECT_URI))
                    {
                        merchant.setSinaRedirectUri ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.WEIXIN_SHARE_key))
                    {
                        merchant.setWeixinShareKey ( xmlResourceParser.nextText ( ) );
                    }
                    else if(name.equals ( Constants.PUSH_KEY))
                    {
                        merchant.setPushKey ( xmlResourceParser.nextText ( ) );
                    }
                } else if (xmlResourceParser.getEventType() == XmlPullParser.END_TAG) {
                }
                //下一个标签
                xmlResourceParser.next ( );
            }
            return merchant;
        } catch (XmlPullParserException e) {
            KJLoger.exception ( e );
            return null;
        } catch (IOException e) {
            KJLoger.exception ( e );
            return null;
        }

    }


    /***
     * 读取菜单信息
     * @param context
     * @return
     */
    public List<MenuBean> readMenuInfo(Context context)
    {
        List<MenuBean> menus = null;
        MenuBean menu = null;

        try {
            XmlResourceParser xmlResourceParser = context.getResources ().getXml ( R.xml.main_menu );
            menus = new ArrayList< MenuBean > (  );
            //如果没有到文件尾继续执行
            while (xmlResourceParser.getEventType () != XmlResourceParser.END_DOCUMENT) {

                //如果是开始标签
                if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG) {
                    //获取标签名称
                    String name = xmlResourceParser.getName();

                     if(name.equals ( Constants.HOME_MENU ))
                    {
                        menu = new MenuBean ();
                    }
                    if(null != menu)
                    {
                        if(name.equals ( Constants.MENU_NAME ))
                        {
                            String menuName = xmlResourceParser.nextText ();
                            menu.setMenuName ( menuName );
                        }
                        else if(name.equals ( Constants.MENU_TAG ))
                        {
                            String menuTag = xmlResourceParser.nextText ();
                            menu.setMenuTag (  menuTag );
                        }
                        else if(name.equals ( Constants.MENU_ICON ))
                        {
                            String menuIcon = xmlResourceParser.nextText ();
                            menu.setMenuIcon ( menuIcon );
                        }
                        else if(name.equals ( Constants.MENU_URL ))
                        {
                            String menuUrl = xmlResourceParser.nextText ();
                            menu.setMenuUrl ( menuUrl );
                        }
                        else if(name.equals ( Constants.MENU_GROUP ))
                        {
                            String menuGroup = xmlResourceParser.nextText ();
                            menu.setMenuGroup ( menuGroup );
                        }
                    }

                } else if (xmlResourceParser.getEventType() == XmlPullParser.END_TAG) {
                    String endName = xmlResourceParser.getName ();
                    if(endName.equals ( Constants.HOME_MENU ))
                    {
                        menus.add ( menu );
                        menu = null;
                    }
                }
                //下一个标签
                xmlResourceParser.next ( );
            }
            return menus;
        } catch (XmlPullParserException e) {
            KJLoger.exception ( e );
            return null;
        } catch (IOException e) {
            KJLoger.exception ( e );
            return null;
        }

    }

}
