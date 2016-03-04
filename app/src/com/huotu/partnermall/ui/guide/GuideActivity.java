package com.huotu.partnermall.ui.guide;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.partnermall.adapter.ViewPagerAdapter;
import com.huotu.partnermall.image.ImageUtil;
import com.huotu.partnermall.image.ImageUtils;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 引导界面
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    static String TAG = GuideActivity.class.getName();
    @Bind(R.id.vp_activity)
    ViewPager mVPActivity;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private int lastValue = -1;
    private Resources resources;
    //public Handler mHandler;
    //引导图片资源
    private String[] pics;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        resources = this.getResources();
        setContentView(R.layout.guide_ui);
        ButterKnife.bind(this);
        //mHandler = new Handler ( this );
        views = new ArrayList<>();
        initImage();
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        mVPActivity.setAdapter(vpAdapter);
        //绑定回调
        mVPActivity.addOnPageChangeListener(this);
    }

    @Override
    protected void initView() {
    }

    private void initImage() {
        try {
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            pics = resources.getStringArray(R.array.guide_icon);

            //初始化引导图片列表
            for (int i = 0; i < pics.length; i++) {
                RelativeLayout iv = (RelativeLayout) LayoutInflater.from(GuideActivity.this).inflate(R.layout.guid_item, null);
                TextView skipText = (TextView) iv.findViewById(R.id.skipText);
                iv.setLayoutParams(mParams);
                //int iconId = resources.getIdentifier ( pics[i], "drawable", application.readSysInfo () );
                int iconId = resources.getIdentifier(pics[i], "drawable", this.getPackageName());

                Drawable menuIconDraw = null;
                if (iconId > 0) {
                    menuIconDraw = ContextCompat.getDrawable( this , iconId);//resources.getDrawable(iconId);
                    SystemTools.loadBackground(iv, menuIconDraw);
                }
                skipText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否登录
                        if (application.isLogin()) {
                            ActivityUtils.getInstance().skipActivity(GuideActivity.this, HomeActivity.class);
                        } else {
                            ActivityUtils.getInstance().skipActivity(GuideActivity.this, LoginActivity.class);
                        }
                    }
                });
                views.add(iv);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        mVPActivity.setCurrentItem(position);
    }


    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
    }

    @Override
    public void onPageScrolled(int arg0, float v, int i1) {
        lastValue = arg0;
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

//    @Override
//    public boolean handleMessage ( Message msg ) {
//        return false;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
