package com.huotu.android.library.buyer;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by jinxiangdong on 2016/1/26.
 */
public class Jlibrary {
    public  static void init(Context context){
        Fresco.initialize(context);
    }
}
