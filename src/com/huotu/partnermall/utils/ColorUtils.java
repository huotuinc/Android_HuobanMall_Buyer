package com.huotu.partnermall.utils;

import android.graphics.Color;

/**
 * 颜色计算类
 */
public
class ColorUtils {

    private static class Holder
    {
        private static final ColorUtils instance = new ColorUtils();
    }

    private ColorUtils()
    {
    }

    public static final ColorUtils getInstance()
    {
        return Holder.instance;
    }

    /*public
    Color mergeColor(Color mainColor, Color secondColor)
    {

    }*/
}
