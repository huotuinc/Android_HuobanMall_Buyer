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

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(null!=viewGroup){
            if(viewGroup instanceof KJWebView)
            {

                if(((KJWebView)viewGroup).getWebScrollY() == 0){

                    return super.onTouchEvent(ev);
                }else{
                    //直接截断时间传播
                    return true;
                }
            }
            else
            {
                return super.onTouchEvent(ev);
            }

        }
        return super.onTouchEvent(ev);
    }
}
