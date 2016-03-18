package com.huotu.partnermall.ui.nativeui;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.bean.GroupBean.GoodsGroupConfig;
import com.huotu.android.library.buyer.widget.GroupWidget.GoodsGroupWidget;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.NativeBaseActivity;

public class ClassActivity extends NativeBaseActivity {
    GoodsGroupConfig config;
    GoodsGroupWidget widget;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        ll = (LinearLayout) findViewById(R.id.ll);

    }
}
