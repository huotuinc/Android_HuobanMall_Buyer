package com.huotu.partnermall.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.AdBannerConfig;
import com.huotu.partnermall.model.AdImageBean;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.widgets.custom.AdBannerWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdActivity extends BaseActivity {
    @Bind(R.id.adBanner)
    AdBannerWidget adBannerWidget;
    @Bind(R.id.tvSkip)
    TextView tvSkip;
    AdBannerConfig adBannerConfig;

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
    }

    @Override
    protected void initView() {
        adBannerConfig = new AdBannerConfig();
        adBannerConfig.setAutoPlay(false);
        adBannerConfig.setHeight(0);
        adBannerConfig.setWidth(0);
        List<AdImageBean> list = new ArrayList<>();
        AdImageBean bean = new AdImageBean();
        bean.setImageUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        bean.setLinkUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        list.add( bean );
        bean = new AdImageBean();
        bean.setImageUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        bean.setLinkUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        list.add( bean );
        bean = new AdImageBean();
        bean.setImageUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        bean.setLinkUrl("http://taskapi.fhsilk.com//resource/appimg/appad/pic20161119155147598268.jpg");
        list.add( bean );

        adBannerConfig.setImages(list);

        adBannerWidget.setAdBannerConfig(adBannerConfig);

    }
}
