package com.huotu.partnermall.utils;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huotu.partnermall.image.VolleyUtil;

/**
 * 简化volley请求的操作
 */
public
class VolleyHttpUtil {

    private String url;
    private Activity context;
    private
    Response.Listener listener;
    private Response.ErrorListener errorListener;
    private
    int methodType;

    public VolleyHttpUtil(String url, Activity context, Response.Listener listener, Response.ErrorListener errorListener, int methodType)
    {
        this.url = url;
        this.context = context;
        this.listener = listener;
        this.errorListener = errorListener;
        this.methodType = methodType;
    }

    public void doHttp()
    {
        if(Request.Method.GET == methodType)
        {
            VolleyUtil.getRequestQueue ().add ( new JsonObjectRequest (Request.Method.GET, url, null, listener, errorListener){
                                                });
        }
        else if(Request.Method.POST == methodType)
        {
            VolleyUtil.getRequestQueue ().add ( new JsonObjectRequest (Request.Method.POST, url, null, listener, errorListener){
                                                });
        }
    }
}
