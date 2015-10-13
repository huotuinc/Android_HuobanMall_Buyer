package com.huotu.partnermall.utils;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 授权参数构建类
 */
public
class AuthParamUtils {

    private
    BaseApplication application;

    private String url;

    private long timestamp;

    public
    AuthParamUtils(BaseApplication application, long timestamp, String url)
    {
        this.application = application;
        this.timestamp = timestamp;
        this.url = url;
    }

    public String obtainUrl()
    {
        StringBuilder builder = new StringBuilder (  );
        try {
            Map< String, String > paramMap = new HashMap< String, String > ( );
            //获取url中的参数
            String params = url.substring ( url.indexOf ( ".aspx?" ) + 6, url.length ( ) );
            String[] str = params.split ( "&" );
            if ( str.length > 0 ) {
                for ( String map : str ) {
                    //获取参数
                    String[] values = map.split ( "=" );
                    if ( 2 == values.length ) {
                        paramMap.put ( values[ 0 ], URLEncoder.encode ( values[ 1 ], "UTF-8" ) );
                    }
                    else if ( 1 == values.length ) {
                        paramMap.put ( values[ 0 ], null );
                    }
                }
            }

            //添加额外固定参数
            //1、timestamp
            paramMap.put ( "timestamp", URLEncoder.encode ( String.valueOf ( timestamp ), "UTF-8" ) );
            //appid
            paramMap.put ( "appid", URLEncoder.encode ( Constants.APP_ID , "UTF-8" ));
            //unionid
            paramMap.put ( "unionid", URLEncoder.encode ( application.readUserUnionId ( ), "UTF-8" ) );
            //生成sigin
            paramMap.put ( "sign", getSign ( paramMap ) );

            builder.append ( url );
            builder.append ( "&timestamp="+paramMap.get ( "timestamp" ) );
            builder.append ( "&appid="+paramMap.get ( "appid" ) );
            builder.append ( "&unionid="+paramMap.get ( "unionid" ) );
            builder.append ( "&sign="+paramMap.get ( "sign" ) );

            return builder.toString ();
        }
        catch ( UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                KJLoger.e ( e.getMessage ( ) );
                return null;
            }

    }

    private String getSign(Map map)
    {
        String values = this.doSort(map);
        KJLoger.i ( "sign", values );
        // values = URLEncoder.encode(values);
        //String signHex =DigestUtils.md5DigestAsHex(values.toString().getBytes("UTF-8")).toLowerCase();
        String signHex = EncryptUtil.getInstance().encryptMd532(values);
        KJLoger.i("signHex", signHex);
        return signHex;
    }

    /**
     *
     * @方法描述：获取sign码第二步：参数排序
     * @方法名：doSort
     * @参数：@param map
     * @参数：@return
     * @返回：String
     * @exception
     * @since
     */
    private String doSort(Map<String, String> map)
    {
        map.put ( "appSecret", Constants.APP_SECRET );
        StringBuffer buffer = new StringBuffer();
        List arrayList = new ArrayList (map.entrySet());

        Collections.sort (
                arrayList, new Comparator ( ) {
                    public
                    int compare ( Object arg1, Object arg2 ) {
                        Map.Entry obj1 = ( Map.Entry ) arg1;
                        Map.Entry obj2 = ( Map.Entry ) arg2;
                        return ( obj1.getKey ( ) ).toString ( ).compareTo (
                                ( String ) obj2.getKey ( )
                                                                          );
                    }
                }
                         );

        //
        for (Iterator iter = arrayList.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            // Log.i("key", key);
            // Log.i("value", resultMap.get(key));
            buffer.append(map.get(key));
        }

        return buffer.toString();
    }

}
