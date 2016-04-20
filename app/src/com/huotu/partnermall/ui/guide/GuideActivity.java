package com.huotu.partnermall.ui.guide;

import android.app.NativeActivity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.ui.nativeui.FragMainActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.SystemTools;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 引导界面
 */
public class GuideActivity extends BaseActivity
        implements View.OnTouchListener ,View.OnClickListener, ViewPager.OnPageChangeListener{
    static String TAG = GuideActivity.class.getName();
    @Bind(R.id.vp_activity)
    ViewPager viewPager;
    @Bind(R.id.guide_index)
    LinearLayout llIndex;
    TextView skipText;
    ViewPagerAdapter viewPagerAdapter;
    List<View> views;
    List<ImageView> indexList;
    Resources resources;
    String[] pics;
    int lastX=0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.guide_ui);
        ButterKnife.bind(this);
        initView();
    }

    private void initIndex( int count ){
        llIndex.removeAllViews();
        indexList = new ArrayList<>();
        if(count<1)return;

        for(int i=0;i<count;i++){
            ImageView iv = new ImageView(this);
            iv.setPadding(5, 0, 5, 0);
            if(i==0){
                iv.setImageResource(R.drawable.ic_page_indicator_focused);
            }else {
                iv.setImageResource(R.drawable.ic_page_indicator);
            }
            llIndex.addView(iv);
            indexList.add(iv);
        }
    }

    @Override
    protected void initView() {
        try {
            resources = this.getResources();
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
            viewPager.addOnPageChangeListener(this);

            initIndex(pics.length);
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
                //ActivityUtils.getInstance().skipActivity(GuideActivity.this, NativeActivity.class,bd);
                ActivityUtils.getInstance().skipActivity(GuideActivity.this , FragMainActivity.class , bd );
            } else {
                ActivityUtils.getInstance().skipActivity(GuideActivity.this, LoginActivity.class);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(indexList==null || indexList.size()<1) return;
        int len = indexList.size();
        for(int i=0;i< len ; i++) {
            if( i == position) {
                indexList.get(i).setImageResource(R.drawable.ic_page_indicator_focused);
            }else {
                indexList.get(position).setImageResource(R.drawable.ic_page_indicator);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
