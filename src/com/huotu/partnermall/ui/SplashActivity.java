package com.huotu.partnermall.ui;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.ColorBean;
import com.huotu.partnermall.model.MerchantBean;
import com.huotu.partnermall.service.LocationService;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.PropertiesUtil;
import com.huotu.partnermall.utils.XMLParserUtils;

import java.io.IOException;
import java.io.InputStream;


public class SplashActivity extends BaseActivity {

    public static final String TAG = SplashActivity.class.getSimpleName ( );

    private ImageView mSplashItem_iv = null;
    private
    BaseApplication application;
    private Intent locationI = null;
    private boolean isConnection = false;// 假定无网络连接

    @Override
    protected
    void findViewById ( ) {
        mSplashItem_iv = ( ImageView ) findViewById ( R.id.splash_loading_item );
    }

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) SplashActivity.this.getApplication ( );
        setContentView ( R.layout.activity_splash );

        DisplayMetrics metrics = new DisplayMetrics ( );
        getWindowManager ( ).getDefaultDisplay ( ).getMetrics ( metrics );
        Constants.SCREEN_DENSITY = metrics.density;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        Constants.SCREEN_WIDTH = metrics.widthPixels;

        mHandler = new Handler ( getMainLooper());
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
                                               //检测网络
                                               isConnection = application.checkNet ( SplashActivity.this );
                                               if(!isConnection)
                                               {
                                                   //无网络日志
                                                   KJLoger.i ( "设置无网络" );
                                                   //给出界面上的提示
                                                   AlertDialog.Builder dialog = new AlertDialog.Builder (
                                                           SplashActivity.this);
                                                   dialog.setTitle("网络设置")
                                                         .setMessage("网络不可用，请设置")
                                                         .setPositiveButton("设置",
                                                                            new DialogInterface.OnClickListener()
                                                                            {

                                                                                @Override
                                                                                public void onClick(
                                                                                        DialogInterface dialog,
                                                                                        int which)
                                                                                {
                                                                                    // TODO Auto-generated method stub

                                                                                    Intent intent = null;
                                                                                    // 判断手机系统的版本 即API大于10 就是3.0或以上版本
                                                                                    if (android.os.Build.VERSION.SDK_INT > 10)
                                                                                    {
                                                                                        intent = new Intent(
                                                                                                android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                                                                    } else
                                                                                    {
                                                                                        intent = new Intent();
                                                                                        ComponentName component = new ComponentName(
                                                                                                "com.android.settings",
                                                                                                "com.android.settings.WirelessSettings");
                                                                                        intent.setComponent(component);
                                                                                        intent.setAction("android.intent.action.VIEW");
                                                                                    }
                                                                                    SplashActivity.this
                                                                                            .startActivity(intent);
                                                                                }
                                                                            })
                                                         .setNegativeButton("取消",
                                                                            new DialogInterface.OnClickListener()
                                                                            {

                                                                                @Override
                                                                                public void onClick(
                                                                                        DialogInterface dialog,
                                                                                        int which)
                                                                                {
                                                                                    // TODO Auto-generated method stub
                                                                                    dialog.dismiss();
                                                                                    // 未设置网络，关闭应用
                                                                                    closeSelf(SplashActivity.this);
                                                                                }
                                                                            }).show ( );
                                               }
                                               //定位
                                               locationI = new Intent ( SplashActivity.this,
                                                                        LocationService.class );
                                               SplashActivity.this.startService ( locationI );
                                               //加载商家信息
                                               //判断
                                               if(!application.checkMerchantInfo ()) {
                                                   //设置商户信息
                                                   MerchantBean merchant = XMLParserUtils.getInstance ( ).readMerchantInfo ( SplashActivity.this );
                                                   KJLoger.i ( "商户信息获取成功。" );
                                                   //写入文件
                                                   if(null != merchant)
                                                   {
                                                       application.writeMerchantInfo ( merchant );
                                                   }
                                                   else
                                                   {
                                                       KJLoger.e ( "载入商户信息失败。" );
                                                   }
                                               }
                                               //加载颜色配置信息
                                               try {
                                                   InputStream is = SplashActivity.this.getAssets
                                                               ().open ( "color.properties" );
                                                   ColorBean color = PropertiesUtil.getInstance ().readProperties ( is );

                                                   //记录颜色值
                                                   KJLoger.i ( "记录颜色值." );
                                               }
                                               catch ( IOException e ) {
                                                   KJLoger.e ( e.getMessage () );
                                               }

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
        mSplashItem_iv.setAnimation ( translate );
    }

    @Override
    protected
    void onDestroy ( ) {
        super.onDestroy ( );
        if (null != locationI)
        {
            stopService(locationI);
        }
    }
}

