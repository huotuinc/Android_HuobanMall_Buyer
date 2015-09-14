package com.huotu.partnermall.widgets;

/**
 * Created by Administrator on 2015/9/11.
 */
public
interface OnWheelScrollListener {

    /**
     * Callback method to be invoked when scrolling started.
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingStarted(TimeView wheel);

    /**
     * Callback method to be invoked when scrolling ended.
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingFinished(TimeView wheel);
}
