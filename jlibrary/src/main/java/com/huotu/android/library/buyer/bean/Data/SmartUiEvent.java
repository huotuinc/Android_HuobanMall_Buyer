package com.huotu.android.library.buyer.bean.Data;

/**
 * Created by Administrator on 2016/3/21.
 */
public class SmartUiEvent {
    private String configUrl;
    public SmartUiEvent(String configUrl){
        this.configUrl = configUrl;
    }

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }
}
