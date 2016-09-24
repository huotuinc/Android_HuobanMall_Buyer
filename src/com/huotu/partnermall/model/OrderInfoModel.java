package com.huotu.partnermall.model;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 */

public class OrderInfoModel extends DataBase {

    private Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
