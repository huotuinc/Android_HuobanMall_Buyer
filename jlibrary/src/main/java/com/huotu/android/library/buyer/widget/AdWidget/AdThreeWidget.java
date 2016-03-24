package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.bean.AdBean.AdThreeConfig;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * 橱窗二组件 左一大右两小
 * Created by jingdong on 2016/1/14.
 */
public class AdThreeWidget extends BaseLinearLayout{
    private AdThreeConfig config;
    private SimpleDraweeView iv1;
    private SimpleDraweeView iv2;
    private SimpleDraweeView iv3;
    private LinearLayout ll1;

    public AdThreeWidget(Context context , AdThreeConfig config) {
        super(context);

        this.config = config;

        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(CommonUtil.parseColor( config.getBackcolor() ));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        iv1 = new SimpleDraweeView(context);
        iv1.setOnClickListener(this);

        ll1 = new LinearLayout(context);
        ll1.setOrientation(VERTICAL);

        iv2 = new SimpleDraweeView(context);
        iv2.setOnClickListener(this);
        iv3 = new SimpleDraweeView(context);
        iv3.setOnClickListener(this);

        int leftPx = DensityUtils.dip2px(context, config.getPaddingLeft());
        int rightPx = DensityUtils.dip2px( context , config.getPaddingRight());
        int topPx = DensityUtils.dip2px( context, config.getPaddingTop());
        int bottomPx = DensityUtils.dip2px( context , config.getPaddingBottom());
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
            String imageUrl = Variable.resourceUrl + config.getImages().get(0).getImageUrl();
            FrescoDraweeController.loadImage(iv1, itemWidth, imageUrl );
            iv1.setTag(config.getImages().get(0));
        }
        if( config.getImages().size()>1){
            int itemWidth = ( context.getResources().getDisplayMetrics().widthPixels - leftPx - rightPx)/2;
            String imageUrl = Variable.resourceUrl + config.getImages().get(1).getImageUrl();
            FrescoDraweeController.loadImage(iv2,itemWidth , imageUrl );
            iv2.setTag( config.getImages().get(1) );
        }
        if( config.getImages().size()>2){
            int itemWidth = ( context.getResources().getDisplayMetrics().widthPixels - leftPx - rightPx)/2;
            String imageUrl = Variable.resourceUrl + config.getImages().get(2).getImageUrl();
            FrescoDraweeController.loadImage(iv3,itemWidth, imageUrl );
            iv3.setTag( config.getImages().get(2) );
        }
    }

    @Override
    public void onClick(View v) {
        if( v.getTag()!=null && v.getTag() instanceof AdImageBean){
            AdImageBean bean = (AdImageBean)v.getTag();
            String url = bean.getLinkUrl();
            String name = bean.getLinkName();
            CommonUtil.link(name,url);
        }
    }
}
