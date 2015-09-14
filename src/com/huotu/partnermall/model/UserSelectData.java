package com.huotu.partnermall.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/11.
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
