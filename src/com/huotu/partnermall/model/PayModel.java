package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2015/10/13.
 */
public
class PayModel extends BaseBean {

    private String customId;
    private String tradeNo;
    private String paymentType;
    private int amount;
    private String detail;
    private String notifyurl;

    public
    String getDetail ( ) {
        return detail;
    }

    public
    String getNotifyurl ( ) {
        return notifyurl;
    }

    public
    void setNotifyurl ( String notifyurl ) {
        this.notifyurl = notifyurl;
    }

    public
    void setDetail ( String detail ) {
        this.detail = detail;
    }

    public
    int getAmount ( ) {
        return amount;
    }

    public
    void setAmount ( int amount ) {
        this.amount = amount;
    }

    public
    String getPaymentType ( ) {
        return paymentType;
    }

    public
    void setPaymentType ( String paymentType ) {
        this.paymentType = paymentType;
    }

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
