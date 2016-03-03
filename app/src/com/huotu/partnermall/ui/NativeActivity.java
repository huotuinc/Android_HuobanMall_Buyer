package com.huotu.partnermall.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huotu.android.library.buyer.bean.PageConfig;
import com.huotu.android.library.buyer.bean.WidgetConfig;
import com.huotu.android.library.buyer.utils.GsonUtil;
import com.huotu.android.library.buyer.widget.WidgetBuilder;
import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.EventManager;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NativeActivity extends AppCompatActivity implements Handler.Callback{
    @Bind(R.id.activity_native_scrollview)
    PullToRefreshScrollView scrollView;
    @Bind(R.id.activity_native_main)
    LinearLayout llMain;
    @Bind(R.id.activity_native_footer)
    RelativeLayout rlFooter;

    PageConfig uiConfig;

    EventManager eventManager;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        ButterKnife.bind(this);
        eventManager = new EventManager(this);
        eventManager.Register();

        mHandler = new Handler(this);

        loadWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        eventManager.UnRegister();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    protected void loadWidgets(){
        String json = PreferenceHelper.readString( this , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_KEY);
        if(TextUtils.isEmpty(json)){
            ToastUtils.showLongToast(this,"配置信息丢失");
            return;
        }

        GsonUtil<PageConfig> gsonUtil = new GsonUtil<>();
        PageConfig pageConfig = new PageConfig();
        gsonUtil.toBean(json , pageConfig);
        for(WidgetConfig config : pageConfig.getWidgets() ) {
            View view = WidgetBuilder.build( config , this );
            llMain.addView(view);
        }
    }
}
