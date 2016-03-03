package com.huotu.android.library.buyer.utils;

import android.util.Log;
import com.huotu.android.library.buyer.BuildConfig;
/**
 * Created by Administrator on 2016/1/6.
 */
public class Logger {
    private static String TAG="debug";

    public static void i(String msg){
        if(BuildConfig.DEBUG){
            Log.i(TAG, msg);
        }
    }
    public static void e(String msg){
        if(BuildConfig.DEBUG){
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg , Throwable t){
        if( BuildConfig.DEBUG){
            Log.e(TAG,msg,t);
        }
    }

    public static void w(String msg){
        if( BuildConfig.DEBUG){
            Log.w(TAG, msg);
        }
    }
    public static void w(String msg , Throwable t){
        if( BuildConfig.DEBUG){
            Log.w(TAG,msg,t);
        }
    }
}
