package com.huotu.android.library.buyer.bean;

import java.io.Serializable;

/**
 *
 * Created by jinxiangdong on 2016/1/6.
 */
public class BaseConfig implements Serializable {
    /**
     * 是否是静态
     *
     */
    private boolean isStatic = true;

    public boolean isStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

}
