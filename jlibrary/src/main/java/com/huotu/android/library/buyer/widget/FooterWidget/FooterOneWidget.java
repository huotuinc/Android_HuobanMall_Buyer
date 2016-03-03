package com.huotu.android.library.buyer.widget.FooterWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.FooterBean.FooterImageBean;
import com.huotu.android.library.buyer.bean.FooterBean.FooterOneConfig;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;

import org.greenrobot.eventbus.EventBus;

/**
 * 底部导航组件
 * Created by jinxiangdong on 2016/1/22.
 */
public class FooterOneWidget extends LinearLayout implements View.OnClickListener{
    private FooterOneConfig footerOneConfig;

    public FooterOneWidget(Context context , FooterOneConfig footerOneConfig) {
        super(context);

        this.footerOneConfig= footerOneConfig;

        this.setOrientation(HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(Color.parseColor( footerOneConfig.getBackgroundColor()));
        this.setPadding( 0, footerOneConfig.getTopMargion(),0,footerOneConfig.getBottomMargion() );

        if( footerOneConfig.getRows()==null || footerOneConfig.getRows().size()<1 ) return;
        //int itemWidth = getResources().getDisplayMetrics().widthPixels/ footerOneConfig.getList().size();
        for(FooterImageBean item : footerOneConfig.getRows()){
            LinearLayout ll = new LinearLayout(context);
            ll.setId(item.hashCode());
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            ll.setOrientation(VERTICAL);
            ll.setLayoutParams(layoutParams);
            ll.setOnClickListener(this);

            SimpleDraweeView iv = new SimpleDraweeView(context);
            layoutParams = new LayoutParams( item.getWidth() , ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            iv.setLayoutParams(layoutParams);
            ll.addView(iv);
            FrescoDraweeController.loadImage(iv, item.getWidth(), item.getImageUrl());

            TextView tv = new TextView(context);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            tv.setLayoutParams(layoutParams);
            tv.setText(item.getName());
            tv.setTextColor(Color.parseColor( footerOneConfig.getFontColor() ) );
            ll.addView(tv);

            this.addView(ll);
        }
    }

    @Override
    public void onClick(View v) {
        for( FooterImageBean item : footerOneConfig.getRows()) {
            if (v.getId() == item.hashCode()) {
                Toast.makeText(getContext(), item.getName(),Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(new LinkEvent( item.getName() , item.getLinkUrl()));
                break;
            }
        }
    }
}
