package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2015/10/13.
 */
public
class PayModel extends BaseBean {

    private String customId;
    private String tradeNo;

    public
    String getTradeNo ( ) {
        return tradeNo;
    }

    public
    void setTradeNo ( String tradeNo ) {
        this.tradeNo = tradeNo;
    }

    public
    String getCustomId ( ) {
        return customId;
    }

    public
    void setCustomId ( String customId ) {
        this.customId = customId;
    }
}
