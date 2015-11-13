package com.huotu.partnermall.ui.guide;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huotu.partnermall.adapter.ViewPagerAdapter;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导界面
 */
public
class GuideActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, Handler.Callback {


    private ViewPager        mVPActivity;
    private ViewPagerAdapter vpAdapter;
    private List< View >     views;
    private int lastValue = - 1;
    private
    Resources resources;
    public Handler mHandler;
    private long exitTime = 0l;

    //引导图片资源
    private String[] pics;

    @Override
    protected
    void onCreate ( Bundle arg0 ) {
        // TODO Auto-generated method stub
        super.onCreate ( arg0 );
        resources = this.getResources ();
        setContentView ( R.layout.guide_ui );
        mHandler = new Handler ( this );
        views = new ArrayList< View > ( );
        mVPActivity = ( ViewPager ) findViewById ( R.id.vp_activity );
        initImage ( );

        //初始化Adapter
        vpAdapter = new ViewPagerAdapter ( views );
        mVPActivity.setAdapter ( vpAdapter );
        //绑定回调
        mVPActivity.setOnPageChangeListener ( this );
    }

    @Override
    protected void findViewById() {
    }

    @Override
    protected
    void initView ( ) {

    }

    private
    void initImage ( ) {
        try {
            pics = this.getResources ( ).getAssets ( ).list ( "guide" );
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                              LinearLayout.LayoutParams.MATCH_PARENT);
            pics = resources.getStringArray ( R.array.guide_icon );

            //初始化引导图片列表
            for(int i=0; i<pics.length; i++) {
                ImageView iv = new ImageView(this);
                iv.setLayoutParams ( mParams );
                int iconId = resources.getIdentifier ( pics[i], "drawable", application.readSysInfo () );
                Drawable menuIconDraw = resources.getDrawable ( iconId );
                SystemTools.loadBackground ( iv, menuIconDraw );
                views.add(iv);
            }
        } catch (IOException e) {
            KJLoger.e ( e.getMessage () );
        }
    }

    /**
     *设置当前的引导页
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length) {
            return;
        }

        mVPActivity.setCurrentItem(position);
    }


    @Override
    public
    void onClick ( View v ) {
        int position = (Integer)v.getTag();
        setCurView ( position );
    }

    @Override
    public
    void onPageScrolled ( int arg0, float v, int i1 ) {
        lastValue = arg0;
    }

    @Override
    public
    void onPageSelected ( int arg0 ) {

    }

    @Override
    public
    void onPageScrollStateChanged ( int arg0 ) {
        if(arg0 == 0){
            if(lastValue == pics.length-1){
                if ( ( System.currentTimeMillis ( ) - exitTime ) > 2000 ) {
                    ToastUtils.showLongToast ( getApplicationContext ( ), "再滑一次进入登录界面" );
                    exitTime = System.currentTimeMillis ( );
                }
                else {
                    //延时2秒后跳入新界面
                    mHandler.postDelayed ( new Runnable ( ) {
                                               @Override
                                               public
                                               void run ( ) {

                                                   //判断是否登录
                                                   if ( application.isLogin ( ) ) {
                                                       ActivityUtils.getInstance ( ).skipActivity ( GuideActivity.this, HomeActivity.class );

                                                   }
                                                   else {
                                                       ActivityUtils.getInstance ( )
                                                                    .skipActivity (
                                                                            GuideActivity
                                                                                    .this,
                                                                            LoginActivity.class );
                                                   }
                                               }
                                           }, 2000 );
                }


            }
        }
    }

    @Override
    public
    boolean handleMessage ( Message msg ) {
        return false;
    }
}
