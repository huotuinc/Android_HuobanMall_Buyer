package com.huotu.partnermall.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/11.
 */
public class WxPaySuccessCallbackModel implements Serializable{
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public WxPaySuccessCallbackModel(String orderNo){
        this.orderNo= orderNo;
    }
}
