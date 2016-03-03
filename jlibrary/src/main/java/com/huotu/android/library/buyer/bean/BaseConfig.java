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

//    private int leftMargion;
//    private int rightMargion;
//    private int topMargion;
//    private int bottomMargion;

//    public int getLeftMargion() {
//        return leftMargion;
//    }
//
//    public void setLeftMargion(int leftMargion) {
//        this.leftMargion = leftMargion;
//    }
//
//    public int getRightMargion() {
//        return rightMargion;
//    }
//
//    public void setRightMargion(int rightMargion) {
//        this.rightMargion = rightMargion;
//    }
//
//    public int getTopMargion() {
//        return topMargion;
//    }
//
//    public void setTopMargion(int topMargion) {
//        this.topMargion = topMargion;
//    }
//
//    public int getBottomMargion() {
//        return bottomMargion;
//    }
//
//    public void setBottomMargion(int bottomMargion) {
//        this.bottomMargion = bottomMargion;
//    }
//

    public boolean isStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

}
