package com.jxd.jlibrary;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.huotu.android.library.buyer.bean.AdBean.AdOneConfig;
import com.huotu.android.library.buyer.bean.WidgetConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testss(){
        AdOneConfig config = new AdOneConfig();
        WidgetConfig c = new WidgetConfig();
        Map m = new HashMap();
        m.put("paddingLeft","");
        m.put("paddingRight" ,"");

    }
}