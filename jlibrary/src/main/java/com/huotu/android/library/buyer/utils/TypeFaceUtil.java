package com.huotu.android.library.buyer.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Administrator on 2016/1/26.
 */
public class TypeFaceUtil {
    public  static Typeface typeface;

    public static Typeface FONTAWEOME(Context context){
        if( typeface == null){
            typeface = Typeface.createFromAsset( context.getAssets() , "fontawesome-webfont.ttf" );
        }
        return typeface;
    }
}
