package com.huotu.partnermall.model;

/**
 * 订单详情
 */
public
class OrderModel {

    private int code;
    private String msg;
    private OrderData data;

    public
    int getCode ( ) {
        return code;
    }

    public
    void setCode ( int code ) {
        this.code = code;
    }

    public
    String getMsg ( ) {
        return msg;
    }

    public
    void setMsg ( String msg ) {
        this.msg = msg;
    }

    public
    OrderData getData ( ) {
        return data;
    }

    public
    void setData ( OrderData data ) {
        this.data = data;
    }

    public class OrderData
    {
        private int Final_Amount;
        private String Tostr;

        public
        String getTostr ( ) {
            return Tostr;
        }

        public
        void setTostr ( String tostr ) {
            Tostr = tostr;
        }

        public
        int getFinal_Amount ( ) {
            return Final_Amount;
        }

        public
        void setFinal_Amount ( int final_Amount ) {
            Final_Amount = final_Amount;
        }
    }
}
