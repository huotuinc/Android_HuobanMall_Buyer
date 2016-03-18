package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.huotu.android.library.buyer.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.bean.AdBean.AdPageBannerConfig;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jinxiangdong on 2016/1/14.
 * 滑动 广告控件
 *
 */
public class AdPageBannerWidget extends LinearLayout implements View.OnClickListener{
    private AdPageBannerConfig config;
    HorizontalScrollView horizontalScrollView;
    LinearLayout ll;
    static Handler timerhandler;
    int itemWidth;
    static Runnable runnable;

    public AdPageBannerWidget(Context context , AdPageBannerConfig config ) {
        super(context);

        this.config = config;

        this.setBackgroundColor(Color.parseColor(config.getBackcolor()));
        int leftPx = DensityUtils.dip2px(getContext(), config.getPaddingLeft());
        int topPx = DensityUtils.dip2px(getContext(), config.getPaddingTop());
        int rightPx = DensityUtils.dip2px(getContext(), config.getPaddingRight());
        int bottomPx = DensityUtils.dip2px(getContext(), config.getPaddingBottom());
        this.setPadding(leftPx, topPx, rightPx, bottomPx);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.ad_pagebanner, this, true);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.ad_pagebanner_hsv);
        ll = (LinearLayout) findViewById(R.id.ad_pagebanner_ll);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int pagecount = (int) config.getSwiperPage();
        this.itemWidth = screenWidth / pagecount;
        ll.removeAllViews();

        if (config.getImages() == null || config.getImages().size() < 1) return;
        for (AdImageBean item : config.getImages()) {
            SimpleDraweeView iv = new SimpleDraweeView(getContext());
            LayoutParams layoutParams = new LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(2, 0, 2, 0);
            iv.setLayoutParams(layoutParams);

            String imageUrl = Variable.resourceUrl + item.getImageUrl();

            FrescoDraweeController.loadImage(iv, itemWidth, imageUrl);
            ll.addView(iv);
            iv.setTag( item );
            iv.setOnClickListener(this);
        }

        if (!config.isSwiperStop()) return;
        if (config.getImages().size() <= pagecount) return;

        timerhandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                int off = horizontalScrollView.getScrollX() + itemWidth;
                int t = horizontalScrollView.getMaxScrollAmount();
                View v = horizontalScrollView.getChildAt(0);
                int rightpx = v.getRight();
                int max = itemWidth * 10;

                int temp = rightpx - horizontalScrollView.getScrollX() - horizontalScrollView.getWidth();

                if (temp > 0) {
                    horizontalScrollView.smoothScrollTo(off, 0);
                    //horizontalScrollView.smoothScrollTo( horizontalScrollView.getMeasuredWidth() * 2, 0);
                } else {
                    //horizontalScrollView.smoothScrollTo(0, 0);
                    horizontalScrollView.smoothScrollTo(0, 0);
                }
                timerhandler.postDelayed(this, 2000);
            }
        };
        timerhandler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if( timerhandler==null) return;
        timerhandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        if( v.getTag()!=null && v.getTag() instanceof AdImageBean) {
            AdImageBean bean = (AdImageBean) v.getTag();
            String url = Variable.siteUrl + bean.getLinkUrl();
            String name = bean.getLinkName();
            EventBus.getDefault().post(new LinkEvent(name , url ));
        }
    }
}
