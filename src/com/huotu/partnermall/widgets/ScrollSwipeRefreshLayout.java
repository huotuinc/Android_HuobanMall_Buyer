package com.huotu.partnermall.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * 自定义下拉控件
 */
public class ScrollSwipeRefreshLayout extends SwipeRefreshLayout {

    private ViewGroup viewGroup ;

    public ScrollSwipeRefreshLayout(Context context) {
        super(context);
    }

    public ScrollSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup getViewGroup() {
        return viewGroup;
    }
    public void setViewGroup(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }
}
