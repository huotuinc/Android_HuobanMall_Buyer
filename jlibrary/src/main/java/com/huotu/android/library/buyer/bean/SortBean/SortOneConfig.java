package com.huotu.android.library.buyer.bean.SortBean;


import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * Created by Administrator on 2016/1/21.
 */
public class SortOneConfig extends BaseConfig {
     /**
     * 是否启用筛选
     */
    private boolean filterRule=false;

    public boolean isFilterRule() {
        return filterRule;
    }

    public void setFilterRule(boolean filterRule) {
        this.filterRule = filterRule;
    }
}
