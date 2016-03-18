package com.huotu.android.library.buyer.widget.ShopWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.ShopBean.ShopTwoConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;

import org.greenrobot.eventbus.EventBus;


/**
 * 店铺头部二
 * Created by jinxiangdong on 2016/1/15.
 */
public class ShopTwoWidget extends RelativeLayout implements View.OnClickListener{
    private ShopTwoConfig config;
    SimpleDraweeView ivLeft;
    SimpleDraweeView ivMiddle;
    SimpleDraweeView ivRight;

    public ShopTwoWidget(Context context , ShopTwoConfig config ) {
        super(context);

        this.config = config;

        ivLeft = new SimpleDraweeView(context);
        ivLeft.setOnClickListener(this);
        ivMiddle = new SimpleDraweeView(context);
        ivMiddle.setOnClickListener(this);
        ivRight = new SimpleDraweeView(context);
        ivRight.setOnClickListener(this);

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

        int topPx = DensityUtils.dip2px( getContext() , config.getPaddingTop());
        int leftPx = DensityUtils.dip2px(getContext(), config.getPaddingLeft());
        this.setPadding( leftPx , topPx ,leftPx,topPx );
        this.setBackgroundColor(Color.parseColor(this.config.getBackColor()));

        //
        if( config.isShow1() ) {
            String imageUrl1 = Variable.resourceUrl + config.getImageUrl1();
            FrescoDraweeController.loadImage(ivLeft, widthPx, imageUrl1);
        }
        if( config.isShow2()) {
            String imageUrl2 = Variable.resourceUrl + config.getImageUrl2();
            FrescoDraweeController.loadImage(ivMiddle, widthPx, imageUrl2);
        }
        if( config.isShow3()) {
            String imageUrl3 = Variable.resourceUrl + config.getImageUrl3();
            FrescoDraweeController.loadImage(ivRight, widthPx, imageUrl3);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== ivLeft.getId()){
            String url = Variable.resourceUrl + config.getLinkUrl1();
            EventBus.getDefault().post(new LinkEvent("",url));
        }else if(v.getId() == ivMiddle.getId()){
            String url = Variable.resourceUrl + config.getLinkUrl2();
            EventBus.getDefault().post(new LinkEvent("",url));
        }else if( v.getId() == ivRight.getId()){
            String url = Variable.resourceUrl + config.getLinkUrl3();
            EventBus.getDefault().post(new LinkEvent("",url));
        }
    }
}
