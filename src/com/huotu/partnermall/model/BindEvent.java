package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2016/6/13.
 */
public class BindEvent {
    private boolean isBindWeiXin=false;
    private String redirectUrl = "";

    public BindEvent(boolean isBindWeiXin , String redirectUrl ){
        this.isBindWeiXin=isBindWeiXin;
        this.redirectUrl = redirectUrl;
    }

    public boolean isBindWeiXin() {
        return isBindWeiXin;
    }

    public void setBindWeiXin(boolean bindWeiXin) {
        isBindWeiXin = bindWeiXin;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
