package com.huotu.partnermall.widgets.custom;

/**
 * Created by Administrator on 2016/2/24.
 */
public class BizBean {
    private int resultCode;
    private String resultMsg;
    private MallInfoBean data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public MallInfoBean getData() {
        return data;
    }

    public void setData(MallInfoBean data) {
        this.data = data;
    }
}
