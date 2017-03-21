package com.huotu.partnermall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.AdBannerConfig;
import com.huotu.partnermall.model.AdImageBean;
import com.huotu.partnermall.model.Advertise;
import com.huotu.partnermall.model.AdvertiseModel;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.DensityUtils;
import com.huotu.partnermall.widgets.custom.AdBannerWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 广告页面
 */
public class AdActivity extends BaseActivity implements Handler.Callback ,AdBannerWidget.AdOnClickListener {
    @Bind(R.id.activity_ad)
    FrameLayout rootView;
    @Bind(R.id.adBanner)
    AdBannerWidget adBannerWidget;
    @Bind(R.id.tvSkip)
    TextView tvSkip;
    AdBannerConfig adBannerConfig;
    //多少秒后跳过广告
    int skipTimeSecond = 0;
    //推送信息
    Bundle bundlePush;

    List<Advertise> advertiseList;

    String adLinkUrl;

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

        setImmerseLayout(rootView);

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
            advertiseList= (List<Advertise>) getIntent().getSerializableExtra(Constants.HUOTU_AD_KEY);
        }

        adBannerConfig = new AdBannerConfig();
        adBannerConfig.setAutoPlay(true);
        adBannerConfig.setHeight(0);
        adBannerConfig.setWidth(0);
        List<AdImageBean> list = new ArrayList<>();

        for(Advertise item : advertiseList ){

            AdImageBean bean = new AdImageBean();
            bean.setImageUrl( item.getImages());
            bean.setLinkUrl( item.getLinkUrl());
            list.add(bean);
        }

        adBannerConfig.setImages(list);

        adBannerConfig.setInterval(4000);
        skipTimeSecond = adBannerConfig.getImages().size() * 4;

        adBannerWidget.setCanLoop(false);
        adBannerWidget.setAdBannerConfig(adBannerConfig, R.drawable.login_bg );
        adBannerWidget.setPointViewVisible(false);
        adBannerWidget.setAdOnClickListener(this);


        setSkipText();

        mHandler.post(skipRunnable);
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
        if(null!= adLinkUrl){
            intent.putExtra(Constants.HUOTU_AD_URL_KEY, adLinkUrl);
        }

        ActivityUtils.getInstance().skipActivity(this, intent);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            int second = skipTimeSecond--;
            if (second <=0) {
                startHome();
                return true;
            }
            setSkipText();
            return true;
        }
        return false;
    }

    void setSkipText() {
        if(tvSkip==null)return;
        String desc = String.valueOf(skipTimeSecond) + "秒后跳过";
        tvSkip.setText(desc);
    }

    @Override
    public void onAdClick(AdImageBean bean) {
        if( bean==null) return;
        if( bean.getLinkUrl()==null || bean.getLinkUrl().isEmpty())return;

        adLinkUrl = bean.getLinkUrl();
        startHome();
    }
}
