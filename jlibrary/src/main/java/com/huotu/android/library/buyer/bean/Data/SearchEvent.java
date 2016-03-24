package com.huotu.android.library.buyer.bean.Data;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SearchEvent {
    private String keyword;
    private String configUrl;

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SearchEvent(String configUrl ,String keyword){
        this.keyword = keyword;
        this.configUrl= configUrl;
    }
}
