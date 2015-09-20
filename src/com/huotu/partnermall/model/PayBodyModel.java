package com.huotu.partnermall.model;

/**
 * 支付需要的参数[body]实体
 */
public
class PayBodyModel extends BaseBean {

    //主题{支付宝专用}
    private String subject;
    //价格
    private String price;
    //内容
    private String body;
    //商品类型
    private int productType;
    //商品编号
    private long productId;
    //回调消息url{支付宝专用}
    private String notifyurl;

    public
    String getSubject ( ) {
        return subject;
    }

    public
    void setSubject ( String subject ) {
        this.subject = subject;
    }

    public
    String getPrice ( ) {
        return price;
    }

    public
    void setPrice ( String price ) {
        this.price = price;
    }

    public
    String getBody ( ) {
        return body;
    }

    public
    void setBody ( String body ) {
        this.body = body;
    }

    public
    int getProductType ( ) {
        return productType;
    }

    public
    void setProductType ( int productType ) {
        this.productType = productType;
    }

    public
    long getProductId ( ) {
        return productId;
    }

    public
    void setProductId ( long productId ) {
        this.productId = productId;
    }

    public
    String getNotifyurl ( ) {
        return notifyurl;
    }

    public
    void setNotifyurl ( String notifyurl ) {
        this.notifyurl = notifyurl;
    }
}
