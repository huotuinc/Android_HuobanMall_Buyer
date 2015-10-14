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
    private JsonObjectRequest request;

    public VolleyHttpUtil( JsonObjectRequest request)
    {
        this.request = request;
    }

    public void doHttp()
    {
        request.setRetryPolicy ( new MallRetryPolicy (  ) );
        VolleyUtil.getRequestQueue ().add ( request );
    }
}
