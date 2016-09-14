package com.huotu.partnermall.utils;

import android.util.Log;

import com.huotu.partnermall.model.ColorBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取properties文件工具类
 */
public
class PropertiesUtil {

    private static class Holder
    {
        private static final PropertiesUtil instance = new PropertiesUtil();
    }

    private PropertiesUtil()
    {

    }

    public static final PropertiesUtil getInstance()
    {
        return Holder.instance;
    }

    public
    ColorBean readProperties(InputStream is)
    {

        ColorBean color = null;
        Map<String, String> colorMap = new HashMap< String, String > (  );


        Properties property = new Properties (  );
        try {
            property.load ( is );
            Enumeration en = property.propertyNames ();
            while ( en.hasMoreElements () )
            {
                String key = ( String ) en.nextElement ();
                colorMap.put ( key, property.getProperty ( key ) );
            }

            color = new ColorBean ();
            color.setColorMap ( colorMap );

            return color;
        }
        catch ( IOException e ) {
            Log.e ( "error" , e.getMessage () );
            return null;
        }

    }
}
