package com.huotu.partnermall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.AdBannerConfig;
import com.huotu.partnermall.model.AdImageBean;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.widgets.custom.AdBannerWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 广告页面
 */
public class AdActivity extends BaseActivity implements Handler.Callback {
    @Bind(R.id.adBanner)
    AdBannerWidget adBannerWidget;
    @Bind(R.id.tvSkip)
    TextView tvSkip;
    AdBannerConfig adBannerConfig;
    //多少秒后跳过广告
    int skipTime = 0;
    //推送信息
    Bundle bundlePush;

    Object ad;

    Runnable skipRunnable = new Runnable() {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage(1);
            mHandler.sendMessage(msg);
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

        if (skipRunnable != null) {
            mHandler.removeCallbacks(skipRunnable);
        }
    }

    @Override
    protected void initView() {
        mHandler = new Handler(this);
        //获得推送信息
        if (null != getIntent() && getIntent().hasExtra(Constants.HUOTU_PUSH_KEY)) {
            bundlePush = getIntent().getBundleExtra(Constants.HUOTU_PUSH_KEY);
        }
        if(null!= getIntent() && getIntent().hasExtra(Constants.HUOTU_AD_KEY)){
            ad= getIntent().getBundleExtra(Constants.HUOTU_AD_KEY);
        }

        adBannerConfig = new AdBannerConfig();
        adBannerConfig.setAutoPlay(false);
        adBannerConfig.setHeight(0);
        adBannerConfig.setWidth(0);
        List<AdImageBean> list = new ArrayList<>();
        AdImageBean bean = new AdImageBean();
        bean.setImageUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        bean.setLinkUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        list.add(bean);
        bean = new AdImageBean();
        bean.setImageUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        bean.setLinkUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        list.add(bean);
        bean = new AdImageBean();
        bean.setImageUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        bean.setLinkUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        list.add(bean);

        adBannerConfig.setImages(list);

        skipTime = adBannerConfig.getImages().size() * 2000;

        adBannerWidget.setAdBannerConfig(adBannerConfig);

        setSkipText();
        mHandler.postDelayed(skipRunnable, 1000);
    }

    @OnClick(R.id.tvSkip)
    void skip(View view) {
        startHome();
    }

    void startHome() {
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        if (null != bundlePush) {
            intent.putExtra(Constants.HUOTU_PUSH_KEY, bundlePush);
        }
        ActivityUtils.getInstance().skipActivity(this, intent);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            skipTime--;
            if (skipTime == 0) {
                startHome();
                return true;
            }
            setSkipText();
            return true;
        }
        return false;
    }

    void setSkipText() {
        String desc = String.valueOf(skipTime) + "秒后跳过";
        tvSkip.setText(desc);
    }
}
