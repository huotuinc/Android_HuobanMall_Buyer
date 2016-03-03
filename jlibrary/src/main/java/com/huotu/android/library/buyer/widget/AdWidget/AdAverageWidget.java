package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.AdBean.AdAverageConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget;

/**
 * 均分广告
 * Created by jinxiangdong on 2016/1/14.
 */
public class AdAverageWidget extends com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget {
    private AdAverageConfig config = null;
    public AdAverageWidget(Context context , AdAverageConfig config) {
        super(context);

        this.config = config;

        int outLeftPx = DensityUtils.dip2px(context, this.config.getLeftDistance());
        int outRightPx= DensityUtils.dip2px(context,this.config.getRightDistance());
        int topPx = DensityUtils.dip2px( context , this.config.getUpDownDistance());
        int bottomPx = DensityUtils.dip2px(context,this.config.getUpDownDistance());
        int leftPx = DensityUtils.dip2px(context,this.config.getLeftRightDistance());
        int rightPx = DensityUtils.dip2px(context,this.config.getLeftRightDistance());
        //this.setPadding( leftPx+outLeftPx , topPx , rightPx+outRightPx , bottomPx );
        this.setBackgroundColor(Color.parseColor( this.config.getWidgetBackColor()));

        //TODO
        if( this.config.getImages() ==null )return;
        int itemCount = config.getImages().size();
        int itemWidth  = (screenWidth- outLeftPx - outRightPx - itemCount* (leftPx+rightPx)) / this.config.getImages().size();
        for(int i =0 ; i< this.config.getImages().size() ; i++ ){
            AdImageBean item = this.config.getImages().get(i);
            SimpleDraweeView iv = new SimpleDraweeView(context);
            LinearLayout.LayoutParams layoutParams =new LayoutParams( itemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
            if( i ==0 ){
                int leftitem = outLeftPx + leftPx;
                int rightitem = rightPx;
                layoutParams.setMargins( leftitem , topPx , rightitem , bottomPx);
            }else if( i == config.getImages().size()-1){
                int leftitem = leftPx;
                int rightitem = rightPx + outRightPx;
                layoutParams.setMargins( leftitem , topPx , rightitem , bottomPx);
            }else {
                int leftitem =  leftPx;
                int rightitem = rightPx;
                layoutParams.setMargins( leftitem , topPx , rightitem , bottomPx);
            }

            iv.setLayoutParams(layoutParams);

            this.addView(iv);
            FrescoDraweeController.loadImage(iv, itemWidth, item.getImageUrl());

        }
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
