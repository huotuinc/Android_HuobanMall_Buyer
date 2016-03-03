package com.huotu.partnermall.ui.sis;

import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */
public class SisSortModel {
    private Long sisId;

    public Long getSisId() {
        return sisId;
    }

    public void setSisId(Long sisId) {
        this.sisId = sisId;
    }

    public String getSisName() {
        return sisName;
    }

    public void setSisName(String sisName) {
        this.sisName = sisName;
    }

    private String sisName;
    private List<SisSortModel> list;

    private boolean isFirstClass = false;

    public boolean isFirstClass() {
        return isFirstClass;
    }

    public void setIsFirstClass(boolean isFirstClass) {
        this.isFirstClass = isFirstClass;
    }

    public List<SisSortModel> getList() {
        return list;
    }

    public void setList(List<SisSortModel> list) {
        this.list = list;
    }

}
