package com.huotu.partnermall.widgets.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.AdBannerConfig;
import com.huotu.partnermall.model.AdImageBean;
import com.huotu.partnermall.ui.WebViewActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.DensityUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Banner组件
 * Created by jinxiangdong on 2016/1/13.
 */
public class AdBannerWidget extends com.bigkoo.convenientbanner.ConvenientBanner
        implements OnItemClickListener,
        ViewPager.OnPageChangeListener,
        FrescoControllerListener.ImageCallback{
    private AdBannerConfig config;
    private List<WH> sizeList;
    private AdOnClickListener adOnClickListener;

    public interface AdOnClickListener{
        void onAdClick(AdImageBean bean);
    }

    public void setAdOnClickListener(AdOnClickListener adOnClickListener) {
        this.adOnClickListener = adOnClickListener;
    }

    public AdBannerWidget(Context context) {
        this(context, null);

    }

    public AdBannerWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdBannerWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        sizeList = new ArrayList<>();

        final int iwidth = DensityUtils.getScreenW(getContext());
        int iHeight = 0;
        iHeight = DensityUtils.dip2px( getContext(), iHeight);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( iwidth , iHeight );
        this.setLayoutParams(layoutParams);

        this.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        if(position>=sizeList.size()) return;
//        WH wh = sizeList.get(position);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( wh.getWidth() , wh.getHeight() );
//        this.setLayoutParams(layoutParams);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //    public AdBannerWidget(Context context , AdBannerConfig  config ) {
//        super(context);
//
//        this.config = config;
//        final int iwidth = DensityUtils.getScreenW(getContext()); //this.config.getWidth();
//        int iHeight = 0;//LayoutParams.WRAP_CONTENT;
//        iHeight = DensityUtils.dip2px( getContext(), iHeight);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( iwidth , iHeight );
//        this.setLayoutParams(layoutParams);

//        this.setPages(new CBViewHolderCreator() {
//            @Override
//            public Object createHolder() {
//                return new FrescoHolderView( iwidth , AdBannerWidget.this );
//            }
//        }, config.getImages() )
//        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//        .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
//        .setOnItemClickListener(this);
//
//        this.stopTurning();
//        if(!config.isAutoPlay()) return;
//        if(config.getImages().size()<=1) return;
//
//
//        int time = config.getImages().size()*1500;
//        this.startTurning( time );
//        setAdBannerConfig(config);
//    }

    @Override
    public void onItemClick(int position) {
        if(adOnClickListener!=null){
            AdImageBean bean = config.getImages().get(position);
            adOnClickListener.onAdClick( bean);
        }else {
            AdImageBean bean = config.getImages().get(position);
            go(bean);
        }
    }

    @Override
    public void imageCallback( int width, int height) {
        sizeList.add( new WH(width,height) );

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( width , height );
        this.setLayoutParams(layoutParams);
        this.requestLayout();
    }

    protected void go(AdImageBean bean){

        String relateUrl = bean.getLinkUrl();

        if( relateUrl == null || relateUrl.isEmpty()) return;

        Intent intent = new Intent(getContext() , WebViewActivity.class);
        intent.putExtra(Constants.INTENT_URL, relateUrl);
        ActivityUtils.getInstance().showActivity((Activity) getContext(),intent);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        this.stopTurning();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int he=0;
//        int count = this.getViewPager().getChildCount();
//        for(int i = 0;i<count;i++){
//            View v = this.getViewPager().getChildAt(i);
//            v.measure( widthMeasureSpec , MeasureSpec.makeMeasureSpec( 0 , MeasureSpec.UNSPECIFIED ) );
//            int h = v.getMeasuredHeight();
//            if(h>he){he=h;}
//        }
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(he,MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setAdBannerConfig(AdBannerConfig config , final int defaultImageId ){
        this.config = config;
        final int iwidth = DensityUtils.getScreenW(getContext());
        this.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new FrescoHolderView( iwidth , AdBannerWidget.this , defaultImageId );
            }
        }, config.getImages())
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setOnItemClickListener(this);
        this.stopTurning();
        if(!config.isAutoPlay()) return;
        if(config.getImages().size()<=1) return;

        //int time = config.getTotalSecond() <1 ? config.getImages().size()*1500: config.getTotalSecond()*1000;
        int interval = config.getInterval()<=0? 2000:config.getInterval();
        this.startTurning( interval );
    }


    public class WH{
        private int width;
        private int height;

        public WH(int width,int height){
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
