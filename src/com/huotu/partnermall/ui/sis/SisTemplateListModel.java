package com.huotu.partnermall.ui.sis;

import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class SisTemplateListModel extends BaseModel{
    Long id;
    String pictureUrl;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    List<String> list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
