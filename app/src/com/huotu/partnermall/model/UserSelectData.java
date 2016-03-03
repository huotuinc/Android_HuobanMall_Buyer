package com.huotu.partnermall.model;

import java.io.Serializable;

/**
 * 用户设置选择数据
 */
public
class UserSelectData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public UserSelectData(String name, String id){
        this.name = name;
        this.id = id;
    }
    public String name;
    public String id;
}
