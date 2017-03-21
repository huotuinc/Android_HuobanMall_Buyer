package com.huotu.mall.config;

import com.android.volley.Request;
import com.android.volley.Response;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.BuildConfig;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.widgets.custom.PageConfig;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/1/17.
 */

public class UrlConfig {

    public static String getFooterConfigUrl() {
        String id = BaseApplication.single.readMerchantId();
        String url = BuildConfig.SMART_Url;
        url += "merchantWidgetSettings/search/findByMerchantIdAndScopeDependsScopeOrDefault/nativeCode/" + id + "/global";
        return url;
    }

}
