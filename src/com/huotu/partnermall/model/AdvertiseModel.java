package com.huotu.partnermall.model;

import java.io.Serializable;
import java.util.List;

/**
 * 开机广告类
 * Created by Administrator on 2017/1/10.
 */
public class AdvertiseModel extends DataBase implements Serializable {
    private List<Advertise> data;

    public List<Advertise> getData() {
        return data;
    }

    public void setData(List<Advertise> data) {
        this.data = data;
    }


}
