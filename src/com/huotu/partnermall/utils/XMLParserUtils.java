package com.huotu.partnermall.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.CatagoryBean;
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
        List<CatagoryBean> catagorys = null;
        CatagoryBean catagory = null;
        List<MenuBean> menus = null;
        MenuBean menu = null;

        try {
            XmlResourceParser xmlResourceParser = context.getResources ().getXml ( R.xml.merchant_info );
            //如果没有到文件尾继续执行
            while (xmlResourceParser.getEventType () != XmlResourceParser.END_DOCUMENT) {
                merchant = new MerchantBean ();
                catagorys = new ArrayList< CatagoryBean > (  );
                menus = new ArrayList< MenuBean > (  );
                //如果是开始标签
                if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG) {

                    //获取标签名称
                    String name = xmlResourceParser.getName();
                    //判断标签名称是否等于ID
                    if(name.equals( Constants.MERCHANT)){
                        //记录商户ID
                        String merchantId = xmlResourceParser.getAttributeValue ( null, Constants.MERCHANT_ID );
                        merchant.setMerchantId ( merchantId );
                        String alipayKey = xmlResourceParser.getAttributeValue ( null, Constants.ALIPAY_KEY );
                        merchant.setAlipayKey ( alipayKey );
                        String weixinKey = xmlResourceParser.getAttributeValue ( null, Constants.WEIXIN_KEY );
                        merchant.setWeixinKey ( weixinKey );
                    }
                    else if(name.equals ( Constants.HOME_MENU ))
                    {
                        menu = new MenuBean ();
                        String menuName = xmlResourceParser.getAttributeValue ( null,  Constants.MENU_NAME);
                        menu.setMenuName ( menuName );
                        String menuIcon = xmlResourceParser.getAttributeValue ( null, Constants.MENU_ICON );
                        menu.setMenuIcon ( menuIcon );
                        menus.add ( menu );
                    }
                    else if(name.equals ( Constants.CATAGORY_MENU ))
                    {
                        catagory = new CatagoryBean ();
                        String catagoryName = xmlResourceParser.getAttributeValue ( null, Constants.CATAGORY_TYPE );
                        catagory.setCatagoryName ( catagoryName );
                        catagorys.add ( catagory );
                    }

                } else if (xmlResourceParser.getEventType() == XmlPullParser.END_TAG) {
                    if(null != menu || null != catagory)
                    {
                        menu = null;
                        catagory = null;
                    }
                }
                //下一个标签
                xmlResourceParser.next();
            }
            merchant.setCatagorys ( catagorys );
            merchant.setMenus ( menus );
            return merchant;
        } catch (XmlPullParserException e) {
            KJLoger.exception ( e );
            return null;
        } catch (IOException e) {
            KJLoger.exception ( e );
            return null;
        }

    }

}
