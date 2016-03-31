package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
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
public class AdAverageWidget extends com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget
                            implements View.OnClickListener{
    private AdAverageConfig config = null;
    public AdAverageWidget(Context context , AdAverageConfig config) {
        super(context);

        this.config = config;

        int outLeftPx = DensityUtils.dip2px(context, this.config.getPaddingOutLeft());
        int outRightPx= DensityUtils.dip2px(context,this.config.getPaddingOutRight());
        int topPx = DensityUtils.dip2px( context , this.config.getPaddingTop());
        int bottomPx = DensityUtils.dip2px(context,this.config.getPaddingTop());
        int leftPx = DensityUtils.dip2px(context,this.config.getPaddingLeft());
        int rightPx = DensityUtils.dip2px(context,this.config.getPaddingLeft());
        this.setPadding( outLeftPx , topPx , outRightPx , bottomPx );

        if(!TextUtils.isEmpty(this.config.getBackcolor())) {
            this.setBackgroundColor(CommonUtil.parseColor(this.config.getBackcolor()));
        }

        if( this.config.getImages() ==null )return;
        int itemCount = config.getImages().size();
        int itemWidth  = (screenWidth- outLeftPx - outRightPx - (itemCount +1) * leftPx ) / itemCount;
        topPx=bottomPx=0;
        for(int i =0 ; i< itemCount ; i++ ){
            AdImageBean item = this.config.getImages().get(i);
            SimpleDraweeView iv = new SimpleDraweeView(context);
            LinearLayout.LayoutParams layoutParams =new LayoutParams( itemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
            if( i == 0 && i != (itemCount -1)){
                int leftitem = leftPx;
                int rightitem = 0;
                layoutParams.setMargins( leftitem , topPx , rightitem , bottomPx);
            }else if(i==0 && i == (itemCount-1)){
                int leftitem = leftPx;
                int rightitem = rightPx;
                layoutParams.setMargins( leftitem , topPx , rightitem , bottomPx);
            }else if( i == (itemCount-1) ){
                int leftitem = leftPx;
                int rightitem = rightPx;
                layoutParams.setMargins( leftitem , topPx , rightitem , bottomPx);
            }else {
                int leftitem = leftPx;
                int rightitem = 0;// rightPx;
                layoutParams.setMargins( leftitem , topPx , rightitem , bottomPx);
            }

            iv.setLayoutParams(layoutParams);
            iv.setOnClickListener(this);

            this.addView(iv);
            iv.setTag(item);

            String imageUrl = Variable.resourceUrl + item.getImageUrl();
            FrescoDraweeController.loadImage(iv, itemWidth, imageUrl );
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void onClick(View v) {
        if( v.getTag()!=null && v.getTag() instanceof AdImageBean){
            AdImageBean bean = (AdImageBean)v.getTag();
            String url = bean.getLinkUrl();
            String name = bean.getTitle();
            CommonUtil.link(name, url );
        }
    }
}
