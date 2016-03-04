package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.AdBean.AdFourConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;

/**
 * Created by Administrator on 2016/1/14.
 */
public class AdFourWidget extends LinearLayout {
    private AdFourConfig config;
    private SimpleDraweeView iv1;
    private SimpleDraweeView iv2;
    private SimpleDraweeView iv3;
    private SimpleDraweeView iv4;
    private LinearLayout ll1;
    private LinearLayout ll2;

    public AdFourWidget(Context context , AdFourConfig config) {
        super(context);

        this.config = config;
        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(Color.parseColor( config.getBackcolor()));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        int leftPx = DensityUtils.dip2px(context, config.getPaddingLeft());
        int rightPx = DensityUtils.dip2px( context , config.getPaddingRight());
        int topPx = DensityUtils.dip2px( context, config.getPaddingTop());
        int bottomPx = DensityUtils.dip2px( context , config.getPaddingBottom());
        this.setPadding(leftPx, topPx, rightPx, bottomPx);


        iv1 = new SimpleDraweeView(context);
        iv2 = new SimpleDraweeView(context);
        iv3 = new SimpleDraweeView(context);
        iv4 = new SimpleDraweeView(context);
        ll1 = new LinearLayout(context);
        ll1.setOrientation(VERTICAL);
        ll2 = new LinearLayout(context);
        ll2.setOrientation(HORIZONTAL);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        iv1.setLayoutParams(layoutParams);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        int px = DensityUtils.dip2px(context,1);
        layoutParams.setMargins(px, 0, 0, 0);
        ll1.setLayoutParams(layoutParams);
        this.addView(iv1);
        this.addView(ll1);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1.0f);
        ll2.setLayoutParams(layoutParams);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1.0f);
        px = DensityUtils.dip2px(context , 1);
        layoutParams.setMargins(0,px , 0,0 );
        iv2.setLayoutParams(layoutParams);
        ll1.addView(ll2);
        ll1.addView(iv2);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        iv3.setLayoutParams(layoutParams);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        layoutParams.setMargins(px,0,0,0);
        iv4.setLayoutParams(layoutParams);
        ll2.addView(iv3);
        ll2.addView(iv4);

        if( config.getImages()==null ) return;
        if( config.getImages().size()>0) {
            int itemWidth = ( context.getResources().getDisplayMetrics().widthPixels - leftPx - rightPx)/2;
            FrescoDraweeController.loadImage(iv1, itemWidth, config.getImages().get(0).getImageUrl());
        }
        if( config.getImages().size()>1){
            int itemWidth = ( context.getResources().getDisplayMetrics().widthPixels - leftPx - rightPx)/2;

            FrescoDraweeController.loadImage(iv2,itemWidth , config.getImages().get(1).getImageUrl());
        }
        if( config.getImages().size()>2){
            int itemWidth = ( context.getResources().getDisplayMetrics().widthPixels - leftPx - rightPx)/4;

            FrescoDraweeController.loadImage(iv3,itemWidth,config.getImages().get(2).getImageUrl());
        }
        if( config.getImages().size()>3){
            int itemWidth = ( context.getResources().getDisplayMetrics().widthPixels - leftPx - rightPx)/4;

            FrescoDraweeController.loadImage(iv4,itemWidth,config.getImages().get(3).getImageUrl());
        }
    }

}
