package com.huotu.android.library.buyer.widget.ShopWidget;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.ShopBean.ShopTwoConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;


/**
 * Created by Administrator on 2016/1/15.
 */
public class ShopTwoWidget extends RelativeLayout {
    private ShopTwoConfig config;

    public ShopTwoWidget(Context context , ShopTwoConfig config ) {
        super(context);

        this.config = config;

        SimpleDraweeView ivLeft = new SimpleDraweeView(context);
        SimpleDraweeView ivMiddle = new SimpleDraweeView(context);
        SimpleDraweeView ivRight = new SimpleDraweeView(context);

        int widthPx =DensityUtils.dip2px(context, 30 ); //DensityUtils.dip2px(context, this.config.getImageLeft().getWidth());
        int heightPx = DensityUtils.dip2px(context, 30 ); //DensityUtils.dip2px( context , this.config.getImageLeft().getHeight());
        LayoutParams layoutParams = new LayoutParams( widthPx , heightPx );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivLeft.setLayoutParams(layoutParams);
        this.addView(ivLeft);


        widthPx =  DensityUtils.dip2px( context ,30);// DensityUtils.dip2px( context , this.config.getImageMiddle().getWidth() );
        heightPx = DensityUtils.dip2px( context ,30);// DensityUtils.dip2px(context, this.config.getImageMiddle().getHeight());
        layoutParams = new LayoutParams(widthPx,heightPx);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ivMiddle.setLayoutParams(layoutParams);
        this.addView(ivMiddle);

        widthPx = DensityUtils.dip2px( context, 30);//DensityUtils.dip2px( context, this.config.getImageRight().getWidth());
        heightPx = DensityUtils.dip2px( context, 30);//DensityUtils.dip2px(context, this.config.getImageRight().getHeight());
        layoutParams = new LayoutParams(widthPx,heightPx);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivRight.setLayoutParams(layoutParams);
        this.addView(ivRight);

        int topPx = DensityUtils.dip2px( getContext() , config.getVerticalDistance());
        int leftPx = DensityUtils.dip2px(getContext(), config.getAroundDistance());
        this.setPadding( leftPx , topPx ,leftPx,topPx );
        this.setBackgroundColor(Color.parseColor(this.config.getWidgetBackColor()));

        //TODO 通过API接口 获得图标信息
//        FrescoDraweeController.loadImage(ivLeft, widthPx, this.config.getImageLeft().getImageUrl());
//        FrescoDraweeController.loadImage(ivMiddle, widthPx, this.config.getImageMiddle().getImageUrl());
//        FrescoDraweeController.loadImage(ivRight, widthPx, this.config.getImageRight().getImageUrl());

    }

}
