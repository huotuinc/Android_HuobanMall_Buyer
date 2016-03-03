package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.AdBean.AdThreeConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;

/**
 * 橱窗二组件 左一大右两小
 * Created by jinxiangdong on 2016/1/14.
 */
public class AdThreeWidget extends LinearLayout {
    private AdThreeConfig config;

    private SimpleDraweeView iv1;
    private SimpleDraweeView iv2;
    private SimpleDraweeView iv3;
    private LinearLayout ll1;

    public AdThreeWidget(Context context , AdThreeConfig config) {
        super(context);

        this.config = config;

        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(Color.parseColor( config.getWidgetBackColor() ));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        iv1 = new SimpleDraweeView(context);

        ll1 = new LinearLayout(context);
        ll1.setOrientation(VERTICAL);

        iv2 = new SimpleDraweeView(context);

        iv3 = new SimpleDraweeView(context);


        int leftPx = DensityUtils.dip2px(context, config.getLeftDistance());
        int rightPx = DensityUtils.dip2px( context , config.getRightDistance());
        int topPx = DensityUtils.dip2px( context, config.getTopDistance());
        int bottomPx = DensityUtils.dip2px( context , config.getBottomDistance());
        this.setPadding(leftPx, topPx, rightPx, bottomPx);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        //layoutParams.weight =1;
        iv1.setLayoutParams(layoutParams);


        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        //layoutParams.weight = 1;
        int px = DensityUtils.dip2px(context,1);
        layoutParams.setMargins(px , 0, 0, 0);
        ll1.setLayoutParams(layoutParams);
        this.addView(iv1);
        this.addView(ll1);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1.0f);
        layoutParams.setMargins(0, 0, 0, 1);
        iv2.setLayoutParams(layoutParams);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1.0f);
        iv3.setLayoutParams(layoutParams);
        ll1.addView(iv2);
        ll1.addView(iv3);

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
            int itemWidth = ( context.getResources().getDisplayMetrics().widthPixels - leftPx - rightPx)/2;

            FrescoDraweeController.loadImage(iv3,itemWidth,config.getImages().get(2).getImageUrl());
        }

    }
}
