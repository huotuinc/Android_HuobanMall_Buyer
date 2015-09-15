package com.huotu.partnermall.utils;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huotu.partnermall.image.VolleyUtil;

/**
 * 简化volley请求的操作
 */
public
class VolleyHttpUtil {

    private String url;
    private Activity context;
    private JsonObjectRequest request;
    private
    RetryPolicy mRetryPolicy;

    public VolleyHttpUtil(String url, Activity context, JsonObjectRequest request, RetryPolicy mRetryPolicy)
    {
        this.url = url;
        this.context = context;
        this.request = request;
        this.mRetryPolicy = mRetryPolicy;
    }

    public void doHttp()
    {
        if(null != mRetryPolicy)
        {
            request.setRetryPolicy ( mRetryPolicy );
        }
        VolleyUtil.getRequestQueue ().add ( request );
    }
}
