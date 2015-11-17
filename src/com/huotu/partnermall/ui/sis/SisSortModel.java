package com.huotu.partnermall.ui.sis;

import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */
public class SisSortModel {
    private Long id;
    private String name;
    private List<SisSortModel> list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SisSortModel> getList() {
        return list;
    }

    public void setList(List<SisSortModel> list) {
        this.list = list;
    }

}
