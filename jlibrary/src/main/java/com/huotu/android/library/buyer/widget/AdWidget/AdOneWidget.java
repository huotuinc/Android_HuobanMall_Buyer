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
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.bean.AdBean.AdOneConfig;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.sql.Wrapper;

/**
 * 单一图片组件
 * Created by iangdong on 2016/1/12.
 */
public class AdOneWidget extends BaseLinearLayout {
    AdOneConfig config;
    public AdOneWidget(Context context , AdOneConfig config) {
        super(context);

        this.config = config;
        this.setOrientation(VERTICAL);

        if(config.getImages()==null || config.getImages().size()<1 ) return;

        for(AdImageBean item : config.getImages()){
            createLayout(item);
        }
    }

    private void createLayout(AdImageBean item ){
        LinearLayout llItem = new LinearLayout(getContext());
        llItem.setOnClickListener(this);
        llItem.setTag(item);
        llItem.setOrientation(VERTICAL);

        int leftPx = DensityUtils.dip2px(getContext(), config.getPaddingLeft());
        int rightPx = DensityUtils.dip2px( getContext() , config.getPaddingRight() );
        int topPx = DensityUtils.dip2px( getContext() , config.getPaddingTop() );
        int bottomPx = DensityUtils.dip2px( getContext(),config.getPaddingBottom() );
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        llItem.setLayoutParams(layoutParams);

        SimpleDraweeView image1 = new SimpleDraweeView( getContext() );
        int width = getResources().getDisplayMetrics().widthPixels;
        width = width - leftPx - rightPx;

        layoutParams = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        image1.setLayoutParams(layoutParams);
        llItem.addView(image1);

        if( !TextUtils.isEmpty( item.getTitle() ) ){
            TextView tvName = new TextView(getContext() );
            tvName.setText(item.getTitle());
            llItem.addView(tvName);
        }

        String imageUrl = Variable.resourceUrl + item.getImageUrl();
        FrescoDraweeController.loadImage(image1, width, imageUrl);

        this.addView(llItem);
    }

    @Override
    public void onClick(View v) {
        if( v.getTag()!=null && v.getTag() instanceof AdImageBean){
            AdImageBean bean = (AdImageBean)v.getTag();
            String url = bean.getLinkUrl();
            //if( TextUtils.isEmpty( url )) return;
            String name = bean.getLinkName();
            CommonUtil.link(name , url);
        }
    }
}
