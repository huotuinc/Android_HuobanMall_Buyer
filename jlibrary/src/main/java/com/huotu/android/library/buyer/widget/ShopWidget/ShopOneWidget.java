package com.huotu.android.library.buyer.widget.ShopWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.ShopBean.ShopOneConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.bean.Constant;

/**
 * Created by Administrator on 2016/1/15.
 */
public class ShopOneWidget extends RelativeLayout{
    private ShopOneConfig config;
    private int id1;
    private int id2;
    private int id3;

    public ShopOneWidget(Context context, ShopOneConfig config){
        super(context);
        this.config = config;

        int leftRight = DensityUtils.dip2px(context, config.getPaddingLeft());
        int topBottom = DensityUtils.dip2px(context, config.getPaddingTop());
        this.setPadding(leftRight, topBottom, leftRight, topBottom);
        this.setBackgroundColor(Color.parseColor(config.getBackColor()));

        SimpleDraweeView leftImage = new SimpleDraweeView(context);
        id1 = leftImage.hashCode();
        leftImage.setId(id1);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftImage.setLayoutParams(layoutParams);
        this.addView(leftImage);

        TextView tvLeftTitle = new TextView(context);
        tvLeftTitle.setSingleLine();
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, id1);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvLeftTitle.setLayoutParams(layoutParams);
        tvLeftTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvLeftTitle.setTextColor(Color.parseColor(config.getFontColor()));
        this.addView(tvLeftTitle);

        TextView tvRighTitle = new TextView(context);
        int id3 = tvRighTitle.hashCode();
        tvRighTitle.setId(id3);
        tvRighTitle.setSingleLine();
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvRighTitle.setLayoutParams(layoutParams);
        tvRighTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvRighTitle.setTextColor(Color.parseColor(config.getFontColor()));
        this.addView(tvRighTitle);

        SimpleDraweeView rightImage = new SimpleDraweeView(context);
        id2 = rightImage.hashCode();
        rightImage.setId(id2);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF, id3);
        rightImage.setLayoutParams(layoutParams);
        this.addView(rightImage);

        int imageWidthPx = DensityUtils.dip2px(context, 30 );
        if( config.getShow_type() == Constant.LOGO_1 ) {

            FrescoDraweeController.loadImage(leftImage, imageWidthPx, config.getImageUrl1());
            tvLeftTitle.setText(config.getTitle_linkname1());
            tvRighTitle.setText(config.getTitle_linkname());
            FrescoDraweeController.loadImage(rightImage, imageWidthPx, config.getImageUrl());
        }else{
            //TODO 通过API接口获得信息

        }
    }

}
