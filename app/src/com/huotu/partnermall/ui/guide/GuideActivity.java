package com.huotu.partnermall.ui.guide;

import android.app.NativeActivity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.adapter.ViewPagerAdapter;
import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.image.ImageUtil;
import com.huotu.partnermall.image.ImageUtils;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.PreferenceHelper;
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
public class GuideActivity extends BaseActivity implements View.OnTouchListener ,View.OnClickListener{
    static String TAG = GuideActivity.class.getName();
    @Bind(R.id.vp_activity)
    ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> views;
    private Resources resources;
    //引导图片资源
    private String[] pics;
    int lastX=0;
    TextView skipText;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        resources = this.getResources();
        setContentView(R.layout.guide_ui);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        try {
            views = new ArrayList<>();
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            pics = resources.getStringArray(R.array.guide_icon);

            //初始化引导图片列表
            for (int i = 0; i < pics.length; i++) {
                RelativeLayout iv = (RelativeLayout) LayoutInflater.from(GuideActivity.this).inflate(R.layout.guid_item, null);
                skipText = (TextView) iv.findViewById(R.id.skipText);
                iv.setLayoutParams(mParams);
                int iconId = resources.getIdentifier(pics[i], "drawable", this.getPackageName());

                Drawable menuIconDraw;
                if (iconId > 0) {
                    menuIconDraw = ContextCompat.getDrawable(this, iconId);
                    SystemTools.loadBackground(iv, menuIconDraw);
                }
                skipText.setOnClickListener(this);
                views.add(iv);
            }
            //初始化Adapter
            viewPagerAdapter = new ViewPagerAdapter(views);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setOnTouchListener(this);
            //绑定回调
            //viewPager.addOnPageChangeListener(this);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_DOWN ){
            lastX =(int) event.getX();
        }else if( event.getAction() == MotionEvent.ACTION_MOVE ){
            int tempX = (int)event.getX();
            if( (lastX - tempX)>100 && viewPager.getCurrentItem()== (viewPagerAdapter.getCount()-1)){
                skipText.performClick();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        if( v.getId()== skipText.getId()) {
            //判断是否登录
            if (application.isLogin()) {
                Bundle bd = new Bundle();
                String url = PreferenceHelper.readString(BaseApplication.single, NativeConstants.UI_CONFIG_FILE, NativeConstants.UI_CONFIG_SELF_HREF);
                bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, url);
                bd.putBoolean(NativeConstants.KEY_ISMAINUI, true);
                ActivityUtils.getInstance().skipActivity(GuideActivity.this, NativeActivity.class,bd);
            } else {
                ActivityUtils.getInstance().skipActivity(GuideActivity.this, LoginActivity.class);
            }
        }
    }
}
