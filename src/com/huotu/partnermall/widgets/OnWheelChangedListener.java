package com.huotu.partnermall.widgets;

/**
 * Created by Administrator on 2015/9/11.
 */
public
interface OnWheelChangedListener {

    /**
     * Callback method to be invoked when current item changed
     * @param wheel the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    void onChanged(TimeView wheel, int oldValue, int newValue);
}
