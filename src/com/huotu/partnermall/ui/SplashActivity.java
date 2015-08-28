package com.huotu.partnermall.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.MerchantBean;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.XMLParserUtils;


public class SplashActivity extends BaseActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();

    private ImageView mSplashItem_iv = null;
    @Override
    protected void findViewById() {
        mSplashItem_iv = (ImageView) findViewById( R.id.splash_loading_item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_DENSITY = metrics.density;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        Constants.SCREEN_WIDTH = metrics.widthPixels;

        mHandler = new Handler(getMainLooper());
        findViewById();
        initView();
    }

    @Override
    protected void initView() {
        Animation translate = AnimationUtils.loadAnimation(this,
                                                           R.anim.splash_loading);
        translate.setAnimationListener(new AnimationListener() {

                                           @Override
                                           public void onAnimationStart(Animation animation) {
                                               //定位
                                               //检测网络
                                               //加载商家信息
                                               MerchantBean merchant = XMLParserUtils.getInstance ().readMerchantInfo ( SplashActivity.this );
                                           }

                                           @Override
                                           public void onAnimationRepeat(Animation animation) {

                                           }

                                           @Override
                                           public void onAnimationEnd(Animation animation) {
                                               openActivity( HomeActivity.class);
                                               overridePendingTransition(R.anim.push_left_in,
                                                                         R.anim.push_left_out);
                                               SplashActivity.this.finish();
                                           }
                                       });
        mSplashItem_iv.setAnimation(translate);
    }

}

