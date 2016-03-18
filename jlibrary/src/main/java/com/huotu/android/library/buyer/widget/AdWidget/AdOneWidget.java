package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.bean.AdBean.AdOneConfig;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;

import org.greenrobot.eventbus.EventBus;

import java.sql.Wrapper;

/**
 * 单一图片组件
 * Created by jinxiangdong on 2016/1/12.
 */
public class AdOneWidget extends LinearLayout implements View.OnClickListener{
    AdOneConfig config;
    public AdOneWidget(Context context , AdOneConfig config) {
        super(context);

        this.config = config;
        this.setOrientation(VERTICAL);

        if(config.getImages()==null || config.getImages().size()<1 ) return;

//        LayoutParams llLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        int leftPx = DensityUtils.dip2px(getContext(), config.getPaddingLeft());
//        int rightPx = DensityUtils.dip2px( getContext() , config.getPaddingRight() );
//        int topPx = DensityUtils.dip2px( getContext() , config.getPaddingTop() );
//        int bottomPx = DensityUtils.dip2px( getContext(),config.getPaddingBottom() );
//        llLayoutParams.setMargins( leftPx , topPx , rightPx  , bottomPx );
//        this.setLayoutParams(llLayoutParams);

        for(AdImageBean item : config.getImages()){
            createLayout(item);
        }
    }

    private void createLayout(AdImageBean item ){
        LinearLayout llItem = new LinearLayout(getContext());
        llItem.setOnClickListener(this);
        llItem.setTag(item);
        llItem.setOrientation(VERTICAL);

        SimpleDraweeView image1 = new SimpleDraweeView( getContext() );
        int width = getResources().getDisplayMetrics().widthPixels;
        LayoutParams layoutParams = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        image1.setLayoutParams(layoutParams);
        llItem.addView(image1);

        if( !TextUtils.isEmpty( item.getLinkName() ) ){
            TextView tvName = new TextView(getContext() );
            tvName.setText(item.getLinkName());
            llItem.addView(tvName);
        }

        int leftPx = DensityUtils.dip2px(getContext(), config.getPaddingLeft());
        int rightPx = DensityUtils.dip2px( getContext() , config.getPaddingRight() );
        int topPx = DensityUtils.dip2px( getContext() , config.getPaddingTop() );
        int bottomPx = DensityUtils.dip2px( getContext(),config.getPaddingBottom() );
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins( leftPx , topPx , rightPx , bottomPx );
        llItem.setLayoutParams(layoutParams);

        String imageUrl = Variable.resourceUrl + item.getImageUrl();
        FrescoDraweeController.loadImage(image1, width, imageUrl);

        this.addView(llItem);
    }

    @Override
    public void onClick(View v) {
        if( v.getTag()!=null && v.getTag() instanceof AdImageBean){
            AdImageBean bean = (AdImageBean)v.getTag();
            String url = Variable.siteUrl + bean.getLinkUrl();
            String name = bean.getLinkName();
            EventBus.getDefault().post(new LinkEvent(name,url));
        }
    }
}
