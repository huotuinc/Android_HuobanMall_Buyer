package com.huotu.partnermall.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/9/20.
 */
public
class DateNumericAdapter extends NumericWheelAdapter {

    // Index of current item
    int currentItem;

    // Index of item to be highlighted
    int currentValue;

    public
    DateNumericAdapter ( Context context ) {
        super ( context );
    }

    public
    DateNumericAdapter ( Context context, int minValue, int maxValue ) {
        super ( context, minValue, maxValue );
    }

    public
    DateNumericAdapter ( Context context, int minValue, int maxValue, String format ) {
        super ( context, minValue, maxValue, format );
    }

    public DateNumericAdapter(Context context, int minValue, int maxValue,
                              int current)
    {
        super(context, minValue, maxValue);
        this.currentValue = current;
        setTextSize(22);
    }

    @Override
    protected void configureTextView(TextView view)
    {
        super.configureTextView(view);
        if (currentItem == currentValue)
        {
            view.setTextColor(0xFF0000F0);
        }
        view.setTypeface(Typeface.SANS_SERIF);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent)
    {
        currentItem = index;
        return super.getItem(index, cachedView, parent);
    }
}
