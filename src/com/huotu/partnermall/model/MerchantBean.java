package com.huotu.partnermall.model;

import java.util.List;

/**
 * 商户信息bean
 */
public
class MerchantBean extends BaseBean {

    //商家ID
    private String merchantId;
    //商家名称
    private String merchantName;
    //商家支付宝key
    private String alipayKey;
    //商家微信Key
    private String weixinKey;
    //关联的底部菜单
    private
    List<MenuBean> menus;
    //关联的商品类别侧滑
    private List<CatagoryBean> catagorys;

    public
    String getMerchantId ( ) {
        return merchantId;
    }

    public
    void setMerchantId ( String merchantId ) {
        this.merchantId = merchantId;
    }

    public
    String getMerchantName ( ) {
        return merchantName;
    }

    public
    void setMerchantName ( String merchantName ) {
        this.merchantName = merchantName;
    }

    public
    String getAlipayKey ( ) {
        return alipayKey;
    }

    public
    void setAlipayKey ( String alipayKey ) {
        this.alipayKey = alipayKey;
    }

    public
    String getWeixinKey ( ) {
        return weixinKey;
    }

    public
    void setWeixinKey ( String weixinKey ) {
        this.weixinKey = weixinKey;
    }

    public
    List< MenuBean > getMenus ( ) {
        return menus;
    }

    public
    void setMenus ( List< MenuBean > menus ) {
        this.menus = menus;
    }

    public
    List< CatagoryBean > getCatagorys ( ) {
        return catagorys;
    }

    public
    void setCatagorys ( List< CatagoryBean > catagorys ) {
        this.catagorys = catagorys;
    }
}
