package com.huotu.partnermall.utils;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 接口访问入口
 */
public
class ParamInterface {

    enum Method
    {
        POST,
        GET
    }
    private Method method;//请求类型：post get
    private Map    extraParams;
    private ObtainParamsMap obtainMap;
    private
    Context context;

    public ParamInterface(Method method, Map extraParams, Context context)
    {

        this.method = method;
        this.extraParams = extraParams;
        this.context = context;
        obtainMap = new ObtainParamsMap ( context );
    }

    public Object getUrl(String url)
    {
        switch ( method )
        {
            case POST:
            {
                //POST请求
                return doPost();
            }
            case GET:
            {
                //GET请求
                return doGet(url);
            }
            default:
            {
                return null;
            }
        }

    }

    private Map doPost()
    {
        Map map = new HashMap (  );
        Map<String, String> paramMap = obtainMap.obtainMap ( );
        paramMap.putAll ( extraParams );
        String signStr = obtainMap.getSign ( paramMap );
        paramMap.put ( "sign", signStr );
        return paramMap;
    }

    private String doGet(String url)
    {
        String paramMap = obtainMap.getMap();
        String signStr = obtainMap.getSign ( extraParams );
        StringBuilder builder = new StringBuilder ( url );
        try {
            builder.append ( "?sign=" + URLEncoder.encode ( signStr, "UTF-8" ) );
            builder.append ( paramMap );
            Iterator< Map.Entry > entryiT = extraParams.entrySet ( ).iterator ( );
            while ( entryiT.hasNext () )
            {
                Map.Entry entry = entryiT.next ();
                builder.append ( "&"+entry.getKey ()+"="+URLEncoder.encode ( String.valueOf ( entry.getValue () ), "UTF-8" ) );
            }
            return builder.toString ();
        } catch ( UnsupportedEncodingException e )
        {
            KJLoger.e(e.getMessage());
            return null;
        }
    }
}
