package com.huotu.android.library.buyer.bean.Data;

/**
 * Created by Administrator on 2016/2/20.
 */
public class ScrollEvent {
    private int scrollY;
    private int scrollX;

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public int getScrollX() {
        return scrollX;
    }

    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
    }

    public ScrollEvent(int scrollX, int scrollY){
        this.scrollX= scrollX;
        this.scrollY = scrollY;
    }
}
