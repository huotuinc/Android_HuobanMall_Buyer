package com.huotu.partnermall.widgets;

/**
 * Created by Administrator on 2015/9/11.
 */
public
interface OnWheelClickedListener {

    /**
     * Callback method to be invoked when current item clicked
     * @param wheel the wheel view
     * @param itemIndex the index of clicked item
     */
    void onItemClicked(TimeView wheel, int itemIndex);
}
