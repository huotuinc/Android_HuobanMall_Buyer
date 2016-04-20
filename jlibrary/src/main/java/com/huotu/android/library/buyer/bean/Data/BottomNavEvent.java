package com.huotu.android.library.buyer.bean.Data;

import com.google.gson.internal.Streams;
import com.huotu.android.library.buyer.bean.FooterBean.FooterOneConfig;
import com.huotu.android.library.buyer.bean.WidgetConfig;

/**
 * Created by Administrator on 2016/4/13.
 */
public class BottomNavEvent {
    WidgetConfig widgetConfig;
    public BottomNavEvent( WidgetConfig widgetConfig){
        this.widgetConfig = widgetConfig;
    }

    public WidgetConfig getWidgetConfig() {
        return widgetConfig;
    }

    public void setWidgetConfig(WidgetConfig widgetConfig) {
        this.widgetConfig = widgetConfig;
    }
}
