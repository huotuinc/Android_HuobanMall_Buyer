package com.huotu.android.library.buyer.bean.Data;

/**
 * Created by Administrator on 2016/4/15.
 */
public class FooterEvent {
    private boolean isShow=true;
    public FooterEvent(boolean isShow){
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
