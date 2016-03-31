package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.widget.LinearLayout;
import com.huotu.android.library.buyer.R;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.huotu.android.library.buyer.bean.AdBean.AdBannerConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
/**
 * Banner组件
 * Created by jinxiangdong on 2016/1/13.
 */
public class AdBannerWidget extends com.bigkoo.convenientbanner.ConvenientBanner implements OnItemClickListener {
    private AdBannerConfig config;

    public AdBannerWidget(Context context) {
        super(context);
    }

    public AdBannerWidget(Context context , AdBannerConfig  config ) {
        super(context);

        this.config = config;
        final int iwidth = DensityUtils.getScreenW(getContext()); //this.config.getWidth();
        int iHeight = 200;//LayoutParams.WRAP_CONTENT;
        iHeight = DensityUtils.dip2px( getContext(), iHeight);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( iwidth , iHeight );
        this.setLayoutParams(layoutParams);

        this.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new FrescoHolderView( iwidth );
            }
        }, config.getImages() )
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
        .setOnItemClickListener(this);

        this.stopTurning();
        if(!config.getAutoPlay()) return;
        if(config.getImages().size()<=1) return;


        int time = config.getImages().size()*1500;
        this.startTurning( time );
    }

    @Override
    public void onItemClick(int position) {
        AdImageBean bean = config.getImages().get( position );
        go(bean);
    }

    protected void go(AdImageBean bean){
        String relateUrl = bean.getLinkUrl();
        String name = bean.getTitle();
        CommonUtil.link(name, relateUrl);
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
}
