package com.huotu.android.library.buyer.bean.Data;

/**
 * Created by Administrator on 2016/3/21.
 */
public class SmartUiEvent {
    private String configUrl;
    private boolean ismainUi;
    public SmartUiEvent(String configUrl , boolean ismainUi){
        this.configUrl = configUrl;
        this.ismainUi = ismainUi;
    }

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }

    public boolean ismainUi() {
        return ismainUi;
    }

    public void setIsmainUi(boolean ismainUi) {
        this.ismainUi = ismainUi;
    }
}
