package com.huotu.partnermall.service;

import com.android.volley.Request;
import com.android.volley.Response;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.mall.config.UrlConfig;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.BuildConfig;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.widgets.custom.PageConfig;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/1/17.
 */

public class BussinessBiz {


    public void getFooterConfig( Response.Listener listener , Response.ErrorListener errorListener  ) {
        //String id = BaseApplication.single.readMerchantId();
        //if( id==null || id.isEmpty() ) return;

        //String url = BuildConfig.SMART_Url;
        //url += "merchantWidgetSettings/search/findByMerchantIdAndScopeDependsScopeOrDefault/nativeCode/" + id + "/global";

        String url = UrlConfig.getFooterConfigUrl();

        String key = Constants.getSMART_KEY();
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(key, Constants.getSMART_SECURITY(), random);
        Map<String, String> header = new HashMap<>();
        header.put("_user_key", key);
        header.put("_user_random", random);
        header.put("_user_secure", secure);

        GsonRequest<PageConfig> request = new GsonRequest<>(Request.Method.GET, url, PageConfig.class, header, listener, errorListener);
        VolleyUtil.getRequestQueue().add(request);
    }

}
