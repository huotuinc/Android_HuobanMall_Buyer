package com.huotu.android.library.buyer.bean.AdBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

/**
 * Created by Administrator on 2016/1/28.
 */
public class AdBaseConfig extends BaseConfig {
    private int leftDistance;
    private int rightDistance;
    private int topDistance;
    private int bottomDistance;

    public int getLeftDistance() {
        return leftDistance;
    }

    public void setLeftDistance(int leftDistance) {
        this.leftDistance = leftDistance;
    }

    public int getRightDistance() {
        return rightDistance;
    }

    public void setRightDistance(int rightDistance) {
        this.rightDistance = rightDistance;
    }

    public int getTopDistance() {
        return topDistance;
    }

    public void setTopDistance(int topDistance) {
        this.topDistance = topDistance;
    }

    public int getBottomDistance() {
        return bottomDistance;
    }

    public void setBottomDistance(int bottomDistance) {
        this.bottomDistance = bottomDistance;
    }
}
